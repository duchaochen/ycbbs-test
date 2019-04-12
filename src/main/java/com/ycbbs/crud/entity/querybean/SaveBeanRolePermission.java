package com.ycbbs.crud.entity.querybean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SaveBeanRolePermission implements Serializable {
    /**
     * 角色id
     */
    private String roleid;
    /**
     * 权限集合
     */
    private List<String> permissionInfoIds;
}
