/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 2017/8/1.
 * @author ethan
 * 校验类
 */
@Slf4j
public class VerifyFile {
    /**
     * 校验网络配置文件
     *
     * @param file 网络配置文件
     * @return true ：有只有一个或者两个网络配置文件 网管只能存在一个
     */
    public static boolean verifyNetworkCard(File file) throws IOException {

        //文件输入流
        FileInputStream fis = null;
        //文件读流
        Reader bis = null;
        //缓冲读流
        BufferedReader br = null;
        try {
            fis = new FileInputStream(file);
            bis = new InputStreamReader(fis);
            br = new BufferedReader(bis);
            //行的内容
            String line;
            //ip的个数
            int count = 0;
            //网关的个数
            int gatewayCount = 0;
            //网卡etho的个数
            int countEth0 = 0;
            //网卡eth1的个数
            int countEth1 = 0;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.contains("address")) {
                    count++;
                }
                if (line.contains("auto eth0")) {
                    countEth0++;
                }
                if (line.contains("auto eth1")) {
                    countEth1++;
                }
                if (line.contains("gateway")) {
                    gatewayCount++;
                }
            }
            boolean result = (count == 2 || count == 1) && gatewayCount == 1 && ((countEth0 + countEth1) > 0 && (countEth0 + countEth1) <= 2) && (countEth0) != 2 && countEth1 != 2;
            //ip地址有至少有一个，最多有两个，网关最多一个，网卡至少有一个，最多有两个
            if (result) {
                return true;
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (br != null) {
                br.close();
            }
            if (bis != null) {
                bis.close();
            }

        }
        return false;
    }

    /**
     * 校验mac目录下是否生成了对应文件
     *
     * @param hospitalNumber 校验哪个mac地址目录
     * @return List<String> 存放缺少name的list
     * @throws IOException 未找到目录
     */
    public static List<String> verifyZipFile(String mac, String hospitalNumber) throws IOException {
        List<String> fileName = new ArrayList<>();
        File file = new File(new Value().getFilePath() + mac);
        // int count = 0;
        //存放mac目录下实际文件的名字
        List<String> listFileNames = new ArrayList<>();
        //存放mac应该存放文件的名字
        List<String> listFileName = new ArrayList<>();
        listFileName.add(new Value().getCaName());
        listFileName.add(hospitalNumber + ".crt");
        listFileName.add(hospitalNumber + ".key");
        listFileName.add(new Value().getPasswordName());
        listFileName.add(new Value().getTigerOvpnName());
        listFileName.add(new Value().getInterfacesName());
        listFileName.add(new Value().getRcLocalName());
        listFileName.add(new Value().getConfigHisName());
        File[] configFiles = file.listFiles();
        if (configFiles != null) {
            for (int i = 0; i < configFiles.length; i++) {
                listFileNames.add(configFiles[i].getName());
            }
            log.info(file.getAbsolutePath(), listFileNames.toString());
            for (int i = 0; i < listFileName.size(); i++) {
                if (!listFileNames.contains(listFileName.get(i))) {
                    fileName.add(listFileName.get(i));
                }
            }
            return fileName;
        } else {
            return listFileName;
        }


    }
}
