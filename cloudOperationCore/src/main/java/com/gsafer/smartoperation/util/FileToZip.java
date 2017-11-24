/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 将指定文件夹下面的文件打包成zip
 * Created by ethan on 2017/8/1.
 */
public class FileToZip {
    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return 打包是否成功
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) throws IOException, NoSuchAlgorithmException {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        //文件输入流
        FileInputStream fis = null;
        //缓存输入流
        BufferedInputStream bis = null;
        //文件输出流
        FileOutputStream fos = null;
        //压缩包输出流
        ZipOutputStream zos = null;
        try {
            File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
            File[] sourceFiles = sourceFile.listFiles();
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            byte[] bytes = new byte[1024 * 10];
            if (sourceFiles==null){
                return false;
            }
            for (File file:sourceFiles) {
                //创建ZIP实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                //读取待压缩的文件并写进压缩包里
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis, 1024 * 10);
                int read ;
                while ((read = bis.read(bytes, 0, 1024 * 10)) != -1) {
                    zos.write(bytes, 0, read);
                }
                flag = true;
                //关闭流
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (zos != null) {
                zos.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }
}
