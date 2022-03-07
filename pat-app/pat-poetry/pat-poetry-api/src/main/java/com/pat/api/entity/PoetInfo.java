package com.pat.api.entity;
import java.util.Date;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 中国古典文
* gen by beetlsql3 2022-03-07
*/
@Data
@Table(name="poet_info")
public class PoetInfo implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 副标题
	 */
	private String subtitle;
	/**
	 * 备注说明
	 */
	private String remark;
	/**
	 * 序列
	 */
	private Integer index;
	/**
	 * 作者
	 */
	private Long authorId;
	/**
	 * 篇id
	 */
	private Long chapterId;
	/**
	 * 节id
	 */
	private Long sectionId;
	/**
	 * 原文id
	 */
	private String extId;
	/**
	 * 文集id
	 */
	private Long setId;
	/**
	 * 更新时间
	 */
	private Date updateTs;
	/**
	 * 版本号
	 */
	@Version
	private Long version;
	/**
	 * 待推送搜索引擎 0  推送成功 1 推送失败 2
	 */
	private String esStatus;

}
