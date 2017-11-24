/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ethan on 2017/8/1.
 * @author ethan
 * 判断是不是符合ip或者掩码规则
 */
public class Verify {
    /**
     * 验证ip
     *
     * @param ip 传入的ip
     * @return ip的boolean值
     */
    public static boolean isIP(String ip) {
        if (ip.length() < 7 || ip.length() > 15 || "".equals(ip)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
       String rexp =new Value().getIpRexp();

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(ip);

        return mat.find();
    }

    /**
     * 验证mask
     *
     * @param mask 子网掩码
     * @return mask的boolean值
     */
    public static boolean isMask(String mask) {
        String[] marks = {
                "255.255.255.0",
                "128.0.0.0",
                "192.0.0.0",
                "224.0.0.0",
                "240.0.0.0",
                "248.0.0.0",
                "252.0.0.0",
                "254.0.0.0",
                "255.0.0.0",
                "255.128.0.0",
                "255.192.0.0",
                "255.224.0.0",
                "255.240.0.0",
                "255.248.0.0",
                "255.252.0.0",
                "255.254.0.0",
                "255.255.0.0",
                "255.255.128.0",
                "255.255.192.0",
                "255.255.224.0",
                "255.255.240.0",
                "255.255.248.0",
                "255.255.252.0",
                "255.255.254.0",
                "255.255.255.128",
                "255.255.255.192",
                "255.255.255.224",
                "255.255.255.240",
                "255.255.255.248",
                "255.255.255.252",
                "255.255.255.254",
                "255.255.255.255"
        };
        for (String markAddress : marks) {
            if (markAddress.equals(mask)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param segment 输入的网段
     * @return 是不是正确的网段
     */
    public static boolean isSegment(String segment){
        String rexp =new Value().getSegmentRexp();
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(segment);
        return mat.find();
    }

}
