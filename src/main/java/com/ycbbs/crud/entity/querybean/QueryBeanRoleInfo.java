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
public class QueryBeanRoleInfo implements Serializable {
    /**
     * 关键字名称
     */
    private String keyword;
    /**
     * 删除
     */
    private String deleted;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 显示条目
     */
    private Integer pageSize;
}
