package com.gsafer.smartoperation.util;

import com.gsafer.smartoperation.facade.HospitalOperationFacade;

/**
 * date 2017/10/30
 *
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用
 */
public class UrlUtil {

    public String getUrl() {
        return new Value().getDataPlatformUrl();
    }

    public String getPublicUrl() {
        return new Value().getPublicDataPlatformUrl();
    }
}
