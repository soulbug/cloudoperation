package com.gsafer.smartoperation.facade;

import com.gsafer.smartoperation.controller.util.JsfUtil;
import com.gsafer.smartoperation.util.Value;
import com.gsafer.smartoperation.util.RouteGwtRpc;
import com.gsafer.smartoperation.util.UrlUtil;
import com.jckj.cis.client.hospitalinfo.service.HisDataSourceService;
import com.jckj.cis.entity.HisDataSource;
import lombok.extern.slf4j.Slf4j;
import net.creative.tools.gwt.client.PaginationSupport;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterion;
import net.creative.tools.gwt.client.datasource.sqlcriteria.GWTCriterionType;
import net.creative.tools.gwt.client.datasource.sqlcriteria.QueryImpl;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * date 2017/10/27
 *
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用 初始化页面的HIS配置信息
 */
@Slf4j
public class HisDataSourceLazyModel extends LazyEntityDataModel<HisDataSource> {
    /**
     * 医院编码
     */
    private String hospitalNumber;

    /**
     * 存放当前页的list
     */
    private List<HisDataSource> itemList;
    /**
     * 医院的facade
     */
    private HospitalOperationFacade hospitalOperationFacade;

    public HospitalOperationFacade getHospitalOperationFacade() {
        return hospitalOperationFacade;
    }

    public void setHospitalOperationFacade(HospitalOperationFacade hospitalOperationFacade) {
        this.hospitalOperationFacade = hospitalOperationFacade;
    }

    public HisDataSourceLazyModel(List<HisDataSource> itemList) {
        super(itemList);
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    @Override
    public List<HisDataSource> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)  {
        HisDataSourceService dataSourceService = new RouteGwtRpc<HisDataSourceService>(new UrlUtil().getUrl())
                .getService(HisDataSourceService.class, "hisDataSource");
        final QueryImpl query = new QueryImpl();
        Value value =new Value();
        //final String hospitalNumberColumnName = "HOSPITAL_NUMBER";
        GWTCriterion gwtCriterion = new GWTCriterion(
                GWTCriterionType.begins, value.getDataHospitalNumberColumn(), getHospitalNumber());
        query.setGWTCriterion(gwtCriterion);
        PaginationSupport<HisDataSource> hospitalInfoPaginationSupport;
        List<HisDataSource> listHisDataSource = null;
        try {
            hospitalInfoPaginationSupport = dataSourceService.fetch(query);
            listHisDataSource = hospitalInfoPaginationSupport.getResults();
            //设置lazyDataModel的总数
            setRowCount((int) hospitalInfoPaginationSupport.getTotalRows());
            this.itemList = listHisDataSource;
        } catch (Exception e) {
            log.warn(ResourceBundle.getBundle("/MyBundle").getString("routeError"), e);
            JsfUtil.addErrorMessage(e,ResourceBundle.getBundle("/MyBundle").getString("routeError"));
        }

        return listHisDataSource;
    }

    /**
     * Retrieves the entity based on a given Row Key.
     *
     * @param rowKey
     * @return
     */
    @Override
    public HisDataSource getRowData(String rowKey) {
        if (itemList != null) {
            for (HisDataSource entity : itemList) {
                if (getRowKey(entity).equals(rowKey)) {
                    return entity;
                }
            }
        }
        return null;
    }
}
