package com.gsafer.smartoperation.controller;

import com.gsafer.smartoperation.entity.HospitalOperation;
import com.gsafer.smartoperation.entity.NoTreatmentMedicalInfo;
import com.gsafer.smartoperation.facade.HospitalOperationFacade;
import com.gsafer.smartoperation.util.UrlUtil;
import com.gsafer.smartoperation.util.Value;
import com.gsafer.smartoperation.util.RouteGwtRpc;
import com.gsafer.smartoperation.util.Values;
import com.jckj.cis.client.medicalNoTreatmentHospital.service.MedicalNoTreatmentHospitalService;
import com.jckj.cis.entity.MedicalNoTreatmentHospital;
import lombok.extern.slf4j.Slf4j;
import net.creative.tools.gwt.client.PaginationSupport;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterion;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterionType;
import net.creative.tools.gwt.client.datasource.sqlcriteria.QueryImpl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Created by vince on 2017/10/27.
 * 未对码医疗目录定时获取
 * @version 0.1
 * @do
*/

@Slf4j
@Named(value = "noTreatmentScheduleController")
@Singleton
public class NoTreatmentScheduleController implements Serializable {

    public NoTreatmentScheduleController() {

    }

    @Inject
    private transient Values values;

    public List<NoTreatmentMedicalInfo> getNoTreatmentMedicalInfos() {
        return noTreatmentMedicalInfos;
    }


    public void setNoTreatmentMedicalInfos(List<NoTreatmentMedicalInfo> noTreatmentMedicalInfos) {
        this.noTreatmentMedicalInfos = noTreatmentMedicalInfos;
    }

    //有未对码医疗目录信息的实体类
    private List<NoTreatmentMedicalInfo> noTreatmentMedicalInfos;

    @Inject
    private HospitalOperationFacade hospitalOperationFacade;

    @Schedule(second = "0", minute = "0", hour = "0", persistent = false) //每天凌晨时执行
    //@Schedule(second = "30", minute = "*", hour = "*", persistent = true)//每隔60秒执行
    public void doWork() {
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        System.out.println("ScheduleExample.doWork() invoked at " + simpleDateFormat.format(currentTime));

        List<HospitalOperation> hospitalOperations = hospitalOperationFacade.getAllNewHospital();
        List<NoTreatmentMedicalInfo> medicalInfos = new ArrayList<>();
        try {
            //要调用的未对码service
            MedicalNoTreatmentHospitalService medicalNoTreatmentHospitalService;
            //遍历。一个医院去查询一次
            final QueryImpl query = new QueryImpl();
            final String hospitalNumberColumnName = new Value().getGwtrpcPropertiesHospitalNumber();
            for (int i = 0; i < hospitalOperations.size(); i++) {
                //创建连接
                medicalNoTreatmentHospitalService = new RouteGwtRpc<MedicalNoTreatmentHospitalService>(
                        new UrlUtil().getUrl()).getService(MedicalNoTreatmentHospitalService.class, "medicalNoTreatmentHospital");
                //构造查询
                final GWTCriterion gwtCriterion = new GWTCriterion(
                        GWTCriterionType.eq, hospitalNumberColumnName, hospitalOperations.get(i).getHospitalNumber());
                query.setGWTCriterion(gwtCriterion);
                //执行查询
                final PaginationSupport<MedicalNoTreatmentHospital> medicalNoTreatmentHospitalPaginationSupport = medicalNoTreatmentHospitalService.fetch(query);
                //将获取到的值放进list导出
                if (0 != medicalNoTreatmentHospitalPaginationSupport.getTotalRows()) {
                    NoTreatmentMedicalInfo noTreatmentMedicalInfo = new NoTreatmentMedicalInfo();
                    noTreatmentMedicalInfo.setHospitalNumber(hospitalOperations.get(i).getHospitalNumber());
                    noTreatmentMedicalInfo.setHospitalName(hospitalOperations.get(i).getHospitalName());
                    noTreatmentMedicalInfo.setSize((int) medicalNoTreatmentHospitalPaginationSupport.getTotalRows());
                    medicalInfos.add(noTreatmentMedicalInfo);
                }
            }
            setNoTreatmentMedicalInfos(medicalInfos);
        } catch (Exception e) {
            log.warn(values.getValue("extracteCodeError") + e);
        }
    }
}
