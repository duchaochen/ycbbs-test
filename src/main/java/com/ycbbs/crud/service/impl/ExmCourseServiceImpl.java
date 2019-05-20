package com.ycbbs.crud.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.ExmCourseInfo;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanExmCourseInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.mapper.ExmCourseMapper;
import com.ycbbs.crud.service.ExmCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

@Service
public class ExmCourseServiceImpl implements ExmCourseService {

    @Autowired
    private ExmCourseMapper exmCourseMapper;
    /**
     * 添加试题科目表（Java,C#,Python）信息
     * @param exmCourseInfo
     * @return
     * @throws CustomException
     */
    @Override
    public boolean insertExmCourseInfo(ExmCourseInfo exmCourseInfo) throws CustomException {
        //用来生成数据库的主键id非常不错。
        String courseId = UUID.randomUUID().toString();
        exmCourseInfo.setCourseId(courseId);
        //暂时设置为全部都是激活状态,如果实行激活操作这里直接去掉
        int rows = exmCourseMapper.insertSelective(exmCourseInfo);
        if (rows == 0) {
            throw new CustomException("系统错误，添加试题科目分类失败!!!");
        }
        return true;
    }
    /**
     * 查询试题科目分类表（分页）
     * @param queryBeanExmCourseInfo
     * @return
     */
    @Override
    public PageInfo<ExmCourseInfo> selectKeyAll(QueryBeanExmCourseInfo queryBeanExmCourseInfo) {
        PageHelper.startPage(queryBeanExmCourseInfo.getPageNum(), queryBeanExmCourseInfo.getPageSize());
        //查询所有书籍
        List<ExmCourseInfo> userInfos = this.selectAll(queryBeanExmCourseInfo);

        PageInfo<ExmCourseInfo> pageInfo = new PageInfo<>(userInfos);
        return pageInfo;
    }
    /**
     * 查询试题科目分类表
     * @param queryBeanExmCourseInfo
     * @return
     */
    @Override
    public List<ExmCourseInfo> selectAll(QueryBeanExmCourseInfo queryBeanExmCourseInfo) {
        Example example = new Example(ExmCourseInfo.class);
        if (null != queryBeanExmCourseInfo) {
            if (null != queryBeanExmCourseInfo.getKeyword() && !"".equals(queryBeanExmCourseInfo.getKeyword())) {
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike("courseName","%"+queryBeanExmCourseInfo.getKeyword()+"%")
                        .orLike("courseType","%"+queryBeanExmCourseInfo.getKeyword()+"%");
            }
        }
        List<ExmCourseInfo> exmCourseInfos = exmCourseMapper.selectByExample(example);
        return exmCourseInfos;
    }
    /**
     * 更新试题科目分类表
     * @param exmCourseInfo
     * @return
     */
    @Override
    public boolean updateExmCourseInfo(ExmCourseInfo exmCourseInfo) {
        int rows = exmCourseMapper.updateByPrimaryKeySelective(exmCourseInfo);
        return rows > 0;
    }
    /**
     * 删除试题科目分类表
     * @param id
     * @return
     */
    @Override
    public boolean deleteExmCourseInfo(String id) {
        int rows = exmCourseMapper.deleteByPrimaryKey(id);
        return rows > 0;
    }
}
