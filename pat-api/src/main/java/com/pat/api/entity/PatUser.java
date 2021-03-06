package com.pat.api.entity;
import java.beans.Transient;
import java.util.Date;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 
* gen by beetlsql3 2022-04-08
*/
@Data
@Table(name="pat_user")
public class PatUser implements java.io.Serializable {
	@AssignID
	private String id;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 0 待核验验证码 1 验证码成功   2 实名认证成功（需要相关验证码）
	 */
	private String status;
	/**
	 * 密码 内置盐
	 */
	private String password;

	/**
	 * 密码登录失败次数 超过一定数时冻结  待验证码登录解冻
	 */
	private Integer pwdFailCount;
	/**
	 * 最新登录时间，当长时间未登录只能用验证码形式登录
	 */
	private Date loginTs;
	/**
	 * 多个角色,以","分隔 角色常量 详见PatOauthConstant
	 */
	private String role;
	/**
	 * 创建时间
	 */
	private Date createTs;
	/**
	 * 更新时间
	 */
	private Date updateTs;
	/**
	 * 用户名称(英文字母以及数字、下划线组成)
	 */
	private String userName;
	/**
	 * 密码登录失败的日期
	 */
	private Date pwdFailDay;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 手机号
	 */
	private String phone;
}
