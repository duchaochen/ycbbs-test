package com.ycbbs.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name="sys_role")
public class RoleInfo implements Serializable{
    @Id
    private String id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 1为删除,0为可用
     */
    private String deleted;
    /**
     * 权限集合
     */
    @Transient
    private List<PermissionInfo> permissionInfos;
}
