/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gsafer.smartoperation.facade;

import com.gsafer.smartoperation.entity.ProcessorMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import com.gsafer.smartoperation.entity.ProcessorMap_;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gsafer.smartoperation.entity.HospitalOperation;
import com.gsafer.smartoperation.entity.ProcessorRoute;

import java.util.List;

/**
 *
 * @author Flymonk
 */
@Stateless
public class ProcessorMapFacade extends AbstractFacade<ProcessorMap> {

    @PersistenceContext(unitName = "com.gsafer_smartOperation_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcessorMapFacade() {
        super(ProcessorMap.class);
    }

/*    public boolean isHospitalOperationEmpty(ProcessorMap entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProcessorMap> processorMap = cq.from(ProcessorMap.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(processorMap, entity), cb.isNotNull(processorMap.get(ProcessorMap_.hospitalOperation)));
        return em.createQuery(cq).getResultList().isEmpty();
    }*/

    public HospitalOperation findHospitalOperation(ProcessorMap entity) {
        return this.getMergedEntity(entity).getHospitalOperation();
    }
    /**
     * 查询医院实施下的映射信息
     * @param hospitalOperation
     * @return 存放路由的list
     */
    public List<ProcessorMap> findProcessorMap(HospitalOperation hospitalOperation){
        String sql="select p FROM ProcessorMap p where p.hospitalOperation=:hospitalOperation";
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("hospitalOperation",hospitalOperation);
        return query.getResultList();
    }
}
