/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpStatus;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Slf4j
/**
 * Created by ethan on 2017/8/1.
 */
public class OpenVpnFile {

    /**
     * 生成openVpn对应的tiger.ovpn
     *
     * @param mac            文件夹名称
     * @param hospitalNumber 医院编码
     */
    public static File createFile(String mac, String hospitalNumber) throws IOException, TemplateException {
        File file = new File(new Value().getFilePath() + mac + "/" + new Value().getTigerOvpnName());
        if (!file.exists()) {
            file.createNewFile();
        }
        StringWriter writer = null;
        FileOutputStream out = null;
        try {
            Configuration configuration = new Configuration();
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setTemplateLoader(new ClassTemplateLoader(OpenVpnFile.class, "/"));
            Template template = configuration.getTemplate(new Value().getTigerOvpnName());
            writer = new StringWriter();
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("hospitalNumber", hospitalNumber);
            template.process(context, writer);
            // 创建输出流FileOutputStream对象out
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

    /**
     * 生成openVpn证书和密码文件
     *
     * @param hospitalNumber 传入医院编码
     * @param mac            存放路径
     */
    public static String executeOpenVpnFile(String mac, String hospitalNumber) throws IOException, InterruptedException {
        String url = new Value().getVpnUrl();
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url + hospitalNumber);
        int status = httpClient.executeMethod(getMethod);
        if (status != HttpStatus.SC_OK) {
            return null;
        }

        String result = getMethod.getResponseBodyAsString();
        JSONObject vpnJson = JSON.parseObject(result);
        String vpnIp = vpnJson.getString("vpnIp");
        if (!Verify.isIP(vpnIp)) {
            return "notIp";
        }

        String caCrt = vpnJson.getString("caCrt");
        String clientKey = vpnJson.getString("clientKey");
        String clientCrt = vpnJson.getString("clientCrt");
        String userName = vpnJson.getString("userName");
        String password = vpnJson.getString("password");


        //根证书文件
        File caCrtFile = new File(new Value().getFilePath() + mac + "/ca.crt");
        createFileByString(caCrtFile, caCrt);

        //客户端证书文件
        File CrtFile = new File(new Value().getFilePath() + mac + "/" + hospitalNumber + ".crt");
        createFileByString(CrtFile, clientCrt);

        //客户端key文件
        File KeyFile = new File(new Value().getFilePath() + mac + "/" + hospitalNumber + ".key");
        createFileByString(KeyFile, clientKey);

        //创建password
        File passwordFile = new File(new Value().getFilePath() + mac + "/" + new Value().getPasswordName());
        if (!passwordFile.exists()) {
            passwordFile.createNewFile();
            // 创建输出流FileOutputStream对象out
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(passwordFile);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(userName + System.getProperty("line.separator"));
                stringBuffer.append(password + System.getProperty("line.separator"));
                out.write((stringBuffer.toString().getBytes("utf-8")));
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }

        return vpnIp;
    }

    private static void createFileByString(File file, String content) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes("UTF-8"));
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}

