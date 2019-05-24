package com.ycbbs.crud.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.QuestionsInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanQuestionsInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.QuestionsInfoSerivce;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
///ycbbs
@RestController
@RequestMapping("/questionsInfo")
public class QuestionsInfoController {
    /**
     * 题目类型基本信息数据访问
     */
    @Autowired
    private QuestionsInfoSerivce questionsInfoSerivce;
    /**
     * 获取题目类型基本信息表信息(分页)
     * @param queryBeanQuestionsInfo
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/list")
    @RequiresPermissions("questions:query")
    public PageInfo<QuestionsInfo> getQuestionsInfoPager(QueryBeanQuestionsInfo queryBeanQuestionsInfo)
            throws CustomException {
        return questionsInfoSerivce.getQuestionsInfoPager(queryBeanQuestionsInfo);
    }
    /**
     * 添加题目类型基本信息表
     * @param questionsInfo
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @PostMapping("/add")
    @RequiresPermissions("questions:create")
    public YcBbsResult insertQuestionsInfo(@RequestBody QuestionsInfo questionsInfo)
            throws CustomException {
        boolean insertOk = questionsInfoSerivce.insertQuestionsInfo(questionsInfo);
        if (insertOk) {
            return YcBbsResult.build(200,"新增成功");
        }else {
            return YcBbsResult.build(200,"新增失败");
        }
    }
    /**
     * 修改题目类型基本信息表
     * @param questionsInfo
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @PostMapping("/update")
    @RequiresPermissions("questions:update")
    public YcBbsResult updateQuestionsInfo(@RequestBody QuestionsInfo questionsInfo)
            throws CustomException {
        boolean insertOk = questionsInfoSerivce.updateQuestionsInfo(questionsInfo);
        if (insertOk) {
            return YcBbsResult.build(200,"修改成功");
        }else {
            return YcBbsResult.build(200,"修改失败");
        }
    }
}
