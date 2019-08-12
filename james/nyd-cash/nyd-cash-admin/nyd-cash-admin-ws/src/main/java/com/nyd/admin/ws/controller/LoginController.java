package com.nyd.admin.ws.controller;

import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.service.UserService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peng
 * @create 2017-12-19 15:12
 **/
@RestController
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    UserService userService;

    /**
     * 登录
     * @param userDto
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResponseData login(@RequestBody UserDto userDto) {
        if(userDto.getAccountNo() == null || "".equals(userDto.getAccountNo())){
            return ResponseData.error();
        }else if(userDto.getPassword() == null || "".equals(userDto.getPassword())){
            return ResponseData.error();
        }
        return userService.getUserInfo(userDto);
    }
}
