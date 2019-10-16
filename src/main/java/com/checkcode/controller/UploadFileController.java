package com.checkcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.checkcode.common.StateEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/")
public class UploadFileController {

    private SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat YMDHMS = new SimpleDateFormat("HHmmssSSS");

    @Value("${file.savePath}")
    String fileSavePath;
    @Value("${file.bakPath}")
    String fileBakPath;


    @PostMapping("/uploadFile")
    public Result fileUpload(@RequestParam("fileName") MultipartFile fileName) {
        if (fileName.isEmpty()) {
            return ResultTool.failed(StateEnum.REQ_HAS_ERR);
        }
        //选择了文件，开始进行上传操作
        try {
            String uploadFilePath = uploadOriFile(fileName,fileSavePath);
            uploadOriFile(fileName,fileBakPath);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileUrl",uploadFilePath);
            return ResultTool.successWithMap(jsonObject);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return ResultTool.failed(StateEnum.FAIL_UPLOADDATA);
    }

    /**
     * 保存文件到常用目录和备份目录
     * @param fileName
     * @throws IOException
     */
    private String uploadOriFile(MultipartFile fileName,String savePath) throws IOException {
        //构建上传目标路径
        File destFile = new File(savePath);
        if (!destFile.exists()) {
            destFile.mkdirs();
            if (savePath.contains(".")){
                String string=" attrib +H "+destFile.getAbsolutePath();
                Runtime.getRuntime().exec(string);
            }
        }
        File upload = new File(destFile.getAbsolutePath(), YMD.format(new Date()) + "/");
        if (!upload.exists()) {
            upload.mkdirs();
        }
        String fileAllName = fileName.getOriginalFilename();
        String filePrefixName = fileAllName.substring(0, fileAllName.lastIndexOf("."));
        String fileSuffixName = fileAllName.substring(fileAllName.lastIndexOf("."));
        String uploadFile = upload.getAbsolutePath() + "\\" + filePrefixName + "-" + YMDHMS.format(new Date()) + fileSuffixName;
        log.info("完整的上传路径:" + uploadFile);
        //根据srcFile的大小，准备一个字节数组
        byte[] bytes = fileName.getBytes();
        Path path = Paths.get(uploadFile);
        //最重要的一步，将源文件写入目标地址
        Files.write(path, bytes);
        return uploadFile;
    }
}
