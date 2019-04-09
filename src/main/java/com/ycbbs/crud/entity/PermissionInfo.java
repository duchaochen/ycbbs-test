package com.ycbbs.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name="sys_permission")
public class PermissionInfo {
    @Id
    private Long id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源类型：menu,permission,
     */
    private String type;
    /**
     * 路由地址
     */
    private String router;
    /**
     * 前端组件的引用地址
     */
    private String component;
    /**
     * 权限代码字符串
     */
    private String percode;
    /**
     * 父结点id
     */
    private String parentid;
    /**
     * 父结点id列表串
     */
    private String parentids;
    /**
     * 排序号
     */
    private String sortstring;
    /**
     * 是否删除,1：删除，0未删除
     */
    private String deleted;
    /**
     * 是否在菜单上显示，1为是，0为否，0都表示为隐藏权限
     */
    private String menued;
    /**
     * 图标
     */
    private String icon;
}
