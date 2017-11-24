package com.gsafer.smartoperation.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;

/**
 * 文件上传工具类
 * Created by vince on 2017/10/25.
 *  将检查无误的已对码医疗目录文件上传到对应的前置机数据平台
 * @version 0.1
 * @do
 */
@Slf4j
public class UploadFileUtils {

    /**
     * 上传已对码医疗目录到对应前置机数据平台地址
     * @param url 平台上传请求地址
     * @param filePath 服务器文件地址
     * @return  返回的成功信息和对应名称
     */
    public static String uploadExcel(String url, String filePath){

        //初始化HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        //初始化返回值
        String tempResult="";
        try {
            //使用post的方式请求
            HttpPost httppost = new HttpPost(url);
            //初始化要上传的文件对象
            FileBody bin = new FileBody(new File(filePath));
            //初始化要传入的参数
            StringBody comment = new StringBody(new Value().getUploadProperty());
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("file", bin);//file为请求后台的File upload;属性
            reqEntity.addPart("module", comment);//module为请求后台的普通参数;属性
            httppost.setEntity(reqEntity);
            //返回值
            HttpResponse response = httpclient.execute(httppost);
            //状态码
            int statusCode = response.getStatusLine().getStatusCode();
            //如果是200 就是成功
            if(statusCode == HttpStatus.SC_OK){
                HttpEntity resEntity = response.getEntity();
                tempResult= EntityUtils.toString(resEntity);
                //httpclient自带的工具类读取返回数据
                EntityUtils.consume(resEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception e) {
                log.warn("关闭连接出错！",e);
            }
        }
        return tempResult;
    }
}
