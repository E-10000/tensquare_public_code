package com.tensquare_base.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/*
@Entity,做JPA的pojo一定要加这个
@Table，和那个表对应
分布式一定要继承Serializable
 */
@Entity
@Table(name = "tb_label")
//@Getter
//@Setter
//@RequiredArgsConstructor
@Data
public class Label implements Serializable {
    @Id
    private String id;
    private String labelname;//标签名称
    private String state;//状态
    private Long count;//使用数量
    private String recommend;//是否推荐
    private Long fans;//关注数

}
