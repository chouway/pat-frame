package com.pat.api.entity;
import java.util.Date;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 推荐词
* gen by beetlsql3 2022-03-10
*/
@Data
@Table(name="poet_suggest")
public class PoetSuggest implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 推荐词
	 */
	private String keyword;
	/**
	 * 类型： 00 诗人  01 作品名  02 诗人 作品名 03 诗句  04   诗短句 05 意象 06 其它
	 */
	private String keyType;
	/**
	 * 关联id  
	 */
	private Long relId;
	/**
	 * 选中频次（相关类型有联动累积）
	 */
	private Long count;
	/**
	 * 更新时间
	 */
	private Date updateTs;
	/**
	 * 版本号
	 */
	@Version
	private Long version;

}
