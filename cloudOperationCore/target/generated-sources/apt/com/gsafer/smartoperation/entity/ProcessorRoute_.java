package com.gsafer.smartoperation.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProcessorRoute.class)
public abstract class ProcessorRoute_ {

	public static volatile SingularAttribute<ProcessorRoute, String> netmask;
	public static volatile SingularAttribute<ProcessorRoute, Date> createTime;
	public static volatile SingularAttribute<ProcessorRoute, Short> eth;
	public static volatile SingularAttribute<ProcessorRoute, String> remark;
	public static volatile SingularAttribute<ProcessorRoute, HospitalOperation> hospitalOperation;
	public static volatile SingularAttribute<ProcessorRoute, Integer> processorRouteId;
	public static volatile SingularAttribute<ProcessorRoute, String> gateway;
	public static volatile SingularAttribute<ProcessorRoute, Short> status;

}

