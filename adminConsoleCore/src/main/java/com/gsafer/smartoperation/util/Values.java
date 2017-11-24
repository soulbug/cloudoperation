package com.gsafer.smartoperation.util;

import javax.enterprise.context.ApplicationScoped;
import java.util.ResourceBundle;

/**
 * date 2017/10/30
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用
 */
@ApplicationScoped
public class Values {
    public String getValue( String name){
        return ResourceBundle.getBundle("/MyBundle").getString(name);
    }
}
