package com.pat.api.entity;

import lombok.Data;
import org.beetl.sql.annotation.entity.SeqID;
import org.beetl.sql.annotation.entity.Table;

import java.util.Date;

/*
* 测试表
* gen by beetlsql3 2021-05-19
*/
@Data
@Table(name="pat_test_info")
public class PatTestInfo implements java.io.Serializable {

//	@AutoID,作用于属性字段或者getter方法，告诉beetlsql，这是自增主键,对应于数据自增长
//  @AssignID，作用于属性字段或者getter方法，告诉beetlsql，这是程序设定 代码设定主键允许像@AssignID 传入id的生成策略以自动生成序列，beetl默认提供了一个snowflake算法，一个用于分布式环境的id生成器
//  @SeqID(name="aibk_test_info_id_seq")
	/**
	 * https://blog.csdn.net/jsdxshi/article/details/74392184
	 * BeetlSQL 注解
	 */
	@SeqID(name="aibk_test_info_id_seq")
	private Long id;
	private String name;
	private Integer departmentId;
	private Date createTime;

}
