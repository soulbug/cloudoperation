package com.gsafer.smartoperation.util;

import com.alibaba.fastjson.JSON;
import com.gsafer.smartoperation.entity.City;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;

/**
 * date 2017/8/26
 * author ethan
 * Copyright @ 互联极简网络科技成都有限公司
 * 类的作用 将地区区划代码存入map中，提高读取效率
 */
@ApplicationScoped
public class MapArea {

    public MapArea() {
    }


    /**将json字符串转成city存入list中*/
    private List<City> lists = JSON.parseArray(ReadJson.readFile(), City.class);
    /**存放省id的key的list*/
    private List<Integer> provinceKeyList;
    /**存放市id的key的list*/
    private List<Integer> CityKeyList;
    /**map的key为city对象的id，value为city对象*/
    private List<City> listProvince;
    /**存放省下面的市*/
    private Map<Integer, List<City>> mapCity;
    /**存放省下面的市*/
    private Map<Integer, List<City>> mapArea;

    public List<City> getListProvince() {
        if (listProvince == null) {
            return getProvince();
        } else {
        return listProvince;
    }
    }

    public Map<Integer, List<City>> getMapCity() {
        if (mapCity == null) {
            return getCity();
        } else {
            return mapCity;
        }
    }

    public Map<Integer, List<City>> getMapArea() {
        if (mapArea == null) {
            return getArea();
        } else {
            return mapArea;
        }
    }

    /**
     * 得到所有省的名字
     *
     * @return 以省的list
     */
    private synchronized List<City> getProvince() {
        listProvince = new ArrayList<>();
        provinceKeyList = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getPid() == 0) {
                listProvince.add(lists.get(i));
                provinceKeyList.add(lists.get(i).getId());
            }
        }
        return listProvince;
    }

    /**
     * 把省下面的市放入相同存入list中
     *
     * @return 以省的id为key，省下面所有的市的map
     */
    private synchronized Map<Integer, List<City>> getCity() {
        mapCity = new HashMap<>();
        CityKeyList = new ArrayList<>();

        for (int j = 0; j < provinceKeyList.size(); j++) {
            //存放单个省下面的市
            List<City> cityList = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).getPid() == provinceKeyList.get(j)) {
                    cityList.add(lists.get(i));
                    CityKeyList.add(lists.get(i).getId());
                }

            }
            mapCity.put(provinceKeyList.get(j), cityList);
        }
        return mapCity;
    }

    /**
     * 把市下面的区放入相同存入list中
     *
     * @return 以市的id为key，市下面所有的区为value的map
     */
    private synchronized Map<Integer, List<City>> getArea() {
        mapArea = new HashMap<>();

        for (int j = 0; j < CityKeyList.size(); j++) {
            List<City> cityList = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).getPid() == CityKeyList.get(j)) {
                    cityList.add(lists.get(i));
                    CityKeyList.add(lists.get(i).getId());
                }
            }
            mapArea.put(CityKeyList.get(j), cityList);
        }
        return mapArea;
    }
}
