package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 
* gen by beetlsql3 2022-03-29
*/
@Data
@Table(name="pat_resource")
public class PatResource implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 资源uri
	 */
	private String uri;
	/**
	 * 资源描述
	 */
	private String uriDesc;
	/**
	 * 序号
	 */
	private Integer index;
	/**
	 * 多个以, 分隔
	 */
	private String role;

}
