/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import com.gsafer.smartoperation.entity.ProcessorRoute;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ethan on 2017/8/1.
 * 创建开机自启文件
 */
public class RcLocalFile {
    /**
     * 生成基础的开机自启文件
     *
     * @return 开机自启文件
     */
    public static File createRclocal(String name) throws IOException, TemplateException {
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(RcLocalFile.class, "/"));

        StringWriter writer = null;
        FileOutputStream out = null;
        try {
            Template template = configuration.getTemplate(new Value().getRcLocalName());
            writer = new StringWriter();
            Map<String, Object> context = new HashMap<String, Object>();
            template.process(context, writer);
            File file = new File(new Value().getFilePath() + name + "/" + new Value().getRcLocalName());
            //File file = new File("D:/peizhi/" + name + "/rc.local");

            boolean result;
            if (!file.exists()) {
                 result = file.createNewFile();
                // 创建输出流FileOutputStream对象out
            }
            out = new FileOutputStream(file);
            out.write(writer.toString().getBytes("UTF-8"));
            return file;
        } finally {
            if (out != null) {
                out.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static File createRoute(ProcessorRoute processorRoute, String mac) throws IOException {
        File fileRcLocal = new File(new Value().getFilePath() + mac + "/" + new Value().getRcLocalName());
        // File fileRcLocal = new File("D:/peizhi/" + mac + "/rc.local");
        FileOutputStream out = null;
        BufferedReader br = null;
        String line = null;
        String networkCardNumber = "";
        try {
            StringBuffer rcLocal = new StringBuffer();
            br = new BufferedReader(new FileReader(fileRcLocal));
            while ((line = br.readLine()) != null) {
                if (!line.equals("exit 0")) {
                    rcLocal.append(line.toString() + System.getProperty("line.separator"));
                }
            }

            String eth = null;
            if (0 == (processorRoute.getEth())) {
                eth = "dev eth0";
            } else if (1 == (processorRoute.getEth())) {
                eth = "dev eth1";
            }
            rcLocal.append("route add -net ");
            rcLocal.append(processorRoute.getNetmask());
            rcLocal.append(" gw ");
            rcLocal.append(processorRoute.getGateway());
            rcLocal.append(" ");
            rcLocal.append(eth + System.getProperty("line.separator"));
            rcLocal.append("exit 0" + System.getProperty("line.separator"));
            out = new FileOutputStream(fileRcLocal);
            out.write(rcLocal.toString().getBytes("utf-8"));
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return fileRcLocal;
    }

  /*  *//**
     * 生成开机自启文件的退出命令
     *
     * @param mac 存放配置文件的mac地址目录
     * @return
     * @throws IOException
     *//*
    public static void generateRclocalExit(String mac) throws IOException {
        File fileRclocal = new File(Value.FILE_PATH + mac + "/rc.local");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileRclocal, true);
            out.write("exit 0".toString().getBytes("utf-8"));
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }*/
}
