/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.controller;

import com.gsafer.cis.remoting.RemotingHelper;
import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.entity.*;
import com.gsafer.smartoperation.facade.HospitalOperationFacade;
import com.gsafer.smartoperation.facade.ProcessorConfigFacade;
import com.gsafer.smartoperation.facade.ProcessorMapFacade;
import com.gsafer.smartoperation.facade.ProcessorRouteFacade;
import com.gsafer.smartoperation.util.*;
import com.jckj.cis.client.hospitalinfo.service.HospitalInfoService;
import com.jckj.cis.entity.HospitalInfo;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import net.creative.tools.gwt.client.PaginationSupport;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterion;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterionType;
import net.creative.tools.gwt.client.datasource.sqlcriteria.QueryImpl;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 医院实施的controller
 * update by ethan on 2017/8/1.
 *
 * @author ethan
 */
@Named(value = "hospitalOperationController")
@ViewScoped
@Slf4j
public class HospitalOperationController extends AbstractController<HospitalOperation> {
    /**
     * 读取指定文件下的mac地址文件
     */
    private List<String> fileNames;
    @Inject
    private HospitalOperationFacade hospitalOperationFacade;
    @Inject
    private ProcessorRouteFacade processorRouteFacade;
    @Inject
    private ProcessorConfigFacade processorConfigFacade;
    @Inject
    private ProcessorMapFacade processorMapFacade;
    @Inject
    private transient Value value;
    @Inject
    private transient MapArea mapArea;

    /**
     * 日志信息
     */
    private String hisString;
    /**
     * resin日志文件是否是成功的
     */
    private boolean hisLog;
    /**
     * 数据平台的存放url地址对象
     */
    private RemotingHelper remotingHelper;
    /**
     * json数据里省份的id
     */
    private Integer province;
    /**
     * json数据里市的id
     */
    private Integer city;
    /**
     * 所有省
     */
    private List<City> provinceList;
    /**
     * 省下面所有市
     */
    private List<City> cityList;
    /**
     * 市下面区或者县
     */
    private List<City> areaList;

    public List<HospitalOperation> getHospitalOperationList() {
        return hospitalOperationFacade.getAllNewHospital();
    }

    public void setHospitalOperationList(List<HospitalOperation> hospitalOperationList) {
        this.hospitalOperationList = hospitalOperationList;
    }

    /**医院列表信息*/
    private List<HospitalOperation> hospitalOperationList;

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public List<City> getProvinceList() {

        return mapArea.getListProvince();
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public List<City> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<City> areaList) {
        this.areaList = areaList;
    }

    public String getHisString() {
        return hisString;
    }

    public RemotingHelper getRemotingHelper() {
        if (remotingHelper == null) {
            remotingHelper = new RemotingHelper();
        }
        remotingHelper.setWebRootUrl(value.getDataPlatformUrl());
        return remotingHelper;
    }


    public void setHisString(String hisString) {
        this.hisString = hisString;
    }


    public HospitalOperationController() {
        super(HospitalOperation.class);
    }

    /**
     * 将没有实施医院的mac地址显示到页面，供实施人员选择
     * update by ethan on 2017/8/1.
     *
     * @return 存放mac的list
     */
    public List<String> getFileNames() {
        List<String> macFile = FileName.getAllFileName(value.getMacPath());
        List<String> macDb = hospitalOperationFacade.getAllMac();
        for (int i = 0; i < macFile.size(); i++) {
            if (macDb.contains(macFile.get(i))) {
                macFile.remove(i);
                i--;
            }
        }
        log.info(ResourceBundle.getBundle("/MyBundle").getString("macFileName"), macFile.toString());
        return macFile;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    /**
     * 得到选择省下面的所有市
     */
    public void changeListForCity(AjaxBehaviorEvent event) {
        Integer id = province;
        cityList = mapArea.getMapCity().get(id);
    }

    /**
     * 得到选择市下面的所有区
     */
    public void changeListForArea(AjaxBehaviorEvent event) {
        int id = city;
        areaList = mapArea.getMapArea().get(id);
    }

    @Override
    /**
     * 查询同一行政区划代码hospitalNumber最大的医院，将最大hospitalNumber加一赋值给新增医院，调用新增医院接口新增
     * 同时在平台新增医院
     * update by ethan on 2017/8/1.
     * @param event 表示当前正在进行的事件
     */
    public void saveNew(ActionEvent event) {
        HospitalOperation hospitalOperation = getSelected();
        //1.调用平台查询行政区划代码下的所有医院，返回该行政区划代码下hopitalNumber最大医院，自动生成HospitalNumber
        List<HospitalInfo> listHospitalNumberMax = getHospitalList(hospitalOperation.getAreaCode(), value.getDataHospitalNumberColumn());
        log.info(ResourceBundle.getBundle("/MyBundle").getString("hospitalNumberMax"), listHospitalNumberMax.toString());
        if (listHospitalNumberMax.size() == 0) {
            hospitalOperation.setHospitalNumber(hospitalOperation.getAreaCode()+value.getNumber());
        } else {
            //2.取出医院最大hospitalNumber加一
            String newHospitalNumber = new BigInteger(listHospitalNumberMax.get(0).getHospitalNumber()).add(new BigInteger("1")).toString();
            hospitalOperation.setHospitalNumber(newHospitalNumber);
        }
        //检查云平台数据库是否存在重复的HospitalNumber和hospitalName
        HospitalOperation nameResult = hospitalOperationFacade.findByName(hospitalOperation.getHospitalName());
        HospitalOperation hospitalNumberResult = hospitalOperationFacade.findByHosptitalNumber(hospitalOperation.getHospitalNumber());
        if(nameResult!=null || hospitalNumberResult!=null){
            JsfUtil.addErrorMessage("已经有相同的医院编码或医院名称!");
            log.warn("已经有相同的医院编码或医院名称!");
            return;
        }
        //GWTRPC远程调用数据平台，添加医院到数据平台
        HospitalInfo hospitalInfo = addHospitalValidation(hospitalOperation);
        log.debug("创建医院完毕 hospitalKey:"+hospitalInfo.getHospitalName()+" HKey:"+hospitalInfo.getHKey());
        //3.调用数据平台新增接口，将医院的的基本信息传入该接口，如果返回值为true，调用super.saveNew(event)在云平台新增
        if (hospitalInfo != null) {
            hospitalOperation.setHospitalKey(hospitalInfo.getHKey());
        }

            //无重名的医院，执行以下逻辑：
            //创建存放配置文件的文件夹
            boolean isCreateMac = MacDir.generateDir(hospitalOperation.getProcessorMac());
            if (isCreateMac) {
                log.info(ResourceBundle.getBundle("/MyBundle").getString("createMacDir"), isCreateMac);
            } else {
                log.info(ResourceBundle.getBundle("/MyBundle").getString("createMacDir"), isCreateMac);
            }
            try {
                //config_his.properties文件，前置机系统配置文件
                File propertiesFile = PropertiesFile.createRinted(hospitalOperation.getProcessorMac(),
                        hospitalOperation.getHospitalKey(), hospitalOperation.getHospitalNumber());
                log.info(ResourceBundle.getBundle("/MyBundle").getString("propertiesFilePath"), propertiesFile.getAbsolutePath());
                //5.调用方法新增openvvpn用户，成功执行生成shell的脚本生成
                //生成openvpn配置： tiger.ovpn
              //  File tigerFile = OpenVpnFile.createFile(hospitalOperation.getProcessorMac(), hospitalOperation.getHospitalNumber());
               // log.info(tigerFile.getAbsolutePath());
                //根据配置文件，是否生成openvpn证书
                if (value.getIsGenerateVPNCert().equals("true")) {
                    //调用远程接口，生成医院对应的数字证书；创建openvpn的用户名和密码
                    String vpnIp = OpenVpnFile.executeOpenVpnFile(hospitalOperation.getProcessorMac(),
                            hospitalOperation.getHospitalNumber());
                    if (Verify.isIP(vpnIp)){
                        //存储openvpn分配的静态IP地址
                        hospitalOperation.setVpnIP(vpnIp);
                    }else if(vpnIp==null){
                        JsfUtil.addErrorMessage("生成vpn出错");
                        log.warn("生成vpn出错");
                        return;
                    }else{
                        JsfUtil.addErrorMessage("错误：医院编码"+hospitalOperation.getHospitalNumber()+"已在OpenVpn服务器注册过，不可重复创建！");
                        log.warn("错误：医院编码"+hospitalOperation.getHospitalNumber()+"已在OpenVpn服务器注册过，不可重复创建！");
                        return;
                    }
                }
                //存储医院信息到数据库
                super.getFacade().create(hospitalOperation);
                //log.info(ResourceBundle.getBundle("/MyBundle").getString("tigerOvpnFilePath"), tigerFile.getAbsolutePath());

            } catch (IOException e) {
                //
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("ioError"));
                log.error(ResourceBundle.getBundle("/MyBundle").getString("ioError"), e);
            } catch (TemplateException e) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("TemplateError"));
                log.error(ResourceBundle.getBundle("/MyBundle").getString("TemplateError"), e);
            } catch (InterruptedException e) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("openVpnError"));
                log.error(ResourceBundle.getBundle("/MyBundle").getString("openVpnError"), e);
            }

        //HospitalInfo hospitalInfo = addHospitalValidation(hospitalOperation);
        //3.调用数据平台新增接口，将医院的的基本信息传入该接口，如果返回值为true，调用super.saveNew(event)在云平台新增
        //4.创建一个以mac地址命名的文件夹


    }


    /**
     * 检查mac日志文件
     * 并将日志内容赋值给存放日志信息的map
     *
     * @param mac mac日志文件
     */
    public void checkHis(String mac) {
        try {
            setHisString(HisLog.checkHisFile(mac));

        } catch (IOException e) {
            //没有日志文件的map
            StringBuffer stringHis = new StringBuffer();
            stringHis.append("false");
            stringHis.append(ResourceBundle.getBundle("/MyBundle").getString("hisUploadNot"));
            setHisString(stringHis.toString());
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("hisNot"));
            log.error(ResourceBundle.getBundle("/MyBundle").getString("hisNot"), e);
        }
    }


    /**
     * 打包时先删除原有的配置文件
     * 生成网络配置文件和在开机自启文件追加路由
     * 调用util方法将配置文件打成zip包
     *
     * @param hospitalOperation 对hospitalOperation医院相关的文件夹进行打包
     */
    public void getZip(HospitalOperation hospitalOperation) {
        List<ProcessorRoute> listProcessorRoute = processorRouteFacade.findProcessorRoute(hospitalOperation);
        log.info(listProcessorRoute.toString());
        //生成新的开机自启文件
        try {
            RcLocalFile.createRclocal(hospitalOperation.getProcessorMac());
        } catch (IOException e) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("fileError"));
            log.error(ResourceBundle.getBundle("/MyBundle").getString("fileError"), e);
        } catch (TemplateException e) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("TemplateError"));
            log.error(ResourceBundle.getBundle("/MyBundle").getString("TemplateError"), e);
        }
        //开关，false不打包
        boolean flag = true;

        for (int i = 0; i < listProcessorRoute.size(); i++) {
            try {
                //向开机自启文件添加路由
                File fileRclocal = RcLocalFile.createRoute(listProcessorRoute.get(i), hospitalOperation.getProcessorMac());
                log.info(ResourceBundle.getBundle("/MyBundle").getString("rcLocalFilePath"), fileRclocal.getAbsolutePath());
            } catch (IOException e) {
                flag = false;
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("fileError"));
                log.error(ResourceBundle.getBundle("/MyBundle").getString("fileError"), e);
            }
        }
        List<ProcessorConfig> listProcessorConfig = processorConfigFacade.findConfig(hospitalOperation);
        log.info(listProcessorConfig.toString());
        try {
            OpenVpnFile.createFile(hospitalOperation.getProcessorMac(),hospitalOperation.getHospitalNumber());
        } catch (IOException e) {
            flag = false;
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("ioError"));
            log.error(ResourceBundle.getBundle("/MyBundle").getString("ioError"), e);
        } catch (TemplateException e) {
            flag = false;
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("openVpnError"));
            log.error(ResourceBundle.getBundle("/MyBundle").getString("openVpnError"), e);
        }
        try {
            //生成网络配置文件
            NetworkFile.generateNetWorkFile(listProcessorConfig, hospitalOperation.getProcessorMac());
            File fileNew = new File(value.getFilePath() + hospitalOperation.getProcessorMac() + "/" + value.getInterfacesName());
            log.info(ResourceBundle.getBundle("/MyBundle").getString("networkFilePath"), fileNew.getAbsolutePath());
            boolean verify = VerifyFile.verifyNetworkCard(fileNew);
            if (!verify) {
                flag = false;
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("netWork"));
                log.warn(ResourceBundle.getBundle("/MyBundle").getString("netWork"));
            }
        } catch (IOException e) {
            flag = false;
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("fileError"));
            log.error(ResourceBundle.getBundle("/MyBundle").getString("fileError"), e);
        } catch (TemplateException e) {
            flag = false;
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("TemplateError"));
            log.error(ResourceBundle.getBundle("/MyBundle").getString("fileError"), e);
        }
        //生成端口映射文件
        List<ProcessorMap> listProcessorMap = processorMapFacade.findProcessorMap(hospitalOperation);
        for (int i = 0; i < listProcessorMap.size(); i++) {
            try {
                File file = RintedFile.createRinted(listProcessorMap.get(i), hospitalOperation.getProcessorMac());
                log.info(ResourceBundle.getBundle("/MyBundle").getString("rinetdFilePath"), file.getAbsolutePath());
            } catch (IOException e) {
                flag = false;
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("fileError"));
                log.error(ResourceBundle.getBundle("/MyBundle").getString("fileError"), e);
            } catch (TemplateException e) {
                flag = false;
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("TemplateError"));
                log.error(ResourceBundle.getBundle("/MyBundle").getString("fileError"), e);
            }
        }
        if (flag) {
            try {
                List<String> fileName = VerifyFile.verifyZipFile(hospitalOperation.getProcessorMac(), hospitalOperation.getHospitalNumber());
                if (fileName.size() == 0) {
                    boolean zip = FileToZip.fileToZip(value.getFilePath() + hospitalOperation.getProcessorMac(), value.getZipPath(), hospitalOperation.getProcessorMac());
                    log.info(ResourceBundle.getBundle("/MyBundle").getString("zip"), zip);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/MyBundle").getString("zipStatus"));
                    File zipFile=new File(value.getZipPath()+hospitalOperation.getProcessorMac()+".zip");
                    new Md5Util().getFileMD5(zipFile,hospitalOperation.getProcessorMac());
                } else {
                    String errorMessges = ResourceBundle.getBundle("/MyBundle").getString("notAllFile");
                    JsfUtil.addErrorMessage(errorMessges + fileName.toString());
                    log.warn(ResourceBundle.getBundle("/MyBundle").getString("notAllFile"));
                }
            } catch (IOException e) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("FileToZipError"));

            } catch (NoSuchAlgorithmException e) {
                log.error(ResourceBundle.getBundle("/MyBundle").getString("md5Error"), e);
            }
        } else {
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/MyBundle").getString("failToZip"));
        }
    }

    /**
     * 查询医院列表
     *
     * @param name   传入的查询值
     * @param column 通过数据库那个字段查询
     * @return 存放医院的信息的列表
     */
    private List<HospitalInfo> getHospitalList(String name, String column) {
        HospitalInfoService hospitalInfoService = getRemotingHelper().newRemoteInstance(
                HospitalInfoService.class, "hospitalInfo");
        QueryImpl query = new QueryImpl();
        query.setSortingField("-" + value.getDataHospitalNumberColumn());
        query.setEndRow(1);
        GWTCriterion gwtCriterion = new GWTCriterion(
                GWTCriterionType.begins, column, name);
        query.setGWTCriterion(gwtCriterion);
        PaginationSupport<HospitalInfo> hospitalInfoPaginationSupport = hospitalInfoService.fetch(query);
        return hospitalInfoPaginationSupport.getResults();
    }

    /**
     * 查询医院列表
     *
     * @param name   传入的查询值
     * @param column 通过数据库那个字段查询
     * @return 存放医院的信息的列表
     */
    private List<HospitalInfo> getHospitalName(String name, String column) {
        HospitalInfoService hospitalInfoService = getRemotingHelper().newRemoteInstance(
                HospitalInfoService.class, "hospitalInfo");
        QueryImpl query = new QueryImpl();
        query.setSortingField("-" + value.getDataHospitalNumberColumn());
        query.setEndRow(1);
        GWTCriterion gwtCriterion = new GWTCriterion(
                GWTCriterionType.eq, column, name);
        query.setGWTCriterion(gwtCriterion);
        PaginationSupport<HospitalInfo> hospitalInfoPaginationSupport = hospitalInfoService.fetch(query);
        return hospitalInfoPaginationSupport.getResults();
    }

    /**
     * 新增实施医院
     *
     * @param hospitalOperation 实施医院的信息
     * @return 新增医院的HospitalInfo对象
     */
    private HospitalInfo addHospital(HospitalOperation hospitalOperation) {
        HospitalInfoService hospitalInfoService = getRemotingHelper().newRemoteInstance(
                HospitalInfoService.class, "hospitalInfo");
        HospitalInfo hospitalInfo = new HospitalInfo();
        hospitalInfo.setHospitalGrade(hospitalOperation.getHospitalGrade());
        hospitalInfo.setHospitalName(hospitalOperation.getHospitalName());
        hospitalInfo.setAreacode(hospitalOperation.getAreaCode());
        hospitalInfo.setHospitalNumber(hospitalOperation.getHospitalNumber());
        hospitalInfo.setRecordCreator(value.getCreateUser());
        return hospitalInfoService.add(hospitalInfo);
    }

    /**
     * 检查医院名字是否重复 不重复新增医院
     *
     * @param hospitalOperation 实施对象
     */
    private HospitalInfo addHospitalValidation(HospitalOperation hospitalOperation) {
        HospitalInfo hospitalInfo = null;
        //精确查询是否存在同名的医院
        List<HospitalInfo> listHospitalInfo = getHospitalName(hospitalOperation.getHospitalName(), value.getDataHospitalNameColumn());
        //通过名字医院查询医院是否为空
        if (listHospitalInfo.size() > 0) {
            //判断医院名称是否重复
                if(listHospitalInfo.get(0).getHospitalName().equals(hospitalOperation.getHospitalName())){
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("nameError").toString());
                    log.error(ResourceBundle.getBundle("/MyBundle").getString("HospitalNameRepeated"), listHospitalInfo.toString());
                    return null;
                }
        } else {
            //  不重复新增
            try {
                hospitalInfo = addHospital(hospitalOperation);
                log.info(ResourceBundle.getBundle("/MyBundle").getString("hospitalInfo"), hospitalInfo.toString());
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e.toString());
                log.error(ResourceBundle.getBundle("/MyBundle").getString("addHospitalError").toString(), e);
            }
        }
        return hospitalInfo;
    }
}