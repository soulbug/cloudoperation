/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import com.gsafer.smartoperation.entity.ProcessorMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成映射文件
 * Created by ethan on 2017/8/1.
 */
public class RintedFile {
    /**
     * 生成端口映射文件
     *
     * @param processorMap 端口转发对象
     */
    public static File createRinted(ProcessorMap processorMap, String mac) throws IOException, TemplateException {
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(RintedFile.class, "/"));
        //得到模板rinetd.conf模板
        Template template = configuration.getTemplate( new Value().getRinetdName());
        StringWriter writer = null;
        FileOutputStream out = null;
        File file = null;
        try {
            writer = new StringWriter();
            Map<String, Object> context = new HashMap<>(2);
            context.put("address", processorMap.getIp());
            context.put("port", Short.toString(processorMap.getPortIn()));
            //修改对应的值
            template.process(context, writer);
            file = new File(new Value().getFilePath() + mac + "/"+new Value().getRinetdName());
            if (!file.exists()) {
                file.createNewFile();
                // 创建输出流FileOutputStream对象out

            }
            out = new FileOutputStream(file);
            out.write(writer.toString().getBytes("UTF-8"));
        } finally {
            if (out != null) {
                out.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        return file;
    }
}