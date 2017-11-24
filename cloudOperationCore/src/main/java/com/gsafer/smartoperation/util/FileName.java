/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2017/7/31.
 * 指定目录的mac地址文件名
 */
public class FileName {
    ;

    /**
     * @param path 指定文件夹
     * @return 一个存mac地址的list
     */
    public static List<String> getAllFileName(String path) {
        List<String> fileName = new ArrayList<>();
        File file = new File(path);
        return getAllFileName(file, fileName);
    }

    /**
     * @param file 文件
     * @return 一个存放一个文件夹的所有文件名
     */
    private static List<String> getAllFileName(File file, List<String> fileName) {

        File[] macFiles = file.listFiles();
        if (macFiles != null) {
            for (File macFile : macFiles) {
                if (macFile.isFile()) {
                    fileName.add(macFile.getName());
                }
            }
        }
        return fileName;
    }
}