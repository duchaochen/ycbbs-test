package com.ycbbs.crud.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.RolePermissionInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanPermission;
import com.ycbbs.crud.entity.querybean.SaveBeanRolePermission;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.mapper.PermissionInfoMapper;
import com.ycbbs.crud.service.PermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermissionInfoServiceImpl implements PermissionInfoService {
    @Autowired
    private PermissionInfoMapper permissionInfoMapper;
    /**
     * 获取menu菜单
     * @param uid
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionInfo> getMenuInfo(String uid) throws CustomException {
        return permissionInfoMapper.getMenuInfo(uid);
    }
    /**
     * 获取可操作权限
     * @param uid
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionInfo> getPermissionInfo(String uid) throws CustomException {
        return permissionInfoMapper.getPermissionInfo(uid);
    }
    /**
     * 获取查询数据（带分页）
     * @param queryBeanPermission
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<PermissionInfo> getPermissionKeyList(QueryBeanPermission queryBeanPermission) throws CustomException {
        PageHelper.startPage(queryBeanPermission.getPageNum(), queryBeanPermission.getPageSize());
        List<PermissionInfo> list = this.getPermissionList(queryBeanPermission);
        PageInfo<PermissionInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
    /**
     * 获取查询数据
     * @param queryBeanPermission
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionInfo> getPermissionList(QueryBeanPermission queryBeanPermission) throws CustomException {
        Example example = new Example(PermissionInfo.class);
        if(null != queryBeanPermission){
            Example.Criteria criteria = example.createCriteria();
            //必须字段名以及搜索的值都不为空
            if (null != queryBeanPermission.getKeyword() && !"".equals(queryBeanPermission.getKeyword())
                    && null != queryBeanPermission.getFieldname() && !"".equals(queryBeanPermission.getFieldname())) {
                criteria.andLike(queryBeanPermission.getFieldname(),"%" + queryBeanPermission.getKeyword()+ "%");
            }
            if (null != queryBeanPermission.getType() && !"".equals(queryBeanPermission.getType())) {
                criteria.andLike("type","%" + queryBeanPermission.getType()+ "%");
            }
        }
        List<PermissionInfo> permissionInfos = permissionInfoMapper.selectByExample(example);
        return permissionInfos;
    }
    /**
     * 获取所有的menu
     * @return
     * @throws CustomException
     */
    @Override
    public List<PermissionInfo> getPermissionMenuList() throws CustomException {
        List<PermissionInfo> permissionList = this.getPermissionList(new QueryBeanPermission());
        List<PermissionInfo> collect = permissionList.stream()
                .filter(p -> "1".equals(p.getMenued()) || "0".equals(p.getParentid()))
                .collect(Collectors.toList());
        return collect;
    }
    /**
     * 添加权限数据
     * @param permissionInfo
     * @return
     * @throws CustomException
     */
    @Override
    public boolean insertPermission(PermissionInfo permissionInfo) throws CustomException {
        int rows = permissionInfoMapper.insertSelective(permissionInfo);
        return rows > 0;
    }
    /**
     * 修改权限数据(包括删除)
     * @param permissionInfo
     * @return
     * @throws CustomException
     */
    @Override
    public boolean updatePermission(PermissionInfo permissionInfo) throws CustomException {
        int rows = permissionInfoMapper.updateByPrimaryKeySelective(permissionInfo);
        return rows > 0;
    }

}
