package com.ycbbs.crud.controller;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.UserInfoService;
import com.ycbbs.crud.utils.AccountValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    /**
     * 注册
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @GetMapping("/list")
    public PageInfo<UserInfo> register(String keyname,int pageIndex,int pageSize) throws CustomException {
        PageInfo<UserInfo> pageInfo = userInfoService.selectKeyAll(keyname, pageIndex, pageSize);
        return pageInfo;
    }
}
