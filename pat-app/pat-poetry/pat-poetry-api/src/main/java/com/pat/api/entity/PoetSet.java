package com.pat.api.entity;
import java.util.Date;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 中国古典文集
* gen by beetlsql3 2022-02-28
*/
@Data
@Table(name="poet_set")
public class PoetSet implements java.io.Serializable {
	@AutoID
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
	 * 数据集类型
	 */
	private String setType;
	/**
	 * 备注说明
	 */
	private String remark;
	/**
	 * 更新时间
	 */
	private Date updateTs;
	/**
	 * 版本号
	 */
	private Long version;
	/**
	 * 文数据总览
	 */
	private String infos;
	/**
	 * 描述
	 */
	private String describe;

}
