package com.pat.api.entity;

import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* aibk 配置表
* gen by beetlsql3 2021-05-19
*/
@Data
@Table(name="pat_config")
public class PatConfig implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 配置名称
	 */
	private String configName;
	/**
	 * 配置code
	 */
	private String configCode;
	/**
	 * 配置值
	 */
	private String configValue;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 序列
	 */
	private Integer seqNo;
	/**
	 * 父id
	 */
	private Long parentId;

}
