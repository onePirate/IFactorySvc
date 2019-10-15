package com.checkcode.common.tools;

import com.checkcode.common.entity.TokenModel;
import com.checkcode.entity.pojo.EmployeePojo;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class TokenTool {

    private static Map<String,TokenModel> tokenMap = new ConcurrentHashMap<>();

    /**
     * 获取tokenMap
     * @return
     */
    public static Map<String,TokenModel> getTokenMap(){
        return tokenMap;
    }

    /**
     * 获取token信息
     * @param token
     * @return
     */
    public static TokenModel getTokenInfo(String token){
        return tokenMap.get(token);
    }

    /**
     * 删除token信息
     * @param token
     * @return
     */
    public static TokenModel removeToken(String token){
        return tokenMap.remove(token);
    }

    /**
     * 更新token信息
     * @param token
     * @return
     */
    public static TokenModel updateTokenInfo(String token){
        TokenModel tokenModel = tokenMap.get(token);
        tokenModel.setLoginTime(System.currentTimeMillis());
        return tokenMap.put(token,tokenModel);
    }

    /**
     * 创建token
     * @param employeePojo
     * @return
     * @throws Exception
     */
    public static String createToken(EmployeePojo employeePojo) {
        long loginTime = System.currentTimeMillis();
        String token = getGUID();
        TokenModel tokenModel = TokenModel.builder().loginTime(loginTime).employeeNo(employeePojo.getEmployeeNo()).build();
        tokenMap.put(token, tokenModel);
        return token;
    }

    /**
     * 生成16位不重复的随机数，含数字+大小写
     * @return
     */
    private static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }



}
