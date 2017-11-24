/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.controller;

import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.entity.ProcessorConfig;
import com.gsafer.smartoperation.util.Verify;
import lombok.extern.slf4j.Slf4j;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ResourceBundle;

@Named(value = "processorConfigController")
@ViewScoped
@Slf4j
/**
 *
 * 网络信息的controller
 * update by ethan on 2017/8/1.
 */
public class ProcessorConfigController extends AbstractController<ProcessorConfig> {
    @Inject
    private HospitalOperationController hospitalOperationController;


    public ProcessorConfigController() {
        // Inform the Abstract parent controller of the concrete ProcessorConfig Entity
        super(ProcessorConfig.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        hospitalOperationController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the HospitalOperation controller in
     * order to display its data in its View dialog.
     * @param event Event object for the widget that triggered an action
     */
    public void prepareHospitalOperation(ActionEvent event) {
        ProcessorConfig selected = this.getSelected();
        if (selected != null && hospitalOperationController.getSelected() == null) {
            hospitalOperationController.setSelected(
                    selected.getHospitalOperation());
        }
    }
    @Override
    /**
     * update by ethan on 2017/8/1.
     * 生成对应的网络配置文件，并校验网络配置文件
     * 同时在数据库新增网络信息
     * @event 当前进行的事件
     */
    public void saveNew(ActionEvent event) {
        ProcessorConfig processorConfig = getSelected();
        if ("".equals(processorConfig.getGateway())||Verify.isIP(processorConfig.getGateway())){
            super.saveNew(event);
        }else{
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/MyBundle").getString("ipFormat"));
            log.warn(ResourceBundle.getBundle("/MyBundle").getString("ipFormat"));
        }
    }
}
