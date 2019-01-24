package com.ycbbs.crud.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registers")
public class UserInfoController {

    @PostMapping("/toregister")
    public String toRegist() {
        return "toRegist";
    }

    @PostMapping("/register")
    public String regist() {
        return "regist";
    }

}
