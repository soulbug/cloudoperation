/*
 * Copyright @ 2017 互联极简网络科技成都有限公司.
 * All right reserved.
 */
package com.gsafer.smartoperation.controller;

import com.gsafer.smartoperation.entity.ProcessorRoute;
import lombok.extern.slf4j.Slf4j;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "processorRouteController")
@ViewScoped
@Slf4j
/**
 * 路由信息的controller
 * update by ethan on 2017/8/1.
 */
public class ProcessorRouteController extends AbstractController<ProcessorRoute> {

    @Inject
    private HospitalOperationController hospitalOperationController;


    public ProcessorRouteController() {
        // Inform the Abstract parent controller of the concrete ProcessorRoute Entity
        super(ProcessorRoute.class);
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
        ProcessorRoute selected = this.getSelected();
        if (selected != null && hospitalOperationController.getSelected() == null) {
            hospitalOperationController.setSelected(
                    selected.getHospitalOperation());
        }
    }

    @Override
    /**
     * update by ethan on 2017/8/1.
     * 生成开机自启文件，并添加路由
     * 在数据库新增路由信息
     * @event 当前事件
     */
    public void saveNew(ActionEvent event) {
       // ProcessorRoute processorRoute=getSelected();
        super.saveNew(event);
    //  List<ProcessorRoute> listProcessorRoute=processorRouteFacade.findProcessorRoute(getSelected().getHospitalOperation());


    }
}
