/*package com.nyd.user.ws.controller.ordercheck;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nyd.order.model.OrderCheckQuery;
import com.nyd.order.model.vo.OrderCheckVo;
import com.nyd.user.entity.Contact;
import com.nyd.user.model.UserInfo;
import com.nyd.user.service.UserService;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/order")
public class OrderCheckController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCheckController.class);
	
	@Autowired
    private UserService userService;
	
	 *//**
     * 订单审核详情
     *
     * @param orderCheckQuery
     * @return ResponseData
     *//*
    @RequestMapping(value = "/orderCheck/detail", method = RequestMethod.POST, produces = "application/json")
    public ResponseData orderCheck(@RequestBody OrderCheckQuery orderCheckQuery) throws Throwable {
        ResponseData responseData = new ResponseData();
        //OrderCheckVo resp = new OrderCheckVo();
        //List<UserInfo>  userinfo =userService
        
         * List<Contact> Contact= userForZLQServise.findByUserId(userNoParam.getUserid());
        	resp.setUserid(Contact.get(0).getUserId());
        	resp.setContactPhone(Contact.get(0).getMobile()+"("+   Contact.get(0).getMobile()  +")");
        	resp.setContactName(Contact.get(0).getName());
        	resp.setContactRelation(Contact.get(0).getRelationship()+"("+Contact.get(0).getType()+")");
        	responseData.setData(resp);
         
        return responseData;
    }
	
}
*/