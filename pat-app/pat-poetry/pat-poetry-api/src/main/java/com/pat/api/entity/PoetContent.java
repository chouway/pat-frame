package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 中国古典文内容
* gen by beetlsql3 2022-02-28
*/
@Data
@Table(name="poet_content")
public class PoetContent implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 段
	 */
	private String paragraph;
	/**
	 * 序列
	 */
	private Integer index;
	/**
	 * 文id
	 */
	private Long infoId;

}
