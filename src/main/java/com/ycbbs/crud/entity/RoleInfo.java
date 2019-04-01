package com.ycbbs.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
     * 是否可用,1：可用，0不可用
     */
    private String available;

}
