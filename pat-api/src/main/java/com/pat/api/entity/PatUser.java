package com.pat.api.entity;
import java.util.Date;
import lombok.Data;
import org.beetl.sql.annotation.entity.*;
/*
* 
* gen by beetlsql3 2022-03-29
*/
@Data
@Table(name="pat_user")
public class PatUser implements java.io.Serializable {
	@AssignID
	private String id;
	/**
	 * 用户名称
	 */
	private String name;
	/**
	 * 注册方式 0 邮箱  1 手机号
	 */
	private String signUp;
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
	 * 多个角色,以","分隔 角色常量 详见PatRoleConstant
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

}
