package com.pat.api.entity;

import lombok.Data;
import org.beetl.sql.annotation.entity.*;
import java.util.Date;
/*
* 异常信息
* gen by beetlsql3 2021-05-19
*/
@Data
@Table(name="pat_error")
public class PatError implements java.io.Serializable {
	@AutoID
	private Long id;
	/**
	 * 项目
	 */
	private String project;
	/**
	 * 服务
	 */
	private String service;
	/**
	 * 方法
	 */
	private String method;
	/**
	 * 参数类信息
	 */
	private String paramClazz;
	/**
	 * 参数信息
	 */
	private String paramJson;
	/**
	 * 异常类信息
	 */
	private String exception;
	/**
	 * 错误码
	 */
	private String errorCode;
	/**
	 * 错误信息
	 */
	private String message;
	/**
	 * 错误堆栈信息
	 */
	private String stackTrace;
	/**
	 * 日志uid
	 */
	private String logId;
	/**
	 * 本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个
	 */
	private String ip;
	/**
	 * 状态： 初始化 0 ,  成功处理 1 , 失败处理 2
	 */
	private String status;
	/**
	 * 处理成功的结果数据
	 */
	private String handleResultJson;
	/**
	 * 处理失败的errorId
	 */
	private Long handleErrorId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTs;
	/**
	 * 更新时间
	 */
	private Date updateTs;

}
