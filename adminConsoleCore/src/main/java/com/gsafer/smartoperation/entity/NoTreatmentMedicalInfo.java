package com.gsafer.smartoperation.entity;

import lombok.Data;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by vince on 2017/10/27.
 * 该类为获取的未对码医疗目录信息。
 * @version 0.1
 * @do
 */
@Data
@ApplicationScoped
public class NoTreatmentMedicalInfo implements Serializable{
    //有未对码医疗目录信息的医院编码
    private String hospitalNumber;

    //有未对码医疗目录信息的医院名称
    private String hospitalName;

    //未对码医疗目录条数
    private int size;
}
