package com.ycbbs.crud.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.QuestionsInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanQuestionsInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.mapper.QuestionsInfoMapper;
import com.ycbbs.crud.service.QuestionsInfoSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionsInfoSerivceImpl implements QuestionsInfoSerivce {
    /**
     * 题目类型基本信息数据访问
     */
    @Autowired
    private QuestionsInfoMapper questionsInfoMapper;
    /**
     * 获取题目类型基本信息表信息(分页)
     *
     * @param queryBeanQuestionsInfo
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<QuestionsInfo> getQuestionsInfoPager(QueryBeanQuestionsInfo queryBeanQuestionsInfo)
            throws CustomException {
        PageHelper.startPage(queryBeanQuestionsInfo.getPageNum(), queryBeanQuestionsInfo.getPageSize());
        List<QuestionsInfo> questionsInfoList = this.getQuestionsInfoList(queryBeanQuestionsInfo);
        PageInfo<QuestionsInfo> pageInfo = new PageInfo<>(questionsInfoList);
        return pageInfo;
    }
    /**
     * 获取题目类型基本信息表信息
     *
     * @param queryBeanQuestionsInfo
     * @return
     * @throws Exception
     */
    @Override
    public List<QuestionsInfo> getQuestionsInfoList(QueryBeanQuestionsInfo queryBeanQuestionsInfo)
            throws CustomException {
        List<QuestionsInfo> questionsInfoList = questionsInfoMapper.getQuestionsInfoList(queryBeanQuestionsInfo);
        return questionsInfoList;
    }
    /**
     * 添加题目类型基本信息表
     * @param questionsInfo
     * @return
     * @throws CustomException
     */
    @Override
    public boolean insertQuestionsInfo(QuestionsInfo questionsInfo) throws CustomException {
        //用来生成数据库的主键id非常不错。
        String courseId = UUID.randomUUID().toString();
        questionsInfo.setQuestionId(courseId);
        int rows = questionsInfoMapper.insertSelective(questionsInfo);
        return rows > 0;
    }
    /**
     * 修改题目类型基本信息表
     * @param questionsInfo
     * @return
     * @throws CustomException
     */
    @Override
    public boolean updateQuestionsInfo(QuestionsInfo questionsInfo) throws CustomException {
        int rows = questionsInfoMapper.updateByPrimaryKeySelective(questionsInfo);
        return rows > 0;
    }
}
