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
public class QueryBeanExmCourseInfo implements Serializable {
    /**
     * 查询关键字
     */
    private String keyword;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 显示条目
     */
    private Integer pageSize;
}
