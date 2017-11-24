/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gsafer.smartoperation.facade;

//import com.gsafer.smartoperation.entity.ProcessorRoute_;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.gsafer.smartoperation.entity.ProcessorRoute;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gsafer.smartoperation.entity.HospitalOperation;

import java.util.List;

/**
 *
 * @author Flymonk
 */
@Stateless
public class ProcessorRouteFacade extends AbstractFacade<ProcessorRoute> {

    @PersistenceContext(unitName = "com.gsafer_smartOperation_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcessorRouteFacade() {
        super(ProcessorRoute.class);
    }

/*    public boolean isHospitalOperationEmpty(ProcessorRoute entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProcessorRoute> processorRoute = cq.from(ProcessorRoute.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(processorRoute, entity), cb.isNotNull(processorRoute.get(ProcessorRoute_.hospitalOperation)));
        return em.createQuery(cq).getResultList().isEmpty();
    }*/

    public HospitalOperation findHospitalOperation(ProcessorRoute entity) {
        return this.getMergedEntity(entity).getHospitalOperation();
    }

    /**
     * 查询医院实施下的路由地址
     * @param hospitalOperation
     * @return 存放路由的list
     */
    public List<ProcessorRoute> findProcessorRoute(HospitalOperation hospitalOperation){
        String sql="select p FROM ProcessorRoute p where p.hospitalOperation=:hospitalOperation";
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("hospitalOperation",hospitalOperation);
        return query.getResultList();
    }
    
}
