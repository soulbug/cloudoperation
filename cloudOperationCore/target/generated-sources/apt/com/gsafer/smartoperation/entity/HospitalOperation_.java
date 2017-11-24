package com.gsafer.smartoperation.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(HospitalOperation.class)
public abstract class HospitalOperation_ {

	public static volatile SingularAttribute<HospitalOperation, String> areaCode;
	public static volatile SingularAttribute<HospitalOperation, String> hospitalGrade;
	public static volatile SingularAttribute<HospitalOperation, String> hospitalNumber;
	public static volatile SingularAttribute<HospitalOperation, Date> createTime;
	public static volatile SingularAttribute<HospitalOperation, String> vpnIP;
	public static volatile SingularAttribute<HospitalOperation, Integer> hospitalOperationId;
	public static volatile SingularAttribute<HospitalOperation, String> remark;
	public static volatile SingularAttribute<HospitalOperation, String> hospitalName;
	public static volatile SingularAttribute<HospitalOperation, Short> category;
	public static volatile SingularAttribute<HospitalOperation, String> processorMac;
	public static volatile SingularAttribute<HospitalOperation, String> hospitalKey;
	public static volatile SingularAttribute<HospitalOperation, Short> status;

}

