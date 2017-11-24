/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建resin下的config_his.proterties
 * Created by godv on 2017/8/1.
 */
public class PropertiesFile {
    /**
     * 生成端口映射文件
     *
     * @param hospitalKey    加密之后的key
     * @param hospitalNumber 医院编码
     */
    public static  File createRinted(String mac, String hospitalKey, String hospitalNumber) throws IOException, TemplateException {
        File file = new File(new Value().getFilePath() + mac + "/"+new Value().getConfigHisName());
        if (!file.exists()) {
            file.createNewFile();
        }
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(PropertiesFile.class, "/"));
        Template template = configuration.getTemplate(new Value().getConfigHisName());
        StringWriter writer = null;
        FileOutputStream out = null;
        try {
            writer = new StringWriter();
            Map<String, Object> context = new HashMap<>(2);
            context.put("hospitalNumber", hospitalNumber);
            context.put("hospitalKey", hospitalKey);
            // 修改模板文件里对应的值
            template.process(context, writer);
            // 创建输出流FileOutputStream对象out
            out = new FileOutputStream(file);
            out.write(writer.toString().getBytes("UTF-8"));
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (out != null) {
                out.close();
            }
        }

        return file;
    }

}
