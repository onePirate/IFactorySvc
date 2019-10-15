package com.checkcode.common.tools;

import java.security.SecureRandom;
import java.util.UUID;

public class IdWorker {

    private static SecureRandom secureRandom = new SecureRandom();

    /**
     * 随机生成一个16位数字
     * @return
     */
    public static String getCodeByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 生成一个四位数的随机数
     * @return
     */
    public static String getNoByUUId() {
        int empNo = secureRandom.nextInt(9999) + 10000;
        return String.valueOf(empNo);
    }
}
