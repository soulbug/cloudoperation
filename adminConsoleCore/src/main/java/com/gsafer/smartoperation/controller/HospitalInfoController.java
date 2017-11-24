package com.gsafer.smartoperation.controller;


import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.entity.HospitalOperation;
import com.gsafer.smartoperation.facade.HospitalOperationFacade;
import com.gsafer.smartoperation.facade.LazyEntityDataModel;
import com.gsafer.smartoperation.util.Value;
import com.gsafer.smartoperation.facade.HospitalLazyModel;
import com.gsafer.smartoperation.util.RouteGwtRpc;
import com.gsafer.smartoperation.util.UrlUtil;
import com.gsafer.smartoperation.util.Values;
import com.jckj.cis.client.hospitalinfo.service.HospitalInfoService;
import com.jckj.cis.entity.HospitalInfo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * date 2017/10/18
 *
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用   操作医院信息
 */
@Named(value = "hospitalInfoController")
@ViewScoped
@Slf4j
public class HospitalInfoController extends AbstractController<HospitalOperation> {
    public HospitalInfoController() {
        super(HospitalOperation.class);
    }

    @Inject
    private HospitalOperationFacade hospitalOperationFacade;
    @Inject
    private transient Value value;
    @Inject
    private transient Values values;
    /**
     * 判断是新增还是更新
     **/
    private boolean isNull = false;

    /**
     * 存放医院信息的懒加载的model
     */
    private LazyEntityDataModel<HospitalOperation> dataModel;
    /**
     * 旧的医院名字
     */
    private String hospitalName;
    /**
     * 医院信息对象
     */
    private HospitalInfo hospitalInfoCis;

    private String getHospitalName() {
        return hospitalName;
    }

    private void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public com.jckj.cis.entity.HospitalInfo getHospitalInfoCis() {

        return hospitalInfoCis;
    }

    public LazyEntityDataModel<HospitalOperation> getDataModel() {
        return dataModel;
    }

    public void setDataModel(LazyEntityDataModel<HospitalOperation> dataModel) {
        this.dataModel = dataModel;
    }

    public void setHospitalInfoCis(com.jckj.cis.entity.HospitalInfo hospitalInfoCis) {
        this.hospitalInfoCis = hospitalInfoCis;
    }

    @PostConstruct
    public void init() {
        dataModel = new HospitalLazyModel(hospitalOperationFacade);
    }

    /**
     * 对医院信息进行新增或者修改根据属性 isNull判断
     * 特殊处理：在修改医院名字时 要传入旧的医院的值
     */
    public void edit() {
        HospitalInfoService hospitalInfoService;
        HospitalInfo hospitalInfo;
        try {

            hospitalInfoService = new RouteGwtRpc<HospitalInfoService>(new UrlUtil().getUrl())
                    .getService(HospitalInfoService.class, "hospitalInfo");
            if (!isNull) {
                hospitalInfo = hospitalInfoService.add(hospitalInfoCis);
                log.info(values.getValue("addhospitalMessage"),hospitalInfo);
                JsfUtil.addSuccessMessage(values.getValue("generateSuccessful"));
            } else {

                if (hospitalInfoCis.getHospitalName().equals(getHospitalName())) {
                    //不修改医院名字
                    hospitalInfoCis.setHospitalName(null);
                    hospitalInfo = hospitalInfoService.update(hospitalInfoCis);
                    JsfUtil.addSuccessMessage(values.getValue("updateSuccessful"));
                } else {
                    //修改医院名字
                    Map<String, Serializable> map = new HashMap<>(1);
                    map.put("hospitalName", getHospitalName());
                    hospitalInfoCis.setOldValues(map);
                    hospitalInfo = hospitalInfoService.update(hospitalInfoCis);
                    JsfUtil.addSuccessMessage(values.getValue("updateSuccessful"));
                }
                log.info(values.getValue("updateHospitalMessages"),hospitalInfo);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e,values.getValue("optionError"));
            log.warn(values.getValue("optionError"), e);
        }

    }

    /**
     * 通过数据库里的医院信息查询数据平台医院信息
     * 不为空设置isNull为true
     */
    public void selectByHospitalNumber() {

            try {
                HospitalInfoService hospitalInfoService =
                        new RouteGwtRpc<HospitalInfoService>(new UrlUtil().getUrl())
                                .getService(HospitalInfoService.class, "hospitalInfo");
                HospitalInfo hospitalInfo = hospitalInfoService.getHisCongfigByHospitalNumber(getSelected().getHospitalNumber());
                //在数据平台上面没有医院信息时将数据库医院信息添加到页面上
                if (hospitalInfo == null) {
                    hospitalInfo = new HospitalInfo();
                    hospitalInfo.setHospitalNumber(getSelected().getHospitalNumber());
                    hospitalInfo.setAreacode(getSelected().getAreaCode());
                    hospitalInfo.setHospitalName(getSelected().getHospitalName());
                    hospitalInfo.setHospitalGrade(getSelected().getHospitalGrade());
                    hospitalInfo.setHKey(getSelected().getHospitalKey());
                    hospitalInfo.setRecordCreateTime(getSelected().getCreateTime());
                    hospitalInfo.setRecordCreator(value.getCreateUser());
                    setHospitalInfoCis(hospitalInfo);
                } else {
                    if (hospitalInfo.getHospitalNumber().equals(getSelected().getHospitalNumber())) {
                        setHospitalName(hospitalInfo.getHospitalName());
                        setHospitalInfoCis(hospitalInfo);
                        isNull = true;
                    } else {

                        log.info(values.getValue("platformNumber"), hospitalInfo.getHospitalNumber());
                        log.info(values.getValue("DBNumber"), getSelected().getHospitalNumber());
                        log.warn(values.getValue("hosNumberDiff"),values.getValue("hosNumberDiff"));
                        //抛出错误信息
                        JsfUtil.addErrorMessage(values.getValue("hosNumberDiff"));
                    }
                }
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e,values.getValue("selectIsError"));
                log.warn(values.getValue("selectIsError"), e);
            }
    }

    /**
     * 进行逻辑删除
     *
     * @param event an event from the widget that wants to delete an Entity from
     */
    @Override
    public void delete(ActionEvent event) {
        HospitalOperation hospitalOperation = getSelected();
        hospitalOperation.setCategory((short) 0);
        hospitalOperationFacade.delete(hospitalOperation);
    }
}
