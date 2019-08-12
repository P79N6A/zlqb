package com.nyd.user.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.user.api.zzl.UserBankCardService;
import com.nyd.user.model.vo.AppConfirmOpenVO;
import com.nyd.user.model.vo.AppPreCardVO;
import com.tasfe.framework.support.model.ResponseData;

@RestController
@RequestMapping("/user")
public class HlbBankInfoController {
	
	 @Autowired
	 private UserBankCardService userBankCardService;
	 /**
	 * 预绑卡
	 * @param vo
	 * @return
	 */
	 @RequestMapping(value = "/hlb/preBindingBank", method = RequestMethod.POST, produces = "application/json")
	 public ResponseData preBindingBank(@RequestBody AppPreCardVO vo){
		 return userBankCardService.preBindingBank(vo);
	 }
		
		/**
		 * 确认绑卡
		 * @param vo
		 * @return
		 */
	 @RequestMapping(value = "/hlb/confirmBindCard", method = RequestMethod.POST, produces = "application/json")
	 public ResponseData confirmBindCard(@RequestBody AppConfirmOpenVO vo){
		 return userBankCardService.confirmBindCard(vo);
	 }
}
