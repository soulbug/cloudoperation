package com.gsafer.smartoperation.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProcessorConfig.class)
public abstract class ProcessorConfig_ {

	public static volatile SingularAttribute<ProcessorConfig, Date> createTime;
	public static volatile SingularAttribute<ProcessorConfig, String> ip;
	public static volatile SingularAttribute<ProcessorConfig, String> netMask;
	public static volatile SingularAttribute<ProcessorConfig, Short> eth;
	public static volatile SingularAttribute<ProcessorConfig, String> remark;
	public static volatile SingularAttribute<ProcessorConfig, HospitalOperation> hospitalOperation;
	public static volatile SingularAttribute<ProcessorConfig, String> gateway;
	public static volatile SingularAttribute<ProcessorConfig, Integer> processorConfigId;
	public static volatile SingularAttribute<ProcessorConfig, Short> status;

}

