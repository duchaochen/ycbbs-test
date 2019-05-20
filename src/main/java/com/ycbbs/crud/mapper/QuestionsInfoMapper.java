package com.ycbbs.crud.mapper;

import com.ycbbs.crud.entity.QuestionsInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanQuestionsInfo;
import com.ycbbs.crud.exception.CustomException;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface QuestionsInfoMapper extends Mapper<QuestionsInfo> {
    /**
     * 获取题目类型基本信息表信息
     * @param queryBeanQuestionsInfo
     * @return
     * @throws Exception
     */
    List<QuestionsInfo> getQuestionsInfoList(QueryBeanQuestionsInfo queryBeanQuestionsInfo) throws CustomException;
}
