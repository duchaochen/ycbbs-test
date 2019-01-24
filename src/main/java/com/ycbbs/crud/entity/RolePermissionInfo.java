package com.ycbbs.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name="sys_role_permission")
public class RolePermissionInfo {

    @Id
    private String id;
    /**
     * 角色id
     */
    private String ycRoleId;
    /**
     * 权限id
     */
    private String ycPermissionId;
    /**
     * 通用mybatis绑定时过滤此属性
     */
    @Transient
    private RoleInfo roleInfo;
    /**
     * 通用mybatis绑定时过滤此属性
     */
    @Transient
    private PermissionInfo permissionInfo;
}
