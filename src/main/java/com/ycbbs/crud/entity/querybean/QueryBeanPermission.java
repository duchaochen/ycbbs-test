package com.ycbbs.crud.entity.querybean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class QueryBeanPermission implements Serializable {
    /**
     * 字段名称
     */
    private String fieldname;
    /**
     * 查询关键字
     */
    private String keyword;
    /**
     * 资源类型：menu,button,
     */
    private String type;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 显示条目
     */
    private Integer pageSize;
}
