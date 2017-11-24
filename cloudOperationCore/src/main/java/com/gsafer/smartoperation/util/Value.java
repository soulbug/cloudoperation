/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.util;

import com.gsafer.smartoperation.controller.util.JsfUtil;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by ethan on 2017/8/9.
 * 得到config.properties里的value值
 *
 * @author ethan
 */
@ApplicationScoped
public class Value {
    public Value() {
    }

    private Properties prop;

    private synchronized Properties getProperties() {
        if (prop == null) {
            prop = new Properties();
            InputStream in = Value.class.getResourceAsStream("/config.properties");
            try {
                prop.load(in);
            } catch (IOException e) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("ioError"));
            }
        }
        return prop;
    }

    /**
     * 该文件夹存放程序自动生成的mac地址文件夹
     */
    private String filePath;
    /**
     * 前置机上传的resin的日志文件夹
     */
    private String hisPath;
    /**
     * 前置机上传的mac的地址的文件夹
     */
    private String macPath;
    /**
     * zip包的地址
     */
    private String zipPath;
    /**
     * 医院新增接口传入的创建人
     */
    private String createUser;
    /**
     * 数据平台的Docker容器url
     */
    private String dataPlatformUrl;

    /**
     * 数据平台的外部url
     */
    private String publicDataPlatformUrl;

    /**
     * 数据平台医院的医院编码的数据库字段
     */
    private String dataHospitalNumberColumn;
    /**
     * 数据平台医院的医院名字的数据库字段
     */
    private String dataHospitalNameColumn;
    /**
     * 根证书文件名
     */
    private String caName;
    /**
     * openVpn密码文件名
     */
    private String passwordName;
    /**
     * openVpn tigervpn文件名
     */
    private String tigerOvpnName;
    /**
     * 网络配置文件名
     */
    private String interfacesName;
    /**
     * 程序包配置文件
     */
    private String configHisName;
    /**
     * 开机自启文件
     */
    private String rcLocalName;
    /**
     * 网段的正则表达式
     */
    private String segmentRexp;
    /**
     * ip的正在表达式
     */
    private String ipRexp;
    /**
     * 三级联动的省市区的json对象
     */
    private String jsonPath;
    /**
     * 端口隐射文件
     */
    private String rinetdName;
    /**
     * 该地区下没有新增过医院默认在后面加一
     */
    private String number;
    /**
     * 脚本目录
     */
    private String shellPath;
    /**
     * md5文件存放路径
     */
    private String md5Path;
    /**
     * 医院key
     */
    private String hKey;
    /**
     * 医院编码
     */
    private String hospitalNumber;
    /**
     * 医院目录编码
     */
    private String HospitalListNumber;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 类别
     */
    private String sort;
    /**
     * 规格
     */
    private String spec;
    /**
     * 项目单位
     */
    private String unit;
    /**
     * 剂型
     */
    private String betakeGenre;
    /**
     * 费用属性
     */
    private String feesSort;
    /**
     * 自费比例
     */
    private String selfPlay;

    /**
     * 三方编码
     */
    private String threeCode;
    /**
     * 限价
     */
    private String limitPrice;
    /**
     * 快捷码
     */
    private String swiftCode;
    /**
     * 备注
     */
    private String remark;

    /**
     * 统一医疗目录编码
     */
    private String unifyHospitalListNumber;
    /**
     * 获取vpnUrl的地址
     */
    private String vpnUrl;
    /**
     * 医疗项目编码
     */
    private String medicalNumber;
    /**
     * 是否生成vpn配置文件
     */
    private String isGenerateVPNCert;

    /**
     * 公司编码
     */
    private String managementOrgCode;
    /**
     * 定点状态码
     */
    private String designateCode;
    /**
     * 未定点状态码
     */
    private String noDesignateCode;
    /**
     * 记录创建人
     */
    private String recordCreator;

    /**
     * 前置机平台请求地址头部
     */
    private String platformUrlHead;

    /**
     * 前置机数据平台请求地址尾部
     */
    private String platformUrlFoot;

    /**
     * 前置机数据平台上传地址尾部
     */
    private String platformUrlUpload;

    /**
     * GWT-RPC请求参数-医院编码
     */
    private String gwtrpcPropertiesHospitalNumber;

    //上传到数据平台时要带入的参数

    public String getIsGenerateVPNCert() {
        if (isGenerateVPNCert == null) {
            return getProperties().getProperty("isGenerateVPNCert");
        } else {
            return isGenerateVPNCert;
        }
    }

    public String getUploadProperty() {
        if (uploadProperty == null) {
            return getProperties().getProperty("uploadProperty");
        } else {
            return uploadProperty;
        }
    }

    private String uploadProperty;

    public String getExpoerNotreatmentHospitalTitle() {
        if (expoerNotreatmentHospitalTitle == null) {
            return getProperties().getProperty("expoerNotreatmentHospitalTitle");
        } else {
            return expoerNotreatmentHospitalTitle;
        }
    }

    //未对码医疗目录导出名称以及标题
    private String expoerNotreatmentHospitalTitle;

//    public String getPlatformUrlHead() {
//        if (platformUrlHead == null) {
//            return getProperties().getProperty("platformUrlHead");
//        } else {
//            return platformUrlHead;
//        }
//    }
//
//    public String getPlatformUrlFoot() {
//        if (platformUrlFoot == null) {
//            return getProperties().getProperty("platformUrlFoot");
//        } else {
//            return platformUrlFoot;
//        }
//    }

    public String getPlatformUrlUpload() {
        if (platformUrlUpload == null) {
            return getProperties().getProperty("platformUrlUpload");
        } else {
            return platformUrlUpload;
        }
    }

    public String getGwtrpcPropertiesHospitalNumber() {
        if (gwtrpcPropertiesHospitalNumber == null) {
            return getProperties().getProperty("gwtrpcPropertiesHospitalNumber");
        } else {
            return gwtrpcPropertiesHospitalNumber;
        }
    }

    public String getShellPath() {
        if (shellPath == null) {
            return getProperties().getProperty("shellPath");
        } else {
            return shellPath;
        }
    }

    public String getFilePath() {
        if (filePath == null) {
            return getProperties().getProperty("filePath");
        } else {
            return filePath;
        }
    }

    public String getHisPath() {
        if (hisPath == null) {
            return getProperties().getProperty("hisPath");
        } else {
            return hisPath;
        }
    }

    public String getMacPath() {
        if (macPath == null) {
            return getProperties().getProperty("macPath");
        } else {
            return macPath;
        }
    }

    public String getZipPath() {

        if (zipPath == null) {
            return getProperties().getProperty("zipPath");
        } else {
            return zipPath;
        }
    }

    public String getCreateUser() {

        if (createUser == null) {
            return getProperties().getProperty("createUser");
        } else {
            return createUser;
        }
    }

    public String getDataPlatformUrl() {

        if (dataPlatformUrl == null) {
            return getProperties().getProperty("dataPlatformUrl");
        } else {
            return dataPlatformUrl;
        }
    }

    public String getDataHospitalNumberColumn() {
        if (dataHospitalNumberColumn == null) {
            return getProperties().getProperty("dataHospitalNumberColumn");
        } else {
            return dataHospitalNumberColumn;
        }
    }

    public String getDataHospitalNameColumn() {
        if (dataHospitalNameColumn == null) {
            return getProperties().getProperty("dataHospitalNameColumn");
        } else {
            return dataHospitalNameColumn;
        }
    }

    public String getCaName() {
        if (caName == null) {
            return getProperties().getProperty("caName");
        } else {
            return caName;
        }
    }

    public String getPasswordName() {
        if (passwordName == null) {
            return getProperties().getProperty("passwordName");
        } else {
            return passwordName;
        }
    }

    public String getTigerOvpnName() {
        if (tigerOvpnName == null) {
            return getProperties().getProperty("tigerOvpnName");
        } else {
            return tigerOvpnName;
        }
    }

    public String getInterfacesName() {
        if (interfacesName == null) {
            return getProperties().getProperty("interfacesName");
        } else {
            return interfacesName;
        }
    }

    public String getConfigHisName() {
        if (configHisName == null) {
            return getProperties().getProperty("configHisName");
        } else {
            return configHisName;
        }
    }

    public String getRcLocalName() {
        if (rcLocalName == null) {
            return getProperties().getProperty("rcLocalName");
        } else {
            return rcLocalName;
        }
    }

    public String getSegmentRexp() {
        if (segmentRexp == null) {
            return getProperties().getProperty("segmentRexp");
        } else {
            return segmentRexp;
        }
    }

    public String getIpRexp() {
        if (ipRexp == null) {
            return getProperties().getProperty("ipRexp");
        } else {
            return ipRexp;
        }
    }

    public String getJsonPath() {
        if (jsonPath == null) {
            return getProperties().getProperty("jsonPath");
        } else {
            return jsonPath;
        }
    }

    public String getRinetdName() {
        if (rinetdName == null) {
            return getProperties().getProperty("rinetdName");
        } else {
            return rinetdName;
        }
    }

    public String getNumber() {
        if (number == null) {
            return getProperties().getProperty("number");
        } else {
            return number;
        }
    }

    public String getMd5Path() {
        if (hKey == null) {
            return getProperties().getProperty("md5Path");
        } else {
            return hKey;
        }
    }

    public String gethKey() {
        if (md5Path == null) {
            return getProperties().getProperty("hKey");
        } else {
            return number;
        }
    }

    public String getNoDesignateCode() {

        if (noDesignateCode == null) {
            return getProperties().getProperty("noDesignate");
        } else {
            return noDesignateCode;
        }
    }

    public String getDesignateCode() {

        if (designateCode == null) {
            return getProperties().getProperty("alreadyDesignate");
        } else {
            return designateCode;
        }
    }

    public String getRecordCreator() {

        if (recordCreator == null) {
            return getProperties().getProperty("recordCreator");
        } else {
            return recordCreator;
        }
    }

    public String getManagementOrgCode() {

        if (managementOrgCode == null) {
            return getProperties().getProperty("managementOrgCode");
        } else {
            return managementOrgCode;
        }
    }

    public String getHospitalNumber() {
        if (hospitalNumber == null) {
            return getProperties().getProperty("hospitalNumber");
        } else {
            return hospitalNumber;
        }
    }

    public String getHospitalListNumber() {
        if (HospitalListNumber == null) {
            return getProperties().getProperty("HospitalListNumber");
        } else {
            return HospitalListNumber;
        }
    }

    public String getProjectName() {
        if (projectName == null) {
            return getProperties().getProperty("projectName");
        } else {
            return projectName;
        }
    }

    public String getSort() {
        if (sort == null) {
            return getProperties().getProperty("sort");
        } else {
            return sort;
        }
    }

    public String getSpec() {
        if (spec == null) {
            return getProperties().getProperty("spec");
        } else {
            return spec;
        }
    }

    public String getUnit() {
        if (unit == null) {
            return getProperties().getProperty("unit");
        } else {
            return unit;
        }
    }

    public String getBetakeGenre() {
        if (betakeGenre == null) {
            return getProperties().getProperty("betakeGenre");
        } else {
            return betakeGenre;
        }
    }

    public String getFeesSort() {
        if (feesSort == null) {
            return getProperties().getProperty("feesSort");
        } else {
            return feesSort;
        }
    }

    public String getSelfPlay() {
        if (selfPlay == null) {
            return getProperties().getProperty("selfPlay");
        } else {
            return selfPlay;
        }
    }

    public String getThreeCode() {
        if (threeCode == null) {
            return getProperties().getProperty("threeCode");
        } else {
            return threeCode;
        }
    }

    public String getLimitPrice() {
        if (limitPrice == null) {
            return getProperties().getProperty("limitPrice");
        } else {
            return limitPrice;
        }
    }

    public String getSwiftCode() {
        if (swiftCode == null) {
            return getProperties().getProperty("swiftCode");
        } else {
            return swiftCode;
        }
    }

    public String getRemark() {
        if (remark == null) {
            return getProperties().getProperty("remark");
        } else {
            return remark;
        }
    }

    public String getUnifyHospitalListNumber() {
        if (unifyHospitalListNumber == null) {
            return getProperties().getProperty("unifyHospitalListNumber");
        } else {
            return unifyHospitalListNumber;
        }
    }

    public String getMedicalNumber() {
        if (medicalNumber == null) {
            return getProperties().getProperty("medicalNumber");
        } else {
            return medicalNumber;
        }
    }

    public String getVpnUrl() {
        if (vpnUrl == null) {
            return getProperties().getProperty("vpnUrl");
        } else {
            return vpnUrl;
        }
    }

    public String getPublicDataPlatformUrl() {
        if (publicDataPlatformUrl == null) {
            return getProperties().getProperty("publicDataPlatformUrl");
        } else {
            return publicDataPlatformUrl;
        }
    }

}
