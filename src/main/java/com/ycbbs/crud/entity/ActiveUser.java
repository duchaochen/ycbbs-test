package com.ycbbs.crud.entity;

import java.util.List;

/**
 * 用户身份信息，存入session 由于tomcat将session会序列化在本地硬盘上，所以使用Serializable接口
 * 
 * @author Thinkpad
 * 
 */
public class ActiveUser implements java.io.Serializable {
	/**
	 * 用户id（主键）
	 */
	private String userid;
	/**
	 * 用户账号
	 */
	private String username;
	/**
	 * 用户名称
	 */
	private String realname;
	/**
	 * 菜单
	 */
	private List<PermissionInfo> menus;
	/**
	 * 权限
	 */
	private List<PermissionInfo> permissions;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public List<PermissionInfo> getMenus() {
		return menus;
	}

	public void setMenus(List<PermissionInfo> menus) {
		this.menus = menus;
	}

	public List<PermissionInfo> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionInfo> permissions) {
		this.permissions = permissions;
	}
}
