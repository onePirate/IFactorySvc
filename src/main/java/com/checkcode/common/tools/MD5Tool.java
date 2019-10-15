package com.checkcode.common.tools;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

@Slf4j
public class MD5Tool {

    private static final String SALT = "cz_solt";

    public static String getMD5(String source){
        MessageDigest md5;
        StringBuilder sb=new StringBuilder();
        source += SALT;
        byte[] buf=source.getBytes();
        try{
            md5= MessageDigest.getInstance("MD5");
            md5.update(buf);
            byte[] data=md5.digest();
            for(byte b:data){
                sb.append(Integer.toHexString(b&0xff));
            }
        }catch(Exception e){
            log.error("md5 error {}",e);
        }
        return sb.toString();
    }

}
