package com.gsafer.smartoperation.entity;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * Created by phoebe on 2017/10/18.
 */
@Entity
@Table(name = "T_MANAGEMENT_ORGANIZATION")
public class ManagementOrganizationEntity implements Cloneable
{
    private long managementOrganizationId;
    private String managementOrganizationCode;
    private String managementOrganizationName;
    private String linkman;
    private String telephone;
    private String address;
    private String mobilephone;
    private String serverIp;
    private String remark;
    private String serverProt;
    private String standbyServerIp;
    private String standbyServerProt;
    private String analogueCalculateFlag;
    private String confirmCompensateFlag;
    private long maxHospitalTotal;
    private String areaCode;
    private Long compensableOrder;
    private String recordCreator;
    private Timestamp recordCreateTime;
    private String managementOrgShortname;

    @Basic
    @Column(name = "MANAGEMENT_ORGANIZATION_ID")
    public long getManagementOrganizationId ()
    {
        return managementOrganizationId;
    }

    public void setManagementOrganizationId (long managementOrganizationId)
    {
        this.managementOrganizationId = managementOrganizationId;
    }

    @Id
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
    @Column(name = "MANAGEMENT_ORGANIZATION_NAME")
    public String getManagementOrganizationName ()
    {
        return managementOrganizationName;
    }

    public void setManagementOrganizationName (String managementOrganizationName)
    {
        this.managementOrganizationName = managementOrganizationName;
    }

    @Basic
    @Column(name = "LINKMAN")
    public String getLinkman ()
    {
        return linkman;
    }

    public void setLinkman (String linkman)
    {
        this.linkman = linkman;
    }

    @Basic
    @Column(name = "TELEPHONE")
    public String getTelephone ()
    {
        return telephone;
    }

    public void setTelephone (String telephone)
    {
        this.telephone = telephone;
    }

    @Basic
    @Column(name = "ADDRESS")
    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    @Basic
    @Column(name = "MOBILEPHONE")
    public String getMobilephone ()
    {
        return mobilephone;
    }

    public void setMobilephone (String mobilephone)
    {
        this.mobilephone = mobilephone;
    }

    @Basic
    @Column(name = "SERVER_IP")
    public String getServerIp ()
    {
        return serverIp;
    }

    public void setServerIp (String serverIp)
    {
        this.serverIp = serverIp;
    }

    @Basic
    @Column(name = "REMARK")
    public String getRemark ()
    {
        return remark;
    }

    public void setRemark (String remark)
    {
        this.remark = remark;
    }

    @Basic
    @Column(name = "SERVER_PROT")
    public String getServerProt ()
    {
        return serverProt;
    }

    public void setServerProt (String serverProt)
    {
        this.serverProt = serverProt;
    }

    @Basic
    @Column(name = "STANDBY_SERVER_IP")
    public String getStandbyServerIp ()
    {
        return standbyServerIp;
    }

    public void setStandbyServerIp (String standbyServerIp)
    {
        this.standbyServerIp = standbyServerIp;
    }

    @Basic
    @Column(name = "STANDBY_SERVER_PROT")
    public String getStandbyServerProt ()
    {
        return standbyServerProt;
    }

    public void setStandbyServerProt (String standbyServerProt)
    {
        this.standbyServerProt = standbyServerProt;
    }

    @Basic
    @Column(name = "ANALOGUE_CALCULATE_FLAG")
    public String getAnalogueCalculateFlag ()
    {
        return analogueCalculateFlag;
    }

    public void setAnalogueCalculateFlag (String analogueCalculateFlag)
    {
        this.analogueCalculateFlag = analogueCalculateFlag;
    }

    @Basic
    @Column(name = "CONFIRM_COMPENSATE_FLAG")
    public String getConfirmCompensateFlag ()
    {
        return confirmCompensateFlag;
    }

    public void setConfirmCompensateFlag (String confirmCompensateFlag)
    {
        this.confirmCompensateFlag = confirmCompensateFlag;
    }

    @Basic
    @Column(name = "MAX_HOSPITAL_TOTAL")
    public long getMaxHospitalTotal ()
    {
        return maxHospitalTotal;
    }

    public void setMaxHospitalTotal (long maxHospitalTotal)
    {
        this.maxHospitalTotal = maxHospitalTotal;
    }

    @Basic
    @Column(name = "AREA_CODE")
    public String getAreaCode ()
    {
        return areaCode;
    }

    public void setAreaCode (String areaCode)
    {
        this.areaCode = areaCode;
    }

    @Basic
    @Column(name = "COMPENSABLE_ORDER")
    public Long getCompensableOrder ()
    {
        return compensableOrder;
    }

    public void setCompensableOrder (Long compensableOrder)
    {
        this.compensableOrder = compensableOrder;
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

    @Basic
    @Column(name = "MANAGEMENT_ORG_SHORTNAME")
    public String getManagementOrgShortname ()
    {
        return managementOrgShortname;
    }

    public void setManagementOrgShortname (String managementOrgShortname)
    {
        this.managementOrgShortname = managementOrgShortname;
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

        ManagementOrganizationEntity that = (ManagementOrganizationEntity) o;

        if (managementOrganizationId != that.managementOrganizationId)
        {
            return false;
        }
        if (maxHospitalTotal != that.maxHospitalTotal)
        {
            return false;
        }
        if (managementOrganizationCode != null ? !managementOrganizationCode.equals (
                that.managementOrganizationCode) : that.managementOrganizationCode != null)
        {
            return false;
        }
        if (managementOrganizationName != null ? !managementOrganizationName.equals (
                that.managementOrganizationName) : that.managementOrganizationName != null)
        {
            return false;
        }
        if (linkman != null ? !linkman.equals (
                that.linkman) : that.linkman != null)
        {
            return false;
        }
        if (telephone != null ? !telephone.equals (
                that.telephone) : that.telephone != null)
        {
            return false;
        }
        if (address != null ? !address.equals (
                that.address) : that.address != null)
        {
            return false;
        }
        if (mobilephone != null ? !mobilephone.equals (
                that.mobilephone) : that.mobilephone != null)
        {
            return false;
        }
        if (serverIp != null ? !serverIp.equals (
                that.serverIp) : that.serverIp != null)
        {
            return false;
        }
        if (remark != null ? !remark.equals (that.remark) : that.remark != null)
        {
            return false;
        }
        if (serverProt != null ? !serverProt.equals (
                that.serverProt) : that.serverProt != null)
        {
            return false;
        }
        if (standbyServerIp != null ? !standbyServerIp.equals (
                that.standbyServerIp) : that.standbyServerIp != null)
        {
            return false;
        }
        if (standbyServerProt != null ? !standbyServerProt.equals (
                that.standbyServerProt) : that.standbyServerProt != null)
        {
            return false;
        }
        if (analogueCalculateFlag != null ? !analogueCalculateFlag.equals (
                that.analogueCalculateFlag) : that.analogueCalculateFlag != null)
        {
            return false;
        }
        if (confirmCompensateFlag != null ? !confirmCompensateFlag.equals (
                that.confirmCompensateFlag) : that.confirmCompensateFlag != null)
        {
            return false;
        }
        if (areaCode != null ? !areaCode.equals (
                that.areaCode) : that.areaCode != null)
        {
            return false;
        }
        if (compensableOrder != null ? !compensableOrder.equals (
                that.compensableOrder) : that.compensableOrder != null)
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
        if (managementOrgShortname != null ? !managementOrgShortname.equals (
                that.managementOrgShortname) : that.managementOrgShortname != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode ()
    {
        int result = (int) (managementOrganizationId ^ (managementOrganizationId >>> 32));
        result = 31 * result + (managementOrganizationCode != null ? managementOrganizationCode.hashCode () : 0);
        result = 31 * result + (managementOrganizationName != null ? managementOrganizationName.hashCode () : 0);
        result = 31 * result + (linkman != null ? linkman.hashCode () : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode () : 0);
        result = 31 * result + (address != null ? address.hashCode () : 0);
        result = 31 * result + (mobilephone != null ? mobilephone.hashCode () : 0);
        result = 31 * result + (serverIp != null ? serverIp.hashCode () : 0);
        result = 31 * result + (remark != null ? remark.hashCode () : 0);
        result = 31 * result + (serverProt != null ? serverProt.hashCode () : 0);
        result = 31 * result + (standbyServerIp != null ? standbyServerIp.hashCode () : 0);
        result = 31 * result + (standbyServerProt != null ? standbyServerProt.hashCode () : 0);
        result = 31 * result + (analogueCalculateFlag != null ? analogueCalculateFlag.hashCode () : 0);
        result = 31 * result + (confirmCompensateFlag != null ? confirmCompensateFlag.hashCode () : 0);
        result = 31 * result + (int) (maxHospitalTotal ^ (maxHospitalTotal >>> 32));
        result = 31 * result + (areaCode != null ? areaCode.hashCode () : 0);
        result = 31 * result + (compensableOrder != null ? compensableOrder.hashCode () : 0);
        result = 31 * result + (recordCreator != null ? recordCreator.hashCode () : 0);
        result = 31 * result + (recordCreateTime != null ? recordCreateTime.hashCode () : 0);
        result = 31 * result + (managementOrgShortname != null ? managementOrgShortname.hashCode () : 0);
        return result;
    }
    @Override
    public Object clone() {
        ManagementOrganizationEntity managementOrganizationEntity = null;
        try{
            managementOrganizationEntity = (ManagementOrganizationEntity)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return managementOrganizationEntity;
    }
}
