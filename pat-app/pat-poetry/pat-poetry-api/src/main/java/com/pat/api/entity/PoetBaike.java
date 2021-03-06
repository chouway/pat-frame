package com.pat.api.entity;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 
* gen by beetlsql3 2022-03-15
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
	/**
	 * 百科搜索的结果title
	 */
	private String baikeSearchTitle;
	/**
	 * 百科搜索的key
	 */
	private String baikeSearchKey;
	/**
	 * 相似度超过0.8以上为可用 1;  解析 baikeUrl 成功 为2； 其它不可用
	 */
	private String status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 更新时间
	 */
	private Date updateTs;
	/**
	 * searchKey 和 searchTitle相似度, 通常介于0和1区间  -1表示 未找到 ，2 表示人工处理
	 */
	private BigDecimal similarVal;
	/**
	 * 待人工核验
	 */
	private String baikeCheck;
	/**
	 * 多个数字 用","分融 表示baikeDesc 每段末尾位置
	 */
	private String baikeDescParas;

}
