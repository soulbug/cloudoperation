/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.controller;

import com.gsafer.smartoperation.entity.ProcessorMap;
import lombok.extern.slf4j.Slf4j;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "processorMapController")
@ViewScoped
@Slf4j
/**
 * 端口映射的controller
 * update by ethan on 2017/8/1.
 */
public class ProcessorMapController extends AbstractController<ProcessorMap> {

    @Inject
    private HospitalOperationController hospitalOperationController;

    public ProcessorMapController() {
        // Inform the Abstract parent controller of the concrete ProcessorMap Entity
        super(ProcessorMap.class);
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
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareHospitalOperation(ActionEvent event) {
        ProcessorMap selected = this.getSelected();
        if (selected != null && hospitalOperationController.getSelected() == null) {
            hospitalOperationController.setSelected(
                    selected.getHospitalOperation());
        }
    }

    @Override

    /**
     * update by ethan on 2017/8/1.
     * 创建端口映射文件 新增映射信息
     * 在数据库新增端口映射信息
     * @event 当前的进行的事件
     */
    public void saveNew(ActionEvent event) {
        super.saveNew(event);
    }
}
