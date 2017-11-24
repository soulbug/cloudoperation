package com.gsafer.smartoperation.facade;

import com.gsafer.smartoperation.entity.HospitalOperation;
import com.gsafer.smartoperation.facade.AbstractFacade;
import com.gsafer.smartoperation.facade.LazyEntityDataModel;
import org.primefaces.model.SortOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2017/10/26
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用 有数据平台的前置机对应医院
 */
public class HospitalLazyModel extends LazyEntityDataModel<HospitalOperation>{

    public HospitalLazyModel(AbstractFacade<HospitalOperation> facade) {
        super(facade);
    }
    @Override
    public List<HospitalOperation> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        if (filters==null){
            filters=new HashMap<String,Object>();
        }
        filters.put("category","1");
        filters.put("status","0");

        return super.load(first, pageSize, sortField, sortOrder, filters);
    }
}
