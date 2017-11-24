package com.gsafer.smartoperation.util;

import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.entity.ProcessorConfig;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by ethan on 2017/8/1.
 * 创建网络配置文件
 */
@Slf4j
public class NetworkFile {

    /**
     * 生成 网络配置file
     * @param processorConfig 网络信息对象
     * @return 对应网络配置file
     */
    public static File generateNetWorkFile(List<ProcessorConfig> processorConfig, String name) throws IOException, TemplateException {

        {
            File file = new File(new Value().getFilePath() + name + "/"+new Value().getInterfacesName());
            if (!file.exists()) {
                file.createNewFile();
            }
            Configuration configuration = new Configuration();
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setTemplateLoader(new ClassTemplateLoader(NetworkFile.class, "/"));
            if (processorConfig.size()==1){
                Template template = configuration.getTemplate("interfacesOne");
                StringWriter writer = null;
                FileOutputStream out = null;
                String networkCardNumber = "";
                if (0 == (processorConfig.get(0).getEth())) {
                    networkCardNumber = "eth0";
                } else if (1 == (processorConfig.get(0).getEth())) {
                    networkCardNumber = "eth1";
                }
                try {
                    writer = new StringWriter();
                    Map<String, Object> context = new HashMap<String, Object>();
                    context.put("eth0", networkCardNumber);
                    context.put("address", processorConfig.get(0).getIp());
                    context.put("gateway", processorConfig.get(0).getGateway());
                    context.put("netmask", processorConfig.get(0).getNetMask());
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

            }if(processorConfig.size()==2) {
                //有网关的网卡的网络信息
                // todo:
                ProcessorConfig processorConfigEth0=null;
                ProcessorConfig processorConfigEth1=null;
                for (int i=0;i<processorConfig.size();i++){
                    if (processorConfig.get(i).getGateway()==null||"".equals(processorConfig.get(i).getGateway())){
                        processorConfigEth1=processorConfig.get(i);
                    }else{
                        processorConfigEth0=processorConfig.get(i);
                    }
                }
                //网卡信息不能为空
                if(processorConfigEth1==null||processorConfigEth0==null){
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("netWork"));
                    log.warn(ResourceBundle.getBundle("/MyBundle").getString("netWork"));
                }else {
                    Template template = configuration.getTemplate("interfacesTwo");
                    StringWriter writer = null;
                    FileOutputStream out = null;
                    String networkCardNumber0 = "";
                    if (0 == (processorConfigEth0.getEth())) {
                        networkCardNumber0 = "eth0";
                    } else if (1 == (processorConfigEth0.getEth())) {
                        networkCardNumber0 = "eth1";
                    }
                    String networkCardNumber1 = "";
                    if (0 == (processorConfigEth1.getEth())) {
                        networkCardNumber1 = "eth0";
                    } else if (1 == (processorConfigEth1.getEth())) {
                        networkCardNumber1 = "eth1";
                    }
                    try {
                        writer = new StringWriter();
                        Map<String, Object> context = new HashMap<String, Object>();
                        context.put("eth0", networkCardNumber0);
                        context.put("address", processorConfigEth0.getIp());
                        context.put("gateway", processorConfigEth0.getGateway());
                        context.put("netmask", processorConfigEth0.getNetMask());
                        context.put("eth1", networkCardNumber1);
                        context.put("address1", processorConfigEth1.getIp());
                        context.put("netmask1", processorConfigEth1.getNetMask());
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
                }

            }
            return file;
        }
    }


}
