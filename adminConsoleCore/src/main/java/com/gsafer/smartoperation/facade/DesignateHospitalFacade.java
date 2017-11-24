package com.gsafer.smartoperation.facade;

import com.gsafer.smartoperation.entity.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by phoebe on 2017/10/18.
 */
@Stateless
public class DesignateHospitalFacade extends AbstractFacade<DesignateHospitalEntity>
{
    @PersistenceContext(unitName = "com.gsafer_smartOperation_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    /**
     *  by phoebe on 2017/10/24.
     * 通过公司编码查询未对码医院列表
     * @return 查询到的医院信息实体类
     */
    public List<HospitalOperation> getNoDesignateHospital(String managementOrganizationCode){
        String sql="select ho from HospitalOperation ho where ho.hospitalNumber not in(select d.hospitalNumber from DesignateHospitalEntity d where d.managementOrganizationCode=:managementOrganizationCode) and ho.status=0 and ho.category=1";
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("managementOrganizationCode",managementOrganizationCode);
        return query.getResultList ();
    }

    /**
     *  by phoebe on 2017/10/26.
     * 通过公司编码查询对码医院列表
     * @return 查询到的医院信息实体类
     */
    public List<HospitalOperation> getDesignateHospitalList(String managementOrganizationCode){
        String sql="select ho from HospitalOperation ho where ho.hospitalNumber in(select d.hospitalNumber from DesignateHospitalEntity d where d.managementOrganizationCode=:managementOrganizationCode)";
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("managementOrganizationCode",managementOrganizationCode);
        return query.getResultList ();
    }

    /**
     * 根据医院编码和公司编码去取消定点
     * @param hospitalNumber
     * @param managementOrganizationCode
     * @return
     */
    public int delDesignateHospital(String hospitalNumber,String managementOrganizationCode){
        String sql="delete from DesignateHospitalEntity designateHospital where designateHospital.hospitalNumber=:hospitalNumber and designateHospital.managementOrganizationCode=:managementOrganizationCode";
        Query query=em.createQuery(sql);
        query.setParameter("hospitalNumber", hospitalNumber);
        query.setParameter("managementOrganizationCode", managementOrganizationCode);
        return query.executeUpdate();

    }
    @Override
    protected EntityManager getEntityManager ()
    {
        return em;
    }
    public DesignateHospitalFacade () {
        super(DesignateHospitalEntity.class);
    }
}
