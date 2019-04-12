package com.ycbbs.crud.entity.querybean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QueryBeanUserInfo implements Serializable {
    /**
     * 查询关键字
     */
    private String keyword;
    /**
     * 账号激活状态（0未，1激活）
     */
    private String state;
    /**
     * 账号是否锁定，1.锁定
     */
    private String locked;
    /**
     * 是否删除(0为否，1为是)
     */
    private String deleted;
    /**
     * 信息是否完整（0为否，1为是）
     */
    private String completed;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 显示条目
     */
    private Integer pageSize;
}
