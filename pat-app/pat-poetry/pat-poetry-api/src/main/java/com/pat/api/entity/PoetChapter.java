package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 中国古典文集-篇
* gen by beetlsql3 2022-03-07
*/
@Data
@Table(name="poet_chapter")
public class PoetChapter implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 篇
	 */
	private String chapter;
	/**
	 * 序列
	 */
	private Integer index;
	/**
	 * 文集id
	 */
	private Long setId;

}
