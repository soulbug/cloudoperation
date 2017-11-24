/**
 * This file was generated by the Jeddict
 */ 

package com.gsafer.smartoperation.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="T_HOSPITAL_OPERATION")
public class HospitalOperation { 

    @Column(name="HOSPITAL_OPERATION_ID",table="T_HOSPITAL_OPERATION",nullable=false)
    @Id
    @GeneratedValue(generator="S_T_HOSPITAL_OPERATION",strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="S_T_HOSPITAL_OPERATION",sequenceName="S_T_HOSPITAL_OPERATION")
    private Integer hospitalOperationId;

    @Column(name="HOSPITAL_NAME",table="T_HOSPITAL_OPERATION",length=50)
    @Basic
    private String hospitalName;

    @Column(name="HOSPITAL_NUMBER",table="T_HOSPITAL_OPERATION",length=16)
    @Basic
    private String hospitalNumber;

    @Column(name="HOSPITAL_GRADE",table="T_HOSPITAL_OPERATION",length=12)
    @Basic
    private String hospitalGrade;

    @Column(name="AREA_CODE",table="T_HOSPITAL_OPERATION",length=6)
    @Basic
    private String areaCode;

    @Column(name="HOSPITAL_KEY",table="T_HOSPITAL_OPERATION",length=50)
    @Basic
    private String hospitalKey;

    @Column(name="PROCESSOR_MAC",table="T_HOSPITAL_OPERATION",length=16)
    @Basic
    private String processorMac;

    @Column(name="STATUS",table="T_HOSPITAL_OPERATION")
    @Basic
    private short status;

    @Column(name="CREATE_TIME",table="T_HOSPITAL_OPERATION")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new java.util.Date();;

    @Column(name="REMARK",table="T_HOSPITAL_OPERATION",length=50)
    @Basic
    private String remark;

    @Column(name="CATEGORY",table="T_HOSPITAL_OPERATION",length=1)
    @Basic
    private short category;
    @Column(name="VPN_IP",table="T_HOSPITAL_OPERATION",length=1)
    @Basic
    private String vpnIP;

    public short getCategory() {
        return category;
    }

    public void setCategory(short category) {
        this.category = category;
    }

    public String getVpnIP() {
        return vpnIP;
    }

    public void setVpnIP(String vpnIP) {
        this.vpnIP = vpnIP;
    }

    public Integer getHospitalOperationId() {
        return this.hospitalOperationId;
    }

    public void setHospitalOperationId(Integer hospitalOperationId) {
        this.hospitalOperationId = hospitalOperationId;
    }


    public String getHospitalName() {
        return this.hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }


    public String getHospitalNumber() {
        return this.hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }


    public String getHospitalGrade() {
        return this.hospitalGrade;
    }

    public void setHospitalGrade(String hospitalGrade) {
        this.hospitalGrade = hospitalGrade;
    }


    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }


    public String getHospitalKey() {
        return this.hospitalKey;
    }

    public void setHospitalKey(String hospitalKey) {
        this.hospitalKey = hospitalKey;
    }


    public String getProcessorMac() {
        return this.processorMac;
    }

    public void setProcessorMac(String processorMac) {
        this.processorMac = processorMac;
    }


    public short getStatus() {
        return this.status;
    }

    public void setStatus(short status) {
        this.status = status;
    }


    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}
        if (!java.util.Objects.equals(getClass(), obj.getClass())) {return false;}
        final HospitalOperation other = (HospitalOperation) obj;
        if (!java.util.Objects.equals(this.getHospitalOperationId(), other.getHospitalOperationId())) {        return false;        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.getHospitalOperationId() != null ? this.getHospitalOperationId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "HospitalOperation{" + " hospitalOperationId=" + hospitalOperationId + ", hospitalName=" + hospitalName + ", hospitalNumber=" + hospitalNumber + ", hospitalGrade=" + hospitalGrade + ", areaCode=" + areaCode + ", hospitalKey=" + hospitalKey + ", processorMac=" + processorMac + ", status=" + status + ", createTime=" + createTime + ", remark=" + remark + '}';
    }

}
