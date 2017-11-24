package com.gsafer.smartoperation.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProcessorMap.class)
public abstract class ProcessorMap_ {

	public static volatile SingularAttribute<ProcessorMap, Date> createTime;
	public static volatile SingularAttribute<ProcessorMap, Short> portIn;
	public static volatile SingularAttribute<ProcessorMap, String> ip;
	public static volatile SingularAttribute<ProcessorMap, HospitalOperation> hospitalOperation;
	public static volatile SingularAttribute<ProcessorMap, Integer> processorMapId;
	public static volatile SingularAttribute<ProcessorMap, Short> portOut;
	public static volatile SingularAttribute<ProcessorMap, Short> status;

}

