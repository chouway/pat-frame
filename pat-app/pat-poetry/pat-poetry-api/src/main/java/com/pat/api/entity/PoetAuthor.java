package com.pat.api.entity;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 中国古典文作者
* gen by beetlsql3 2022-02-28
*/
@Data
@Table(name="poet_author")
public class PoetAuthor implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 作者
	 */
	private String name;
	/**
	 * 百科id
	 */
	private Long baikeId;
	/**
	 * 描述
	 */
	private String describe;

}
