package com.gsafer.smartoperation.validator;

import com.gsafer.smartoperation.util.Verify;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ResourceBundle;

/**
 * 校验器
 * Created by godv on 2017/8/3.
 */
public class IpValidator implements Validator {


    /**
     * 校验器方法
     * 校验ip格式 网段格式和子网掩码格式
     *
     * @param facesContext
     * @param uiComponent  ui组件
     * @param o            传入的校验信息
     * @throws ValidatorException 校验异常
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

        String validateString = (String) o;
        String msg = (String) uiComponent.getAttributes().get("msg");
        if (msg.equals("ip")) {
            if (!Verify.isIP(validateString)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/MyBundle").getString("ip"), ResourceBundle.getBundle("/MyBundle").getString("ip"));
                throw new ValidatorException(message);
            }
        } else if (msg.equals("segment")) {
            if (!Verify.isSegment(validateString)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/MyBundle").getString("segment"), ResourceBundle.getBundle("/MyBundle").getString("segment"));
                throw new ValidatorException(message);
            }
        } else if (msg.equals("mask")) {
            if (!Verify.isMask(validateString)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/MyBundle").getString("netMask"), ResourceBundle.getBundle("/MyBundle").getString("netMask"));
                throw new ValidatorException(message);
            }
        } else if (msg.equals("gw")) {
            if (!(validateString == null||"".equals(validateString))) {
                if (!Verify.isIP(validateString)) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/MyBundle").getString("ip"), ResourceBundle.getBundle("/MyBundle").getString("ip"));
                    throw new ValidatorException(message);
                }
            }
        }
    }
}
