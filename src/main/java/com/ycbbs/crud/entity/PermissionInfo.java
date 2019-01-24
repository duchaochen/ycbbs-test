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
    private String id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源类型：menu,button,
     */
    private String type;
    /**
     * 访问url地址
     */
    private String url;
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
     * 是否可用,1：可用，0不可用
     */
    private String available;
}
