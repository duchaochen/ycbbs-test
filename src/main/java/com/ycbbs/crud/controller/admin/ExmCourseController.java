package com.ycbbs.crud.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.ExmCourseInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanExmCourseInfo;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.ExmCourseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
///ycbbs
@RequestMapping("/exmcourse")
public class ExmCourseController {
    @Autowired
    private ExmCourseService exmCourseService;

    /**
     * 查询试题科目分类表（分页）
     * @param queryBeanExmCourseInfo
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/list")
    @RequiresPermissions("exmcourse:query")
    public PageInfo<ExmCourseInfo> selectKeyAll(QueryBeanExmCourseInfo queryBeanExmCourseInfo)
            throws Exception {
        PageInfo<ExmCourseInfo> exmCourseInfoPageInfo = exmCourseService.selectKeyAll(queryBeanExmCourseInfo);
        return exmCourseInfoPageInfo;
    }

    /**
     * 查询试题科目分类表
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/all")
    public YcBbsResult selectAll() throws Exception {
        List<ExmCourseInfo> exmCourseInfos = exmCourseService.selectAll(null);
        return YcBbsResult.build(200,"新增成功",exmCourseInfos);
    }

    /**
     * 添加试题科目分类表
     * @param exmCourseInfo
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/add")
    @RequiresPermissions("exmcourse:create")
    public YcBbsResult addPermission(@RequestBody ExmCourseInfo exmCourseInfo) throws Exception {
        boolean b = exmCourseService.insertExmCourseInfo(exmCourseInfo);
        if (b) {
            return YcBbsResult.build(200,"新增成功");
        }
        return YcBbsResult.build(200,"新增失败");
    }

    /**
     * 添加试题科目分类表
     * @param exmCourseInfo
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/update")
    @RequiresPermissions("exmcourse:update")
    public YcBbsResult updatePermission(@RequestBody ExmCourseInfo exmCourseInfo) throws Exception {
        boolean b = exmCourseService.updateExmCourseInfo(exmCourseInfo);
        if (b) {
            return YcBbsResult.build(200,"更新成功");
        }
        return YcBbsResult.build(200,"更新失败");
    }

    /**
     * 删除权限
     * @param id
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/delete")
    @RequiresPermissions("exmcourse:delete")
    public YcBbsResult deletePermission(String id ) throws Exception {
        if(null == id){
            return YcBbsResult.build(500,"无法找到要删除的对应项!!!");
        }
        boolean b = exmCourseService.deleteExmCourseInfo(id);
        if (b) {
            return YcBbsResult.build(200,"删除成功");
        }
        return YcBbsResult.build(200,"删除失败");
    }
}
