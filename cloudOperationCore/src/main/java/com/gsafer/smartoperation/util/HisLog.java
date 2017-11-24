/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Created by ethan on 2017/8/8.
 * 检查日志信息
 */
@Slf4j
public class HisLog {
    /**
     * @param mac 用来查找mac地址对应的日志文件
     * @return 存放了日志信息 包括是否成功和详细的日志信
     * @throws IOException 输出流异常
     */
    public static String checkHisFile(String mac) throws IOException {
       // Map<String, Object> map = new HashMap<>();
        //hisLog文件
        File fileHis = new File(new Value().getHisPath() + mac + "_his.log");
        //mac地址文件
        File fileMac = new File(new Value().getMacPath() + mac);
        //构造一个BufferedReader类来读取hisLog文件
        BufferedReader br = null;
        //构造一个一个输出流将文件存入StringBuffer中
        FileOutputStream out = null;
        StringBuffer hisString = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(fileHis));
            out = new FileOutputStream(fileMac);

            String hisLog = null;
            //使用readLine方法，一次读一行
            while ((hisLog = br.readLine()) != null) {
                if ("true".equals(hisLog)) {
                    log.info(hisLog);
                    out.write("true".getBytes());
                    hisString.append("true"+System.lineSeparator());
                } else if ("false".equals(hisLog)) {
                    hisString.append("false"+System.lineSeparator());
                } else {
                    hisString.append(hisLog+System.lineSeparator());
                }
                //写入页面
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (br!=null){
                br.close();
            }
        }
        return hisString.toString();
    }
}
