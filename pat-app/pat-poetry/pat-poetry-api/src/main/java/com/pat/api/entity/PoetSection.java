package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 中国古典文集-节
* gen by beetlsql3 2022-03-07
*/
@Data
@Table(name="poet_section")
public class PoetSection implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 节
	 */
	private String section;
	/**
	 * 序列
	 */
	private Integer index;
	/**
	 * 篇id
	 */
	private Long chapterId;

}
