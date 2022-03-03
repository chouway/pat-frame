package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 诗属性表
* gen by beetlsql3 2022-03-03
*/
@Data
@Table(name="poet_property")
public class PoetProperty implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 属性key
	 */
	private String key;
	/**
	 * 属性val
	 */
	private String value;
	/**
	 * 关联类别
	 */
	private String relType;
	/**
	 * 关联id
	 */
	private Long relId;
	/**
	 * 是否有效
	 */
	private String status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 序列
	 */
	private Integer index;

}
