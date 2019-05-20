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
public class QueryBeanQuestionsInfo implements Serializable {
    /**
     * 题目类型名称（问答题，选择题，判断题）
     */
    private String questionName;
    /**
     * 题目类型级别
     */
    private Integer questionLevel;
    /**
     * 试题科目id
     */
    private String questionCourseId;
    /**
     * 题目类型来源
     */
    private String questionOwner;
    /**
     * 题目类型标签（#java#cache#这种标识）
     */
    private String tag;
    /**
     * 操作状态（可用，不可用）
     */
    private String status;
    /**
     * 操作日期
     */
    private String doneDate;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 行数
     */
    private Integer pageSize;
}
