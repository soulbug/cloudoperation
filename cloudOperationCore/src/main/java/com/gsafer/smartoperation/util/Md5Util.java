package com.gsafer.smartoperation.util;

/**
 * date 2017/10/16
 *
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用 生成MD5
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Util {

    public Md5Util() {
    }
    /**
     * @param file zip文件
     * @return MD5值
     * @throws IOException              io
     * @throws NoSuchAlgorithmException MD5
     */
    private  String getStringMD5(File file) throws IOException, NoSuchAlgorithmException {

        if (file.exists() && file.isFile()) {
            DigestInputStream digestInputStream;
            MessageDigest digest;
            FileInputStream in = null;
            byte[] buffer = new byte[1024];

            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);
                digestInputStream = new DigestInputStream(in, digest);
                int len;
                while (digestInputStream.read(buffer) > 0) {
                    digest = digestInputStream.getMessageDigest();
                }
                byte[] resultByteArray = digest.digest();

                return byteArrayToHex(resultByteArray);
            } finally {
                if (in != null) {
                    in.close();
                }

            }


        } else {
            return null;
        }
    }


      private static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

        char[] resultCharArray = new char[byteArray.length * 2];


        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

        int index = 0;

        for (byte b : byteArray) {

            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];

            resultCharArray[index++] = hexDigits[b & 0xf];

        }


        // 字符数组组合成字符串返回

        return new String(resultCharArray);
    }

    /**
     * @param mac         传入的mac地址的zip包
     * @param fileContent 写入的内容
     * @throws IOException io
     */
    private void createFile(String mac, String fileContent) throws IOException {
        String filePath = (new Value()).getMd5Path() + "/" + mac + ".txt";
       // String filePath = "D:/opt/smartOperation/md5" + "/" + mac + ".txt";
        File file = new File(filePath);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            out.write(fileContent.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }

        }

    }

    /**
     * @param zipFile zip文件
     * @param mac     mac地址
     * @throws IOException              生成文件异常
     * @throws NoSuchAlgorithmException MD5异常
     */
  public void getFileMD5(File zipFile, String mac) throws IOException, NoSuchAlgorithmException {
    String md5=  this.getStringMD5(zipFile);
      this.createFile(mac, md5);
    }
}