package com.gsafer.smartoperation.controller;

import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.facade.HisDataSourceLazyModel;
import com.gsafer.smartoperation.facade.HospitalOperationFacade;
import com.gsafer.smartoperation.util.RouteGwtRpc;
import com.gsafer.smartoperation.util.UrlUtil;
import com.gsafer.smartoperation.util.Values;
import com.jckj.cis.client.hospitalinfo.service.HisDataSourceService;
import com.jckj.cis.entity.HisDataSource;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * date 2017/10/23
 *
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用 医院his配置
 */
@Named(value = "hisDataSourceController")
@ViewScoped
@Slf4j
public class HisDataSourceController implements Serializable {

    public HisDataSourceController() {
    }

    @Inject
    private HospitalOperationFacade hospitalOperationFacade;
    @Inject
    private Values values;
    /**
     * 选中的对象
     */
    private HisDataSource selected;
    /**
     * 增加的医院his配置对象
     */
    private HisDataSource hisDataSourceAdd;

    /**
     * 医院编码
     */
    private String hospitalNumber;
    /**
     * 初始化页面加载lazyDataTable
     */
    private LazyDataModel<HisDataSource> lazyModel;

    public HisDataSource getSelected() {
        return selected;
    }

    public void setSelected(HisDataSource selected) {
        this.selected = selected;
    }

    public LazyDataModel<HisDataSource> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<HisDataSource> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public HisDataSource getHisDataSourceAdd() {

        return hisDataSourceAdd;
    }

    public void setHisDataSourceAdd(HisDataSource hisDataSourceAdd) {
        this.hisDataSourceAdd = hisDataSourceAdd;
    }

    private HisDataSourceService getHisDataSourceService() {
        return new RouteGwtRpc<HisDataSourceService>(new UrlUtil().getUrl())
                .getService(HisDataSourceService.class, "hisDataSource");
    }

    /**
     * 业务逻辑
     * 页面第一加载时得到传入的hospitalNumber
     * 初始化页面加载lazyDataTable
     */
    @PostConstruct
    public void init() {
        hisDataSourceAdd = new HisDataSource();
        if (!FacesContext.getCurrentInstance().isPostback()) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            setHospitalNumber(request.getParameter("hospitalNumber"));
            log.info(values.getValue("hospitalNumber"));
            HisDataSourceLazyModel dataSourceLazyModel = new HisDataSourceLazyModel(null);
            dataSourceLazyModel.setHospitalOperationFacade(hospitalOperationFacade);
            dataSourceLazyModel.setHospitalNumber(getHospitalNumber());
            this.lazyModel = dataSourceLazyModel;
        }
    }

    /**
     * 编辑医院his配置
     */
    public void edit() {
        HisDataSource hisDataSource;
        selected.setHospitalNumber(hospitalNumber);
        try {
            hisDataSource = getHisDataSourceService().update(selected);
            log.info(values.getValue("configInformation"), hisDataSource);
            JsfUtil.addSuccessMessage(values.getValue("updateSuccessful"));
        } catch (Exception e) {
            log.warn(values.getValue("configInformationError"), e);
            JsfUtil.addErrorMessage(e, values.getValue("updateFail"));
        }

    }

    /**
     * 增加医院his配置
     */
    public void add() {
        HisDataSource hisDataSource;
        hisDataSourceAdd.setHospitalNumber(hospitalNumber);
        try {
            hisDataSource = getHisDataSourceService().add(hisDataSourceAdd);
            log.info(values.getValue("configInformation"), hisDataSource);
            JsfUtil.addSuccessMessage(values.getValue("generateSuccessful"));
            //清空对象
            hisDataSourceAdd=new HisDataSource();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, values.getValue("addError"));
            log.warn(values.getValue("configInformationError"), e);
        }

    }
}
