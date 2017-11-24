package com.gsafer.smartoperation.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ResourceBundle;
@Slf4j
/**
 * date 2017/8/26
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用 读取code.json的json
 */
 class ReadJson {
    /**
     * 读取code.json的json
     * @return city对象的json字符串
     */
     static String readFile()  {
        //json的地址
        String path=new Value().getJsonPath();
        BufferedReader reader = null;
        StringBuffer jsonString=new StringBuffer();
        InputStreamReader inputStreamReader=null;
       // FileInputStream fileInputStream=null;
        InputStream in = ReadJson.class .getResourceAsStream( path );
        try{
            inputStreamReader= new InputStreamReader(in, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString ;
            while((tempString = reader.readLine()) != null){
                jsonString .append(tempString);
            }

        } catch (IOException e) {
            log.error(ResourceBundle.getBundle("/MyBundle").getString("areaCode"),e);
        } finally{

                try {
                    if (reader!=null){
                        reader.close();
                    }
                   if (inputStreamReader!=null){
                       inputStreamReader.close();
                   }
                } catch (IOException e) {
                    log.error(ResourceBundle.getBundle("/MyBundle").getString("areaCode"),e);
                }
        }
        return jsonString.toString();
    }

}
