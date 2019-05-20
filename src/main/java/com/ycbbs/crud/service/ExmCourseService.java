package com.ycbbs.crud.service;


import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.ExmCourseInfo;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanExmCourseInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanUserInfo;
import com.ycbbs.crud.exception.CustomException;

import java.util.List;

public interface ExmCourseService {
    /**
     * 添加试题科目表（Java,C#,Python）信息
     * @param exmCourseInfo
     * @return
     * @throws CustomException
     */
    boolean insertExmCourseInfo(ExmCourseInfo exmCourseInfo) throws CustomException;
    /**
     * 查询试题科目分类表（分页）
     * @param queryBeanExmCourseInfo
     * @return
     */
    PageInfo<ExmCourseInfo> selectKeyAll(QueryBeanExmCourseInfo queryBeanExmCourseInfo);
    /**
     * 查询试题科目分类表
     * @param queryBeanExmCourseInfo
     * @return
     */
    List<ExmCourseInfo> selectAll(QueryBeanExmCourseInfo queryBeanExmCourseInfo);
    /**
     * 更新试题科目分类表
     * @param exmCourseInfo
     * @return
     */
    boolean updateExmCourseInfo(ExmCourseInfo exmCourseInfo);
    /**
     * 删除试题科目分类表
     * @param id
     * @return
     */
    boolean deleteExmCourseInfo(String id);
}
