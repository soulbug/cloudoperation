/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import java.io.File;

/**
 * 创建存放医院配置的文件夹
 * Created by ethan on 2017/8/2.
 * @author ethan
 */
public class MacDir {

    /**
     *
     * @param mac 创建以mac地址命名的文件夹
     * @return true 成功
     */
    public static boolean generateDir(String mac) {
        File file = new File(new Value().getFilePath() + mac);
        // 判断文件否存在
        boolean flag=false;
        if (!file.exists()) {
            flag = file.mkdirs();
        }
        return flag;
    }

}
