package com.ycbbs.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "answer_questionsInfo")
/**
 * 题目类型基本信息表，表示为判断题，选择题，问答题等等
 */
public class QuestionsInfo implements Serializable {
    /**
     * 题目类型基本信息id
     */
    @Id
    @Column(name = "question_id")
    private String questionId;
    /**
     * 题目类型名称（问答题，选择题，判断题）
     */
    @Column(name = "question_Name")
    private String questionName;
    /**
     * 题目类型级别
     */
    @Column(name = "question_Level")
    private String questionLevel;
    /**
     * 试题科目id
     */
    @Column(name = "question_courseId")
    private String questionCourseId;
    /**
     * 试题科目实体
     */
    @Transient
    private ExmCourseInfo exmCourseInfo;
    /**
     * 题目类型来源
     */
    @Column(name = "question_owner")
    private String questionOwner;
    /**
     * 题目类型标签（#java#cache#这种标识）
     */
    @Column(name = "tag")
    private String tag;
    /**
     * 备用字段1
     */
    @Column(name = "EXT_1")
    private String ext1;
    /**
     * 备用字段2
     *
     * @return
     */
    @Column(name = "EXT_2")
    private String ext2;
    /**
     * 备用字段3
     *
     * @return
     */
    @Column(name = "EXT_3")
    private String ext3;
    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;
    /**
     * 操作状态（可用，不可用）
     */
    @Column(name = "status")
    private String status;
    /**
     * 操作日期
     */
    @Column(name = "doneDate")
    private String doneDate;
}
