package com.gsafer.smartoperation.controller;

import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.entity.*;
import com.gsafer.smartoperation.entity.ManagementOrganizationEntity;
import com.gsafer.smartoperation.facade.DesignateHospitalFacade;
import com.gsafer.smartoperation.facade.HospitalOperationFacade;
import com.gsafer.smartoperation.facade.ManagementOrganizationFacade;


import com.gsafer.smartoperation.util.UrlUtil;
import com.gsafer.smartoperation.util.Value;
import com.gsafer.smartoperation.util.RouteGwtRpc;
import com.gsafer.smartoperation.util.Values;
import com.jckj.cis.client.designatehospital.service.DesignateHospitalService;
import com.jckj.cis.client.managementorganization.service.ManagementOrganizationService;
import com.jckj.cis.entity.*;
import com.jckj.cis.entity.HospitalInfo;
import lombok.extern.slf4j.Slf4j;
import net.creative.tools.gwt.client.PaginationSupport;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterion;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterionType;
import net.creative.tools.gwt.client.datasource.sqlcriteria.QueryImpl;


import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by phoebe on 2017/10/18.
 */
@Named(value = "managementOrganizationController")
@ViewScoped
@Slf4j
public class ManagementOrganizationController extends AbstractController<ManagementOrganizationEntity>
{
    @Inject
    private ManagementOrganizationFacade managementOrganizationFacade;
    @Inject
    DesignateHospitalFacade designateHospitalFacade;
    @Inject
    private HospitalOperationFacade hospitalOperationFacade;
    @Inject
    private transient Value value;
    @Inject
    private transient Values values;
    private List<HospitalOperation> canDesignateHospital;
    private HospitalInfo designateHospitalInfo;
    private List<HospitalOperation> alreadyDesignateHospital;
    private String payWay;

    //已定点医院信息


    public List<HospitalOperation> getAlreadyDesignateHospital ()
    {
        return alreadyDesignateHospital;
    }

    public void setAlreadyDesignateHospital (List<HospitalOperation> alreadyDesignateHospital)
    {
        this.alreadyDesignateHospital = alreadyDesignateHospital;
    }



    public List<HospitalOperation> getCanDesignateHospital ()
    {
        return canDesignateHospital;
    }

    public void setCanDesignateHospital (List<HospitalOperation> canDesignateHospital)
    {
        this.canDesignateHospital = canDesignateHospital;
    }

    public HospitalInfo getDesignateHospitalInfo ()
    {
        return designateHospitalInfo;
    }

    public void setDesignateHospitalInfo (HospitalInfo designateHospitalInfo)
    {
        this.designateHospitalInfo = designateHospitalInfo;
    }

    public String getPayWay ()
    {
        return payWay;
    }

    public void setPayWay (String payWay)
    {
        this.payWay = payWay;
    }


    public ManagementOrganizationController() {
    super(ManagementOrganizationEntity.class);
}

    @Override
    /**
     * 新增公司，检查输入的公司编码是否存在
     * create by Phoebe on 2017/10/20.
     * @param event 表示当前正在进行的事件
     */
    public void saveNew(ActionEvent event) {
        ManagementOrganizationEntity managementOrganizationEntity = getSelected();
        //1.通过输入的公司编码查询公司信息
        List<ManagementOrganizationEntity> managementOrgs = managementOrganizationFacade.getManagementOrgByCode (
                managementOrganizationEntity.getManagementOrganizationCode ());
       //2.判断输入的公司编码是否为空
        if (managementOrgs.size ()>0) {
            //提示公司编码重复
            JsfUtil.addErrorMessage(values.getValue("orgCodeError"));
            log.info(values.getValue("managementOrgCodeRepeated"), managementOrgs.toString());
        } else {
            //  3.在数据库新增公司
            try {
                //获取当前操作人员，并设置
                managementOrganizationEntity.setRecordCreator (value.getRecordCreator());
                //设置更新时间
                Date date = new Date();
                managementOrganizationEntity.setRecordCreateTime(new Timestamp (date.getTime()));
                log.info(values.getValue("managementOrganizationMessage"), managementOrganizationEntity.toString());
                super.getFacade().create (managementOrganizationEntity);
                JsfUtil.addSuccessMessage (values.getValue("addManagementOrgSuccess"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e.toString());
                log.warn(values.getValue("addManagementOrgError"), e);
            }
        }
    }

    @Override
    /**
     * 更新公司信息
     * 检查要更新的公司是否有定点医院，并向定点医院关联的前置机数据平台发送更新公司信息请求
     * create by Phoebe on 2017/10/23.
     * @param event 表示当前正在进行的事件
     */
    public void save(ActionEvent event) {
        ManagementOrganizationEntity managementOrganizationEntity = getSelected();
         //申明公司接口
        ManagementOrganizationService managementOrganizationService;
        log.info(values.getValue("managementOrganizationCode"), managementOrganizationEntity.getManagementOrganizationCode ());
        //  1.通过要更新的公司编码查询是否存在要转发的前置机
        try {
            //通过公司编码查询公司定点医院对应的前置机信息
            List<HospitalOperation> hospitalOperations = managementOrganizationFacade.getHospitalOperationByManagementOrganizationCode (
                    managementOrganizationEntity.getManagementOrganizationCode ());
            //通判断前要转发的前置机信息列表是否为空
            if (hospitalOperations != null && hospitalOperations.size ()>0) {
                //查询要更新的公司信息在前置机数据平台的ID
                final QueryImpl query = new QueryImpl ();
                //构造查询条件
                final GWTCriterion gwtCriterion = new GWTCriterion (
                        GWTCriterionType.eq,  new Value().getManagementOrgCode(), managementOrganizationEntity.getManagementOrganizationCode ());
                query.setGWTCriterion (gwtCriterion);
                HospitalOperation hospitalOperationError = null;
                for(int i=0;i<hospitalOperations.size();i++){
                    //创建远程调用接口
                    managementOrganizationService = new RouteGwtRpc<ManagementOrganizationService>(
                            new UrlUtil().getUrl()).getService(ManagementOrganizationService.class,"managementOrganization");
                    //  2.通过公司到指定前置机查询公司信息，得到公司ID
                    final PaginationSupport<com.jckj.cis.entity.ManagementOrganization> managementOrganizations = managementOrganizationService.fetch (
                            query);
                    //将指定数据平台的公司信息ID赋给要更新的公司信息实体类
                    com.jckj.cis.entity.ManagementOrganization managementOrg = new com.jckj.cis.entity.ManagementOrganization ();
                    //将修改后的值赋给managementOrg
                    managementOrg.setManagementOrganizationId (managementOrganizations.getResults ().get (0).getManagementOrganizationId ());
                    managementOrg = setManagementOrgEntityToManagementOrg(managementOrganizationEntity,managementOrg);
                    //3.向指定前置机数据平台发送更新公司信息请求
                    com.jckj.cis.entity.ManagementOrganization managementOrganizationResult = managementOrganizationService.update (managementOrg);
                    if(managementOrganizationResult == null){
                         hospitalOperationError = hospitalOperations.get (i);
                        break;
                    }
                }
                if(hospitalOperationError==null){
                    log.info(values.getValue("managementOrganizationMessage"), managementOrganizationEntity.toString());
                    //前置机数据平台更新公司信息成功，开始更新本地公司信息
                    super.getFacade().edit(managementOrganizationEntity);
                    JsfUtil.addSuccessMessage (values.getValue("editManagementOrgSuccess"));
                }else{
                    JsfUtil.addErrorMessage(values.getValue("editOperationManagementOrgError"));
                    //打印未更新成功的医院前置机信息
                    log.info(values.getValue("editOperationManagementOrgError"),hospitalOperationError.toString ());
                }
            }else{
                //直接更新公司信息
                super.getFacade().edit(managementOrganizationEntity);
                JsfUtil.addSuccessMessage (values.getValue("editManagementOrgSuccess"));
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.toString());
            log.warn(values.getValue("editManagementOrgError"), e);
        }
    }

    /**
     * 查询指定公司未对码医院列表
     * create by Phoebe on 2017/10/24.
     */
    public void getNoDesignateHospital()
    {
        ManagementOrganizationEntity managementOrganizationEntity = getSelected();
        try
        {
            //1.通过输入的公司编码查询未定点的医院列表
           setCanDesignateHospital (designateHospitalFacade.getNoDesignateHospital (managementOrganizationEntity.getManagementOrganizationCode ()));
        } catch (Exception e)
        {
            JsfUtil.addErrorMessage (e.toString ());
            log.warn (ResourceBundle.getBundle ("/MyBundle").getString (
                    "getNoDesignateHospitalError"), e);
        }
    }

    /**
     * 查询指定公司已对码对码医院列表
     * create by Phoebe on 2017/10/24.
     */
    public void getAlreadyDesognateHospital()
    {
        ManagementOrganizationEntity managementOrganizationEntity = getSelected();

        try
        {
            //1.通过输入的公司编码查询未定点的医院列表
            setAlreadyDesignateHospital (hospitalOperationFacade.getAlreadyDesognateHospital(managementOrganizationEntity.getManagementOrganizationCode ()));
        } catch (Exception e)
        {
            JsfUtil.addErrorMessage (e.toString ());
            log.warn (ResourceBundle.getBundle ("/MyBundle").getString (
                    "getNoDesignateHospitalError"), e);
        }
    }

    /**
     * 通过公司信息和医院信息构造定点信息
     * create by Phoebe on 2017/10/25.
     * @hospitalOperation hospitalInfo 医院编码
     */
    public void getDesignateHospital(HospitalOperation hospitalOperation)
    {
        //通过医院编码查询医院信息并设置到designateInfo中
        HospitalInfo  hospitalInfo = new HospitalInfo();
        hospitalInfo.setHospitalNumber (hospitalOperation.getHospitalNumber ());
        setDesignateHospitalInfo (hospitalInfo);
        log.info(values.getValue("designateHospitalInfoMessage"),getDesignateHospitalInfo ().toString ());
    }

    /**
     * 医院定点
     * 判断公司定点医院数量是否达到上限
     * 判断定点医院前置机数据平台是否存在该公司
     *先调接口新增医院信息到指定前置机数据平台
     * 新增成功后再调远程服务接口定点医院
     * create by Phoebe on 2017/10/25.
     * @param event 表示当前正在进行的事件
     */
    public void saveDesignateInfo(ActionEvent event) {
        if(!"".equals (getPayWay ()) && getPayWay ()!=null){
            //实列化远程接口定点信实体类
            DesignateHospital designateInfo = new DesignateHospital();
            //实列化远程接口公司信息实体类
            com.jckj.cis.entity.ManagementOrganization managementOrganization=new com.jckj.cis.entity.ManagementOrganization();
            //实列化本地定点信息实体类
            DesignateHospitalEntity designateHospitalEntity = new DesignateHospitalEntity();
            //获取当前公司信息
            ManagementOrganizationEntity organization=getSelected ();
			managementOrganization = setManagementOrgEntityToManagementOrg(organization,managementOrganization);
            //接收页面传来的理赔金支付方式，并设置到DesignateInfo中
            designateInfo.setPayWay (getPayWay ());
            //设置定点标识（“未定点”）
            designateInfo.setDesignateHospitalFlag (new
                    Value().getNoDesignateCode ());
            //设置记录创建人
            designateInfo.setRecordCreator (new
                    Value().getRecordCreator ());
            //设置公司信息
            designateInfo.setManagementOrganization (managementOrganization);
            designateInfo.setManagementOrganizationCode (managementOrganization.getManagementOrganizationCode ());
            //设置医院信息
            designateInfo.setHospitalInfo (getDesignateHospitalInfo ());
            designateInfo.setHospitalNumber (getDesignateHospitalInfo ().getHospitalNumber ());
            //设置信息到本地定点信息实体类
            designateHospitalEntity.setHospitalNumber (designateInfo.getHospitalNumber ());
            designateHospitalEntity.setPayWay (getPayWay ());
            designateHospitalEntity.setRecordCreator (new
                    Value().getRecordCreator ());
            designateHospitalEntity.setManagementOrganizationCode (designateInfo.getManagementOrganizationCode ());
            Date date = new Date();
            designateHospitalEntity.setRecordCreateTime (new Timestamp (date.getTime()));
            try{
                int designateCount = designateHospitalFacade.getDesignateHospitalList(organization.getManagementOrganizationCode ()).size();
                //1.比较已定点医院数量和可定点医院数量
                if(organization.getMaxHospitalTotal () > designateCount){
                    //通过医院编码查询医院信息
                    List<HospitalOperation> hospitalOperations = managementOrganizationFacade.getHospitalOperationByNumber(getDesignateHospitalInfo ().getHospitalNumber ());
                    if (hospitalOperations != null && hospitalOperations.size ()>0) {
                        //创建远程调用接口
                        ManagementOrganizationService managementOrganizationService = new RouteGwtRpc<ManagementOrganizationService> (
                                new UrlUtil().getUrl()).getService(ManagementOrganizationService.class,"managementOrganization");
                        DesignateHospitalService designateHospitalService = new RouteGwtRpc<DesignateHospitalService> (
                                new UrlUtil().getUrl()).getService(DesignateHospitalService.class,"designateHospital");
                        final QueryImpl query = new QueryImpl ();
                        //构造查询条件
                        final GWTCriterion gwtCriterion = new GWTCriterion (
                                GWTCriterionType.eq, new
                                Value().getManagementOrgCode(),
                                organization.getManagementOrganizationCode ());
                        query.setGWTCriterion (gwtCriterion);
                        //2.通过公司编码查询到指定前置机查询公司信息
                        final PaginationSupport<com.jckj.cis.entity.ManagementOrganization> managementOrganizations = managementOrganizationService.fetch (
                                query);
                        //3.判断公司在指定前置机数据平台是否存在
                        if (managementOrganizations.getResults () != null && managementOrganizations.getResults ().size ()>0) {
                            //若公司在指定数据平台存在，则直接调用远程服务定点医院
                            com.jckj.cis.entity.DesignateHospital designateHospitalResult = designateHospitalService.add (designateInfo);
                            //判断远程接口是否定点成功，成功则在本地进行定点
                            if(designateHospitalResult != null){
                                designateHospitalFacade.create (designateHospitalEntity);
                                JsfUtil.addSuccessMessage (values.getValue("designateHospitalSuccess"));
                            }else{
                                //提示远程定点失败
                                JsfUtil.addErrorMessage(values.getValue("designateHospitalError"));
                                log.info(values.getValue("designateHospitalError"),designateInfo.toString ());
                            }
                        }else{
                            //公司不存在则先新增保险公司
                            com.jckj.cis.entity.ManagementOrganization managementOrganizationResult = managementOrganizationService.add (managementOrganization);
                            //判断是否新增成功
                            if(managementOrganizationResult !=null){
                                //新增成功则开始调远程接口定点医院
                                com.jckj.cis.entity.DesignateHospital designateHospitalResult = designateHospitalService.add (designateInfo);
                                //判断远程接口是否定点成功，成功则在本地进行定点
                                if(designateHospitalResult != null){
                                    designateHospitalFacade.create (designateHospitalEntity);
                                    JsfUtil.addSuccessMessage (values.getValue("designateHospitalSuccess"));
                                }else{
                                    //提示远程定点失败
                                    JsfUtil.addErrorMessage(values.getValue("designateHospitalError"));
                                    log.info(values.getValue("designateHospitalError"),designateInfo.toString ());
                                }
                            }else{
                                JsfUtil.addErrorMessage(values.getValue("designateHospitalCreateManagementOrgError"));
                                //新增保险公司失败
                                log.info(values.getValue("designateHospitalCreateManagementOrgError"),managementOrganization.toString ());
                            }
                        }
                    }
                }else{
                    //如果已经达到可定点最大数量，提示可定点医院数量达到上限
                    log.info(values.getValue("canDesignateLimit"),managementOrganization.toString ());
                    JsfUtil.addErrorMessage(values.getValue("canDesignateLimit"));
                }
            }catch (Exception e)
            {
                JsfUtil.addErrorMessage (e.toString ());
                log.warn (values.getValue("designateHospitalError"), e);
            }
        }else{
            JsfUtil.addErrorMessage(values.getValue("designateHospitalLabel_payWay"));
        }

    }

    /**
     * 将本地公司信息实体类的值设置到远程服务接口实体类
     * create by Phoebe on 2017/10/26.
     * @para hospitalInfo 医院编码
     */
    private ManagementOrganization setManagementOrgEntityToManagementOrg(ManagementOrganizationEntity managementOrganizationEntity,ManagementOrganization managementOrg)
    {
        managementOrg.setRecordCreateTime (
                managementOrganizationEntity.getRecordCreateTime ());
        managementOrg.setManagementOrganizationName (
                managementOrganizationEntity.getManagementOrganizationName ());
        managementOrg.setRecordCreator (
                managementOrganizationEntity.getRecordCreator ());
        managementOrg.setServerProt (managementOrganizationEntity.getServerProt ());
        managementOrg.setAddress (managementOrganizationEntity.getAddress ());
        managementOrg.setAnalogueCalculateFlag (
                managementOrganizationEntity.getAnalogueCalculateFlag ());
        managementOrg.setAreacode (managementOrganizationEntity.getAreaCode ());
        managementOrg.setCompensateOrder (
                managementOrganizationEntity.getCompensableOrder ());
        managementOrg.setTelephone (managementOrganizationEntity.getTelephone ());
        managementOrg.setRemark (managementOrganizationEntity.getRemark ());
        managementOrg.setStandbyServerProt (
                managementOrganizationEntity.getStandbyServerProt ());
        managementOrg.setStandbyServerIp (
                managementOrganizationEntity.getStandbyServerIp ());
        managementOrg.setMobilephone (managementOrganizationEntity.getMobilephone ());
        managementOrg.setLinkman (managementOrganizationEntity.getLinkman ());
        managementOrg.setMaxHospitalTotal (
                managementOrganizationEntity.getMaxHospitalTotal ());
        managementOrg.setManagementOrgShortName (
                managementOrganizationEntity.getManagementOrgShortname ());
        managementOrg.setConfirmCompensateFlag (
                managementOrganizationEntity.getConfirmCompensateFlag ());
        managementOrg.setManagementOrganizationCode (
                managementOrganizationEntity.getManagementOrganizationCode ());
        return managementOrg;
    }

    /**
     * 取消医院定点信息
     */

    public void delDesignateHospital(String hospitalNumber){
        //获取当前选择的医院
        ManagementOrganizationEntity organization=getSelected ();
        HospitalOperation hospitalOperation=hospitalOperationFacade.getHospitalOperationByHospitalNumber(hospitalNumber);
        DesignateHospitalService designateHospitalService = new RouteGwtRpc<DesignateHospitalService> (
                new UrlUtil().getUrl()).getService(DesignateHospitalService.class,"designateHospital");

        final QueryImpl query = new QueryImpl ();
        //构造查询条件
        final GWTCriterion gwtCriterion = new GWTCriterion (
                GWTCriterionType.and,new GWTCriterion(GWTCriterionType.eq,"Designate","Designate"),
                new GWTCriterion(GWTCriterionType.eq,new Value().getManagementOrgCode (),organization.getManagementOrganizationCode ()));
        query.setGWTCriterion (gwtCriterion);
        //传入定点实体类
        List<com.jckj.cis.entity.DesignateHospital> designateHospitals = designateHospitalService.fetch (query).getResults();
        boolean result=false;
        for(int i=0;i<designateHospitals.size();i++){
            if(hospitalNumber.equals(designateHospitals.get(i).getHospitalInfo().getHospitalNumber())){
                com.jckj.cis.entity.DesignateHospital currentDH=new com.jckj.cis.entity.DesignateHospital();
                com.jckj.cis.entity.HospitalInfo hospitalInfo=new com.jckj.cis.entity.HospitalInfo();
                com.jckj.cis.entity.ManagementOrganization managementOrganization=new com.jckj.cis.entity.ManagementOrganization();
                managementOrganization.setManagementOrganizationCode(organization.getManagementOrganizationCode());
                managementOrganization.setServerIp(organization.getServerIp());
                managementOrganization.setServerProt(organization.getServerProt());
                hospitalInfo.setHospitalNumber(hospitalNumber);
                currentDH=designateHospitals.get(i);
                currentDH.setHospitalInfo(hospitalInfo);
                currentDH.setManagementOrganization(managementOrganization);
                currentDH.setHospitalNumber(hospitalNumber);
                currentDH.setManagementOrganizationCode(organization.getManagementOrganizationCode());
                currentDH.setDesignateHospitalFlag (new
                        Value().getDesignateCode ());
                try{
                    designateHospitalService.remove (currentDH);
                    designateHospitalFacade.delDesignateHospital(hospitalNumber,managementOrganization.getManagementOrganizationCode());
                    result=true;
                }catch (Exception e){
                    log.warn (ResourceBundle.getBundle ("/MyBundle").getString (
                            "delDesignateHospitalError"), e);
                }
                break;
            }
        }
        if(result){
            JsfUtil.addSuccessMessage (values.getValue("delDesignateHospitalSuccess"));
        }else {
            log.info(values.getValue("delDesignateHospitalError"),hospitalOperation.toString ());
            JsfUtil.addErrorMessage(values.getValue("delDesignateHospitalError"));
        }
    }


}





