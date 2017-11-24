package com.gsafer.smartoperation.facade;

import com.gsafer.smartoperation.entity.HospitalOperation;
import com.gsafer.smartoperation.entity.ManagementOrganizationEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by phoebe on 2017/10/18.
 */
@Stateless
public class ManagementOrganizationFacade extends AbstractFacade<ManagementOrganizationEntity>
{
    @PersistenceContext(unitName = "com.gsafer_smartOperation_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    /**
     *  by phoebe on 2017/10/20.
     * 通过公司编码查询公司信息
     * @return 查询到的公司信息
     */
    public List<ManagementOrganizationEntity> getManagementOrgByCode(String managementOrganizationCode){
        String sql="select m FROM ManagementOrganizationEntity m where m.managementOrganizationCode=:managementOrganizationCode";
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("managementOrganizationCode",managementOrganizationCode);
        return query.getResultList ();
    }
    /**
     *  by phoebe on 2017/10/23.
     * 通过医院编码查询医院信息
     * @return 查询到的医院实体类
     */
    public List<HospitalOperation> getHospitalOperationByNumber(String hospitalNumber){
        String sql="select h FROM  HospitalOperation h where h.hospitalNumber=:hospitalNumber and h.status=0 and h.category=1"  ;
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("hospitalNumber",hospitalNumber);
        return query.getResultList ();
    }

    /**
     *  by phoebe on 2017/10/23.
     * 通过公司编码查询前置机信息
     * @return 查询到的前置机信息实体类
     */
    public List<HospitalOperation> getHospitalOperationByManagementOrganizationCode(String managementOrganizationCode){
        String sql="SELECT ho from DesignateHospitalEntity d\n" +
                "        LEFT JOIN HospitalOperation ho on ho.hospitalNumber=d.hospitalNumber\n" +
                "        where ho.status=0 and ho.category=1 and ho.vpnIP is not null and d.managementOrganizationCode=:managementOrganizationCode"  ;
        Query query=getEntityManager().createQuery(sql);
        query.setParameter("managementOrganizationCode",managementOrganizationCode);
        return query.getResultList ();
    }
    @Override
    protected EntityManager getEntityManager ()
    {
        return em;
    }
    public ManagementOrganizationFacade() {
        super(ManagementOrganizationEntity.class);
    }
}
