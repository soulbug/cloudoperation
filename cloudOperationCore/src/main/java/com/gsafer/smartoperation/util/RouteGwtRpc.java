package com.gsafer.smartoperation.util;

import com.gsafer.cis.remoting.RemotingHelper;

/**
 * date 2017/10/20
 * @author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用 获取数据平台的service类
 */
public class RouteGwtRpc<T> {
    private String url;
    private RouteGwtRpc() {

    }

    /**
     *
     * @param url 前置机数据平台的url
     */
    public RouteGwtRpc(String url) {
        this.url = url;
    }

    private RemotingHelper remotingHelper;

    private RemotingHelper getRemotingHelper() {
        if (remotingHelper == null) {
            remotingHelper = new RemotingHelper();
        }
        remotingHelper.setWebRootUrl(url);
        return remotingHelper;
    }

    /**
     *
     * @param entityClass 传入实体类对应service类
     * @param name serviceName
     * @return 操作的service
     */
    public T getService(Class<T> entityClass,String name) {
        return getRemotingHelper().newRemoteInstance(entityClass, name);
    }
}
