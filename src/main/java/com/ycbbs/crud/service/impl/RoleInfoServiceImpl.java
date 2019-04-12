package com.ycbbs.crud.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.RoleInfo;
import com.ycbbs.crud.entity.RolePermissionInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanRoleInfo;
import com.ycbbs.crud.entity.querybean.SaveBeanRolePermission;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.mapper.RoleInfoMapper;
import com.ycbbs.crud.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 角色业务类
 */
@Service
public class RoleInfoServiceImpl implements RoleInfoService {
    @Autowired
    private RoleInfoMapper roleInfoMapper;

    /**
     * 分页查询
     * @param queryBeanRoleInfo
     * @return
     * @throws CustomException
     */
    @Override
    public PageInfo<RoleInfo> getRoleInfoPageList(QueryBeanRoleInfo queryBeanRoleInfo) throws CustomException {
        PageHelper.startPage(queryBeanRoleInfo.getPageNum(), queryBeanRoleInfo.getPageSize());
        List<RoleInfo> list = this.getRoleInfoList(queryBeanRoleInfo);
        PageInfo<RoleInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
    /**
     * 不带分页查询
     * @param queryBeanRoleInfo
     * @return
     * @throws CustomException
     */
    @Override
    public List<RoleInfo> getRoleInfoList(QueryBeanRoleInfo queryBeanRoleInfo) throws CustomException {

        if(null == queryBeanRoleInfo){
            return null;
        }

        List<RoleInfo> roleInfos = roleInfoMapper.getRolePermissionList(queryBeanRoleInfo);

//        Example example = new Example(RoleInfo.class);
//        if(null != queryBeanRoleInfo){
//            Example.Criteria criteria = example.createCriteria();
//            //必须字段名以及搜索的值都不为空
//            if (null != queryBeanRoleInfo.getKeyword() && !"".equals(queryBeanRoleInfo.getKeyword())) {
//                criteria.andLike("name","%" + queryBeanRoleInfo.getKeyword()+ "%");
//            }
//            if (null != queryBeanRoleInfo.getDeleted() && !"".equals(queryBeanRoleInfo.getDeleted())) {
//                criteria.andLike("deleted","%" + queryBeanRoleInfo.getDeleted()+ "%");
//            }
//        }
//        List<RoleInfo> roleInfos = roleInfoMapper.selectByExample(example);
//        roleInfos.stream().forEach(roleInfo -> {
//            //获取权限
//            List<PermissionInfo> permissionList = roleInfoMapper.getByRolePermissionList(roleInfo.getId());
//            roleInfo.setPermissionInfos(permissionList);
//        });
        return roleInfos;
    }

    /**
     * 新增角色
     * @param roleInfo
     * @return
     * @throws CustomException
     */
    @Override
    public boolean insertRoleInfo(RoleInfo roleInfo) throws CustomException {
        String uuid = UUID.randomUUID().toString();
        int rows = roleInfoMapper.insertSelective(roleInfo.setId(uuid));
        return rows > 0;
    }

    /**
     * 修改/删除角色
     * @param roleInfo
     * @return
     * @throws CustomException
     */
    @Override
    public boolean updateRoleInfo(RoleInfo roleInfo) throws CustomException {
        int rows = roleInfoMapper.updateByPrimaryKeySelective(roleInfo);
        return rows > 0;
    }
    /**
     * 验证角色名是否存在(还要过滤自己原始角色名)
     * 登录时来根据角色名查询实体，直接将oldRoleName为空，validaNewRoleName值为当前角色名就可以查询到数据了
     * @param oldRoleName:原始角色名
     * @param validaNewRoleName:新角色名
     * @return
     */
    @Override
    public RoleInfo selectRoleInfoByUserName(String oldRoleName, String validaNewRoleName) {
        Example example = new Example(RoleInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("name",oldRoleName==null?"":oldRoleName.trim());
        criteria.andEqualTo("name",validaNewRoleName.trim());
        List<RoleInfo> roleInfos = roleInfoMapper.selectByExample(example);
        return roleInfos.size() >0 ? roleInfos.get(0) : null;
    }

    /**
     * 批量删除角色权限
     * @param roleid
     * @return
     * @throws CustomException
     */
    @Override
    public boolean deleteBatch(String roleid) throws CustomException {
        int rows = roleInfoMapper.deleteBatch(roleid);
        return rows > 0;
    }


    /**
     * 批量插入
     * @param saveBeanRolePermission
     * @return
     * @throws CustomException
     */
    @Override
    public boolean insertBatch(SaveBeanRolePermission saveBeanRolePermission) throws CustomException {

        if (null == saveBeanRolePermission.getRoleid() || "".equals(saveBeanRolePermission.getRoleid())) {
            return false;
        }
        //首先删除当前角色的权限
        int batch = roleInfoMapper.deleteBatch(saveBeanRolePermission.getRoleid());

        //在添加权限
        List<RolePermissionInfo> rolePermissionInfos = new ArrayList<>(10);
        saveBeanRolePermission.getPermissionInfoIds().stream()
                .forEach(permissionInfoId -> {
                    rolePermissionInfos.add(
                            new RolePermissionInfo()
                                    .setYcRoleId(saveBeanRolePermission.getRoleid())
                                    .setYcPermissionId(permissionInfoId)
                    );
                });
        int rows = roleInfoMapper.insertBatch(rolePermissionInfos);
        return rows > 0;
    }


}
