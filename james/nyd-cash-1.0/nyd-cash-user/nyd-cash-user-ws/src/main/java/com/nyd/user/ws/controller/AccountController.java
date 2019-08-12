package com.nyd.user.ws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.User;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.UserDto;
import com.nyd.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.user.model.UserBindCardReq;
import com.nyd.user.service.AccountInfoService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/user")
public class AccountController {
	private static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountInfoService accountInfoService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/account/convert2Redis", method = RequestMethod.POST, produces = "application/json")
	public ResponseData convert2Redis(@RequestBody Map<String, Object> req) throws Throwable {
		if(req == null || req.isEmpty()) {
			req.put("minId", Long.parseLong("1"));
		}else {
			req.put("minId", Long.parseLong(req.get("minId").toString()));
			req.put("maxId", Long.parseLong(req.get("maxId").toString()));
		}
		accountInfoService.convertAccountInRedis(req);
		return ResponseData.success();
	}

	@RequestMapping(value = "/account/accountNumber", method = RequestMethod.POST, produces = "application/json")
	public ResponseData queryAccountByAccountNumber(@RequestBody UserDto userDto) throws Throwable {
        ResponseData responseData = ResponseData.success();
		List<Account> accounts = accountInfoService.findByAccountNumber(userDto.getAccountNumber());
		return responseData.setData(accounts);
	}

    @RequestMapping(value = "/userId", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryUserByUserId(@RequestBody UserDto userDto) throws Throwable {
        List<UserInfo> users = new ArrayList<>();
        ResponseData responseData = ResponseData.success();
        List<Account> accounts = accountInfoService.findByAccountNumber(userDto.getAccountNumber());
        LOGGER.info("accounts request params : " + JSON.toJSONString(accounts));
        if(accounts != null && accounts.size() > 0) {
            Account account = accounts.get(0);
            LOGGER.info("account request params : " + JSON.toJSONString(account));
            users = userService.findByUserId(account.getUserId());
            LOGGER.info("users request params : " + JSON.toJSONString(users));
        }
        return responseData.setData(users);
    }
}
