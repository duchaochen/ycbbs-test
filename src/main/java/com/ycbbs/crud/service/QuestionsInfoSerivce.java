package com.ycbbs.crud.service;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.QuestionsInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanQuestionsInfo;
import com.ycbbs.crud.exception.CustomException;

import java.util.List;

public interface QuestionsInfoSerivce {
    /**
     * 获取题目类型基本信息表信息(分页)
     * @param queryBeanQuestionsInfo
     * @return
     * @throws Exception
     */
    PageInfo<QuestionsInfo> getQuestionsInfoPager(QueryBeanQuestionsInfo queryBeanQuestionsInfo)throws CustomException;
    /**
     * 获取题目类型基本信息表信息
     * @param queryBeanQuestionsInfo
     * @return
     * @throws Exception
     */
    List<QuestionsInfo> getQuestionsInfoList(QueryBeanQuestionsInfo queryBeanQuestionsInfo) throws CustomException;
    /**
     * 添加题目类型基本信息表
     * @param questionsInfo
     * @return
     * @throws CustomException
     */
    boolean insertQuestionsInfo(QuestionsInfo questionsInfo) throws CustomException;
    /**
     * 修改题目类型基本信息表
     * @param questionsInfo
     * @return
     * @throws CustomException
     */
    boolean updateQuestionsInfo(QuestionsInfo questionsInfo) throws CustomException;
}
