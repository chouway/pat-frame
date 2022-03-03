package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 
* gen by beetlsql3 2022-03-03
*/
@Data
@Table(name="poet_baike")
public class PoetBaike implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 百科类型
	 */
	private String baikeType;
	/**
	 * 关联类型
	 */
	private String relType;
	/**
	 * 关联主键id
	 */
	private Long relId;
	/**
	 * 百科url
	 */
	private String baikeUrl;
	/**
	 * 百科title
	 */
	private String baikeTitle;
	/**
	 * 百科简要描述
	 */
	private String baikeDesc;

}
