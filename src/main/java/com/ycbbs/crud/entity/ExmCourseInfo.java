package com.ycbbs.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "answer_exmCourse")
public class ExmCourseInfo implements Serializable {
    /**
     * 试题科目id
     */
    @Id
    @Column(name="course_id")
    private String courseId;
    /**
     * 试题名称（java，C#，前端）
     */
    @Column(name="course_name")
    private String courseName;
    /**
     * 试题类型（Spring,java基础，winform）
     */
    @Column(name="course_type")
    private String courseType;
}
