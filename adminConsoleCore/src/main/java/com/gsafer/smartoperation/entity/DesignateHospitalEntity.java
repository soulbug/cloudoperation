package com.gsafer.smartoperation.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/10/25.
 */
@Entity
@Table(name = "T_DESIGNATE_HOSPITAL")
public class DesignateHospitalEntity
{
    private long designateHospitalId;
    private String managementOrganizationCode;
    private String hospitalNumber;
    private String payWay;
    private String recordCreator;
    private Timestamp recordCreateTime;

    @Id
    @Column(name = "DESIGNATE_HOSPITAL_ID")
    public long getDesignateHospitalId ()
    {
        return designateHospitalId;
    }

    public void setDesignateHospitalId (long designateHospitalId)
    {
        this.designateHospitalId = designateHospitalId;
    }

    @Basic
    @Column(name = "MANAGEMENT_ORGANIZATION_CODE")
    public String getManagementOrganizationCode ()
    {
        return managementOrganizationCode;
    }

    public void setManagementOrganizationCode (String managementOrganizationCode)
    {
        this.managementOrganizationCode = managementOrganizationCode;
    }

    @Basic
    @Column(name = "HOSPITAL_NUMBER")
    public String getHospitalNumber ()
    {
        return hospitalNumber;
    }

    public void setHospitalNumber (String hospitalNumber)
    {
        this.hospitalNumber = hospitalNumber;
    }

    @Basic
    @Column(name = "PAY_WAY")
    public String getPayWay ()
    {
        return payWay;
    }

    public void setPayWay (String payWay)
    {
        this.payWay = payWay;
    }

    @Basic
    @Column(name = "RECORD_CREATOR")
    public String getRecordCreator ()
    {
        return recordCreator;
    }

    public void setRecordCreator (String recordCreator)
    {
        this.recordCreator = recordCreator;
    }

    @Basic
    @Column(name = "RECORD_CREATE_TIME")
    public Timestamp getRecordCreateTime ()
    {
        return recordCreateTime;
    }

    public void setRecordCreateTime (Timestamp recordCreateTime)
    {
        this.recordCreateTime = recordCreateTime;
    }

    @Override
    public boolean equals (Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass () != o.getClass ())
        {
            return false;
        }

        DesignateHospitalEntity that = (DesignateHospitalEntity) o;

        if (designateHospitalId != that.designateHospitalId)
        {
            return false;
        }
        if (managementOrganizationCode != null ? !managementOrganizationCode.equals (
                that.managementOrganizationCode) : that.managementOrganizationCode != null)
        {
            return false;
        }
        if (hospitalNumber != null ? !hospitalNumber.equals (
                that.hospitalNumber) : that.hospitalNumber != null)
        {
            return false;
        }
        if (payWay != null ? !payWay.equals (that.payWay) : that.payWay != null)
        {
            return false;
        }
        if (recordCreator != null ? !recordCreator.equals (
                that.recordCreator) : that.recordCreator != null)
        {
            return false;
        }
        if (recordCreateTime != null ? !recordCreateTime.equals (
                that.recordCreateTime) : that.recordCreateTime != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode ()
    {
        int result = (int) (designateHospitalId ^ (designateHospitalId >>> 32));
        result = 31 * result + (managementOrganizationCode != null ? managementOrganizationCode.hashCode () : 0);
        result = 31 * result + (hospitalNumber != null ? hospitalNumber.hashCode () : 0);
        result = 31 * result + (payWay != null ? payWay.hashCode () : 0);
        result = 31 * result + (recordCreator != null ? recordCreator.hashCode () : 0);
        result = 31 * result + (recordCreateTime != null ? recordCreateTime.hashCode () : 0);
        return result;
    }
}
