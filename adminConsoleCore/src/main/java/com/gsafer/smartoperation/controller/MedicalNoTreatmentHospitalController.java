package com.gsafer.smartoperation.controller;

import com.alibaba.fastjson.JSON;
import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.entity.HospitalOperation;
import com.gsafer.smartoperation.entity.NoTreatmentMedicalInfo;
import com.gsafer.smartoperation.facade.HospitalOperationFacade;
import com.gsafer.smartoperation.util.*;
import com.jckj.cis.client.medicalNoTreatmentHospital.service.MedicalNoTreatmentHospitalService;
import com.jckj.cis.entity.MedicalNoTreatmentHospital;
import com.jckj.cis.entity.MedicalTreatmentHospital;
import com.jckj.cis.unit.Configuration;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import lombok.extern.slf4j.Slf4j;
import net.creative.tools.gwt.client.PaginationSupport;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterion;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterionType;
import net.creative.tools.gwt.client.datasource.sqlcriteria.QueryImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.util.*;
import com.gsafer.smartoperation.util.UrlUtil;

/**
 * date 2017/10/23
 *
 * @author vince
 * Copyright @ 互联极简
 * 未对码医疗目录导出，已对码医疗目录导入导出。
 */
@Named(value = "medicalNoTreatmentHospitalController")
@ViewScoped
@Slf4j
public class MedicalNoTreatmentHospitalController implements Serializable {

    public MedicalNoTreatmentHospitalController() {

    }

    /**
     * 国际化
     */
    @Inject
    private transient Values values;
    /**
     * 配置文件
     */
    @Inject
    private transient Value value;

    /**
     * 获取前置机信息Facade
     */
    @Inject
    private HospitalOperationFacade hospitalOperationFacade;

    public List<NoTreatmentMedicalInfo> getNoTreatmentMedicalInfos() {
        return noTreatmentMedicalInfos;
    }

    public void setNoTreatmentMedicalInfos(List<NoTreatmentMedicalInfo> noTreatmentMedicalInfos) {
        this.noTreatmentMedicalInfos = noTreatmentMedicalInfos;
    }

    //平台地址
    private String publicDataPlatformUrl;

    public String getPublicDataPlatformUrl() {
        return new UrlUtil().getPublicUrl();
    }

    public void setPublicDataPlatformUrl(String dataPlatformUrl) {
        this.publicDataPlatformUrl = dataPlatformUrl;
    }
    //未对码医疗目录信息
    private List<NoTreatmentMedicalInfo> noTreatmentMedicalInfos;

    /**
     * 未对码医疗目录导出
     */
    public void exportNotreatmentExcel() {
        //title信息还包括sheet的名称和文件名
        String title = new Value().getExpoerNotreatmentHospitalTitle();
        log.info(values.getValue("exportingMessage"));
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        //String[] rowsName = new String[]{"医院编码", "医疗项目编码", "项目名称", "类别", "规格", "项目单位", "剂型", "费用属性", "自费比例", "三方编码", "限价", "快捷码", "备注"};//标题栏
        String[] rowsName = new String[]{value.getHospitalNumber(), value.getHospitalListNumber(), value.getProjectName(),
                value.getSort(), value.getSpec(), value.getUnit(), value.getBetakeGenre(), value.getFeesSort(), value.getSelfPlay(),
                value.getThreeCode(), value.getLimitPrice(), value.getSwiftCode(), value.getRemark()};//标题栏
        List<Object[]> dataList = new ArrayList<Object[]>();
        //未对码医疗目录List
        List<MedicalNoTreatmentHospital> medicalNoTreatmentHospitals = new ArrayList<>();
        //所有新方式的前置机的信息
        List<HospitalOperation> hospitalOperations = hospitalOperationFacade.getAllNewHospital();
        try {
            //要调用的未对码service
            MedicalNoTreatmentHospitalService medicalNoTreatmentHospitalService;
            //遍历。一个医院去查询一次
            final QueryImpl query = new QueryImpl();
            final String hospitalNumberColumnName = new Value().getGwtrpcPropertiesHospitalNumber();
            for (int i = 0; i < hospitalOperations.size(); i++) {
                //创建连接
                medicalNoTreatmentHospitalService = new RouteGwtRpc<MedicalNoTreatmentHospitalService>(
                        new UrlUtil().getUrl() ).getService(MedicalNoTreatmentHospitalService.class, "medicalNoTreatmentHospital");
                //构造查询
                final GWTCriterion gwtCriterion = new GWTCriterion(
                        GWTCriterionType.eq, hospitalNumberColumnName, hospitalOperations.get(i).getHospitalNumber());
                query.setGWTCriterion(gwtCriterion);
                //执行查询
                final PaginationSupport<MedicalNoTreatmentHospital> medicalNoTreatmentHospitalPaginationSupport = medicalNoTreatmentHospitalService.fetch(query);
                //将获取到的值放进list导出
                if (0 != medicalNoTreatmentHospitalPaginationSupport.getTotalRows()) {
                    medicalNoTreatmentHospitals.addAll(medicalNoTreatmentHospitalPaginationSupport.getResults());
                }
            }

            log.info(JSON.toJSONString(medicalNoTreatmentHospitals));
            //临时存放每一行的表格信息
            Object[] objs = null;
            //将查询到的数据封装进表格中
            for (int i = 0; i < medicalNoTreatmentHospitals.size(); i++) {
                MedicalNoTreatmentHospital medicalNoTreatmentHospital = medicalNoTreatmentHospitals.get(i);
                objs = new Object[rowsName.length];
                objs[0] = medicalNoTreatmentHospital.getHospitalNumber();
                objs[1] = medicalNoTreatmentHospital.getHospitalListNumber();
                objs[2] = medicalNoTreatmentHospital.getHospitalListName();
                objs[3] = medicalNoTreatmentHospital.getSort();
                objs[4] = medicalNoTreatmentHospital.getSpec();
                objs[5] = medicalNoTreatmentHospital.getUnit();
                objs[6] = medicalNoTreatmentHospital.getBetakeGenre();
                objs[7] = medicalNoTreatmentHospital.getPaymentProperty();
                objs[8] = medicalNoTreatmentHospital.getLiableExpense() + "";
                objs[9] = medicalNoTreatmentHospital.getTripartiteNumber();
                objs[10] = medicalNoTreatmentHospital.getLimitedPrice() + "";
                objs[11] = "";
                objs[12] = "";
                dataList.add(objs);
            }
            ExportExcel ex = new ExportExcel(title, rowsName, dataList);

            HSSFWorkbook wb;
            //清空输出流
            response.reset();
            //定义输出类型
            response.setContentType("application/ms-excel");

            wb = ex.export();
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(title.getBytes("gb2312"), "ISO8859-1")
                    + ".xls");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            log.warn(values.getValue("exportNotMatchError"), e.toString());
            JsfUtil.addErrorMessage(e, values.getValue("exportNotMatchError"));
        }
        FacesContext.getCurrentInstance().responseComplete();
    }


    /**
     * 上传已对码医疗目录
     *
     * @param event 上传事件
     */
    public void handleFileUpload(FileUploadEvent event) {
        String resultMsg = "";
        String msgInfo = "";
        HashSet medicalTreadtmentKey = new HashSet();
        log.info(values.getValue("fileUpload") + event.getFile().getFileName());
        File file = new File(UUID.randomUUID().toString() + ".xls");

        try {
            file.createNewFile();

            InputStream excelIS = event.getFile().getInputstream();
            inputstreamtofile(excelIS, file);
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            if (log.isDebugEnabled()) {
                log.debug(values.getValue("startParsingExcel"));
            }

            //首先判断导出的Excel判断标题栏是否符合规范
            if (sheet.getCell(0, 0).getContents().equals(value.getHospitalNumber())
                    && sheet.getCell(1, 0).getContents().equals(value.getUnifyHospitalListNumber())
                    && sheet.getCell(2, 0).getContents().equals(value.getMedicalNumber())
                    && sheet.getCell(3, 0).getContents().equals(value.getProjectName())
                    && sheet.getCell(4, 0).getContents().equals(value.getSort())
                    && sheet.getCell(5, 0).getContents().equals(value.getSpec())
                    && sheet.getCell(6, 0).getContents().equals(value.getUnit())
                    && sheet.getCell(7, 0).getContents().equals(value.getBetakeGenre())
                    && sheet.getCell(8, 0).getContents().equals(value.getFeesSort())
                    && sheet.getCell(9, 0).getContents().equals(value.getSelfPlay())
                    && sheet.getCell(10, 0).getContents().equals(value.getThreeCode())
                    && sheet.getCell(11, 0).getContents().equals(value.getLimitPrice())
                    && sheet.getCell(12, 0).getContents().equals(value.getSwiftCode())) {
                if (log.isDebugEnabled()) {
                    log.debug(values.getValue("excelVerifySuccessfully"));
                }
                //获取sheet里面的行数
                int var32 = sheet.getRows();

                HashMap hospital = new HashMap();

                //错误的医疗目录列表
                ArrayList errorMedicalTreatmentHospitals = new ArrayList();

                //未对码医疗目录
                MedicalNoTreatmentHospital medicalNoTreatmentHospital = null;


                HashSet hospitalInfos = new HashSet();
                HashSet medicalTreatments = new HashSet();
                String hospitalNumber = "";

                //遍历。
                for (int iterator = 1; iterator < var32; ++iterator) {
                    Cell cell = sheet.getCell(0, iterator);
                    Object medicalTreatmentHospitals = (List) hospital.get(cell.getContents().trim());
                    hospitalInfos.add(cell.getContents().trim());
                    hospitalNumber = cell.getContents().trim();
                    //如果为空就记录
                    if (medicalTreatmentHospitals == null) {
                        medicalTreatmentHospitals = new ArrayList();
                        hospital.put(cell.getContents().trim(), medicalTreatmentHospitals);
                    }
                    //新增已对码对象
                    MedicalTreatmentHospital medicalTreatmentHospital = new MedicalTreatmentHospital();
                    //防止一行的数据，赋值进对象
                    medicalTreatmentHospital.setHospitalNumber(cell.getContents().trim());
                    cell = sheet.getCell(1, iterator);
                    medicalTreatmentHospital.setMedicalTreatmentNumber(cell.getContents().trim());
                    medicalTreatments.add(medicalTreatmentHospital.getMedicalTreatmentNumber());
                    cell = sheet.getCell(2, iterator);
                    medicalTreatmentHospital.setHospitalListNumber(cell.getContents().trim());
                    cell = sheet.getCell(3, iterator);
                    medicalTreatmentHospital.setHospitalListName(cell.getContents().trim());
                    cell = sheet.getCell(4, iterator);
                    medicalTreatmentHospital.setSort(cell.getContents().trim());
                    cell = sheet.getCell(5, iterator);
                    medicalTreatmentHospital.setSpec(cell.getContents().trim());
                    cell = sheet.getCell(6, iterator);
                    medicalTreatmentHospital.setUnit(cell.getContents().trim());
                    cell = sheet.getCell(7, iterator);
                    medicalTreatmentHospital.setBetakeGenre(cell.getContents().trim());
                    medicalTreatmentHospital.setUpdateTime(new Date());
                    cell = sheet.getCell(11, iterator);
                    medicalTreatmentHospital.setBetakeGenre(cell.getContents().trim());
                    medicalTreatmentHospital.setUnitPrice(Double.valueOf(0.0D));
                    if (this.checkNullValue(medicalTreatmentHospital) != null) {
                        log.warn(values.getValue("excelDataIsWrong") + medicalTreatmentHospital);
                    } else if (this.checkData(medicalTreatmentHospital) != null) {
                        log.warn(values.getValue("ExcelDataError") + medicalTreatmentHospital);
                        errorMedicalTreatmentHospitals.add(medicalTreatmentHospital);
                    } else if (!medicalTreadtmentKey.contains(medicalTreatmentHospital.getHospitalNumber() + "||" + medicalTreatmentHospital.getHospitalListNumber())) {
                        medicalTreadtmentKey.add(medicalTreatmentHospital.getHospitalNumber() + "||" + medicalTreatmentHospital.getHospitalListNumber());
                        ((List) medicalTreatmentHospitals).add(medicalTreatmentHospital);
                    }
                }
                //处理完值以后。来处理错误的数据
                workbook.close();
                if (errorMedicalTreatmentHospitals.size() > 0) {
                    StringBuffer var35 = new StringBuffer();
                    int var36 = 0;
                    Iterator var37 = errorMedicalTreatmentHospitals.iterator();

                    while (var37.hasNext()) {
                        MedicalTreatmentHospital var39 = (MedicalTreatmentHospital) var37.next();
                        var35.append(var39.getHospitalNumber());
                        var35.append(",");
                        var35.append(var39.getHospitalListNumber());
                        var35.append(";");
                        ++var36;
                        if (var36 > 10) {
                            break;
                        }
                    }
                    //导入医疗目录错误。
                    ServiceException var38 = new ServiceException(Configuration.getInstance().getValue("IMP_MEDICAL_TREATMENT_ERROR_NOT_NULL") + var35.toString());
                    log.warn(Configuration.getInstance().getValue("IMP_MEDICAL_TREATMENT_ERROR_NOT_NULL"), var38);
                    throw var38;
                } else {


                    if (log.isDebugEnabled()) {
                        log.debug(values.getValue("checkHospitalNumber"));
                    }

                    //检查是否多家医院----------判断size是否等于1，目前只支持一家医院医疗目录导入
                    //
                    if (hospitalInfos.size() != 1) {
                        log.info(values.getValue("exportMessage"));
                        JsfUtil.addErrorMessage(values.getValue("exportMessage"));
                        return;
                    }


                    try {
                        //根据文件中的医院编码去判断这家医院是否存在。存在就继续执行。不存在就跳出
                        HospitalOperation hospitalOperation = hospitalOperationFacade.getHospitalOperationByHospitalNumber(hospitalNumber);
                        if (log.isDebugEnabled()) {
                            log.debug(values.getValue("checkHospitalNumberFinish"));
                            log.debug(values.getValue("checkMedical"));

                        }
                        //判断有没用获取数据，即是否存在
                        if (hospitalOperation != null &&!"".equals(hospitalOperation.getVpnIP())) {
                            log.info(values.getValue("temporaryFileAddress") + file.getCanonicalPath());
                            //上传到对应平台的结果
                            String rs = UploadFileUtils.uploadExcel(
                                    new UrlUtil().getUrl() + new Value().getPlatformUrlUpload(), file.getCanonicalPath());
                            //将返回值进行判断 如果
                            if (rs.indexOf(":") == 7) {
                                MedicalNoTreatmentHospitalService medicalNoTreatmentHospitalService =
                                        new RouteGwtRpc<MedicalNoTreatmentHospitalService>(
                                                new UrlUtil().getUrl()).getService(MedicalNoTreatmentHospitalService.class, "medicalNoTreatmentHospital");
                                //文件名
                                String fileName = rs.substring(rs.lastIndexOf(":") + 1, rs.length());
                                medicalNoTreatmentHospitalService.uploadMedicalTreatMent(fileName);
                                //上面的逻辑是得到上传后返回的文件名，在调用数据平台的接口传入名字进行导入。这里只能捕获中间是否有异常，无返回值
                                JsfUtil.addSuccessMessage(values.getValue("fileUploadSuccessful"));
                            } else {
                                log.warn(values.getValue("notUploadDataform"));
                                JsfUtil.addErrorMessage(values.getValue("notUploadDataform"));
                            }
                        } else {
                            log.warn(values.getValue("notHospital"));
                            JsfUtil.addErrorMessage(values.getValue("notHospital"));

                        }
                    } catch (Exception e) {
                        log.warn(values.getValue("ImportException"));
                        JsfUtil.addErrorMessage(e, values.getValue("ImportException"));
                    }
                }
            } else {
                log.warn(values.getValue("uploadFileWrong"));
                JsfUtil.addErrorMessage(values.getValue("uploadFileWrong"));
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e,values.getValue("handleUploadFile"));
            log.warn(values.getValue("handleUploadFile"),e);
        }
    }


    private MedicalTreatmentHospital checkData(MedicalTreatmentHospital medicalTreatmentHospital) {
        return medicalTreatmentHospital.getHospitalNumber() != null && !medicalTreatmentHospital.getHospitalNumber().equals("") ? (medicalTreatmentHospital.getMedicalTreatmentNumber() != null && !medicalTreatmentHospital.getMedicalTreatmentNumber().equals("") ? (medicalTreatmentHospital.getHospitalListName() != null && !medicalTreatmentHospital.getHospitalListName().equals("") ? (medicalTreatmentHospital.getHospitalListNumber() != null && !medicalTreatmentHospital.getHospitalListNumber().equals("") ? null : medicalTreatmentHospital) : medicalTreatmentHospital) : medicalTreatmentHospital) : medicalTreatmentHospital;
    }

    private MedicalTreatmentHospital checkNullValue(MedicalTreatmentHospital medicalTreatmentHospital) {
        return medicalTreatmentHospital.getHospitalNumber() != null && medicalTreatmentHospital.getHospitalNumber().equals("") ? medicalTreatmentHospital : (medicalTreatmentHospital.getMedicalTreatmentNumber() != null && medicalTreatmentHospital.getMedicalTreatmentNumber().equals("") ? medicalTreatmentHospital : (medicalTreatmentHospital.getHospitalListName() != null && medicalTreatmentHospital.getHospitalListName().equals("") ? medicalTreatmentHospital : (medicalTreatmentHospital.getHospitalListNumber() != null && medicalTreatmentHospital.getHospitalListNumber().equals("") ? medicalTreatmentHospital : null)));
    }


    public void inputstreamtofile(InputStream ins, File file) throws Exception {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
}
