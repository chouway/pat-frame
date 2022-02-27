package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 中国古典文集
* gen by beetlsql3 2022-02-27
*/
@Data
@Table(name="poet_set")
public class PoetSet implements java.io.Serializable {

    @AssignID
	private Long id;
	/**
	 * 数据集名称cn
	 */
	private String nameCn;
	/**
	 * 数据集名称en
	 */
	private String nameEn;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 备注说明
	 */
	private String remark;
	/**
	 * 数据集类型
	 */
	private String setType;
	/**
	 * 文数据量
	 */
	private Long infoNum;

}
