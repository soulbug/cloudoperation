/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gsafer.smartoperation.facade;

import com.gsafer.smartoperation.entity.HospitalOperation;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Flymonk
 */
@Stateless
/**
 * 医院实施的持久层
 * update by ethan on 2017/8/1.
 */
public class HospitalOperationFacade extends AbstractFacade<HospitalOperation> {

    @PersistenceContext(unitName = "com.gsafer_smartOperation_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HospitalOperationFacade() {
        super(HospitalOperation.class);
    }

    /**
     * by ethan on 2017/8/1.
     * 查询已实施医院对应所有的mac地址
     *
     * @return 所有的mac地址
     */
    @SuppressWarnings("unchecked")
    public List<String> getAllMac() {
        String sql = "select hospital.processorMac FROM HospitalOperation hospital";
        Query query = getEntityManager().createQuery(sql);
        return query.getResultList();
    }

    /**
     * create update by ethan on 2017/8/1.
     * 查询行政区划代码对应的医院实施信息
     *
     * @param areaCode 传入行政区划代码
     * @return 医院实施信息
     */
    @SuppressWarnings("unchecked")
    public HospitalOperation findByAreaCode(String areaCode) {
        Map map = new HashMap<String, Object>();
        map.put("areaCode", areaCode);
        List<HospitalOperation> listHospitalOperation = super.findRange(0, 1, "hospitalNumber", "DESC", map);
        if (listHospitalOperation.size() == 0) {
            return null;
        } else {
            return listHospitalOperation.get(0);
        }


    }

    /**
     * create update by ethan on 2017/8/1.
     * 查询医院名字对应的医院实施信息
     *
     * @param name 传入医院名字
     * @return 医院实施信息
     */
    @SuppressWarnings("unchecked")
    public HospitalOperation findByName(String name) {
        String sql = "select hospital FROM HospitalOperation hospital where hospital.hospitalName=:name";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("name", name);
        List<HospitalOperation> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * create update by ethan on 2017/11/20.
     * 查询hosptialNumber对应的医院实施信息
     *
     * @param hospitalNumber 传入hospitalNumber
     * @return 医院实施信息
     */
    @SuppressWarnings("unchecked")
    public HospitalOperation findByHosptitalNumber(String hospitalNumber) {
        String sql = "select hospital FROM HospitalOperation hospital where hospital.hospitalNumber=:hospitalNumber";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("hospitalNumber", hospitalNumber);
        List<HospitalOperation> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * by ethan on 2017/10/20
     * 查询已实施医院对应所有的mac地址
     *
     * @return 所有的mac地址
     */
    @SuppressWarnings("unchecked")
    public List<HospitalOperation> getAll() {
        String sql = "select hospital FROM HospitalOperation hospital where hospital.category=1 and hospital.status=0";
        Query query = getEntityManager().createQuery(sql);
        return query.getResultList();
    }

    /**
     * by vince on 2017/10/27
     * 获取所有为新方式的，未被删除的，有内部IP的前置机的前置机信息
     *
     * @return 前置机信息
     */
    @SuppressWarnings("unchecked")
    public List<HospitalOperation> getAllNewHospital() {
        String sql = "SELECT hospital from HospitalOperation hospital where hospital.status=0 and hospital.category=1 and hospital.vpnIP is not null ";
        Query query = getEntityManager().createQuery(sql);
        return query.getResultList();
    }


    /**
     * by vince on 2017/10/27
     * 传入医院编码返回医院对应信息
     *
     * @param hospitalNumber 医院编码
     * @return 医院前置机信息
     */
    @SuppressWarnings("unchecked")
    public HospitalOperation getHospitalOperationByHospitalNumber(String hospitalNumber) {
        String sql = "SELECT hospital from HospitalOperation hospital where hospital.status=0 and hospital.category=1 and hospital.vpnIP is not null and hospital.hospitalNumber=:hospitalNumber";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("hospitalNumber", hospitalNumber);
        List<HospitalOperation> hospitalOperations = query.getResultList();
        if (hospitalOperations.size() == 0) {
            return null;
        } else {
            return hospitalOperations.get(0);
        }
    }

    /**
     * 获取某公司已定点医院列表
     *
     * @param managementOrganizationCode 公司编码
     * @return 某公司已定点医院列表
     */
    @SuppressWarnings("unchecked")
    public List<HospitalOperation> getAlreadyDesognateHospital(String managementOrganizationCode) {
        String sql = "select tho from HospitalOperation tho, DesignateHospitalEntity TDH where tho.hospitalNumber=TDH.hospitalNumber and TDH.managementOrganizationCode=:managementOrganizationCode and tho.status=0 and tho.category=1";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("managementOrganizationCode", managementOrganizationCode);
        return query.getResultList();
    }

    /**
     * 进行逻辑删除 by ethan
     *
     * @param hospitalOperation 医院对象
     */
    public void delete(HospitalOperation hospitalOperation) {
        super.edit(hospitalOperation);
    }

    /**
     * 根据医院编码查询医院对应的vpnIP
     *
     * @param hospitalNumber 医院编码
     * @return 医院对应的vpnIP
     */
    @SuppressWarnings("unchecked")
    public String getIp(String hospitalNumber) {
        String sql = "select h.vpnIP from HospitalOperation h where h.hospitalNumber=:hospitalNumber";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("hospitalNumber", hospitalNumber);
        List<String> vpnIP = query.getResultList();
        if (vpnIP.size() == 0) {
            return null;
        } else {
            return vpnIP.get(0);
        }
    }
}
