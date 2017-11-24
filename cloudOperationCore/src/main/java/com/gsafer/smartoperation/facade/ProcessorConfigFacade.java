/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gsafer.smartoperation.facade;

import com.gsafer.smartoperation.entity.ProcessorConfig;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import com.gsafer.smartoperation.entity.ProcessorConfig_;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gsafer.smartoperation.entity.HospitalOperation;

import java.util.List;

/**
 *
 * @author Flymonk
 */
@Stateless
public class ProcessorConfigFacade extends AbstractFacade<ProcessorConfig> {

    @PersistenceContext(unitName = "com.gsafer_smartOperation_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcessorConfigFacade() {
        super(ProcessorConfig.class);
    }

/*    public boolean isHospitalOperationEmpty(ProcessorConfig entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProcessorConfig> processorConfig = cq.from(ProcessorConfig.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(processorConfig, entity), cb.isNotNull(processorConfig.get(ProcessorConfig_.hospitalOperation)));
        return em.createQuery(cq).getResultList().isEmpty();
    }*/

    public HospitalOperation findHospitalOperation(ProcessorConfig entity) {
        return this.getMergedEntity(entity).getHospitalOperation();
    }

    /**
     * update by ethan on 2017/8/1.
     * 删除对应的网络配置信息
     * @param hospitalOperation 实施医院信息
     */
 /*   public void deleteConfig(HospitalOperation hospitalOperation){
        Query query=getEntityManager().createQuery("delete from ProcessorConfig o where o.hospitalOperation=?1");
        query.setParameter(1, hospitalOperation);
        query.executeUpdate();
    }*/

    /**
     * 查询医院的网络配置
     * @param hospitalOperation
     * @return 存放网络配置的list
     */
    public List<ProcessorConfig> findConfig(HospitalOperation hospitalOperation){
        String sql="select p FROM ProcessorConfig p where p.hospitalOperation=:hospitalOperation";
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("hospitalOperation",hospitalOperation);
        return query.getResultList();
    }
}
