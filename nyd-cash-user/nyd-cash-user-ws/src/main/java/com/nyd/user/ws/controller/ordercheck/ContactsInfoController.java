package com.nyd.user.ws.controller.ordercheck;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.user.api.zzl.UserForZLQServise;
import com.nyd.user.entity.Contact;
import com.nyd.user.model.ordercheck.ContactsInfoVo;
import com.nyd.user.model.ordercheck.UserNoParam;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/user")
public class ContactsInfoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContactsInfoController.class);
	
	 @Autowired
	 private UserForZLQServise userForZLQServise;
	 /**
     * 联系人信息
     *
     * @param userNoParam
     * @return ResponseData
     */
    @RequestMapping(value = "/contactsInfo", method = RequestMethod.POST, produces = "application/json")
    public ResponseData contactsInfo(@RequestBody UserNoParam userNoParam) throws Throwable {
    	ResponseData responseData = new ResponseData();
    	ContactsInfoVo resp = new ContactsInfoVo();
    	if(userNoParam.getUserId() == null){
            return responseData.success();
        }else{
        	
        	List<Contact> Contact= userForZLQServise.findByUserId(userNoParam.getUserId());
        	resp.setUserid(Contact.get(0).getUserId());
        	resp.setContactPhone(Contact.get(0).getMobile()+"("+   Contact.get(0).getMobile()  +")");
        	resp.setContactName(Contact.get(0).getName());
        	resp.setContactRelation(Contact.get(0).getRelationship()+"("+Contact.get(0).getType()+")");
        	responseData.setData(resp);
        }
    	return responseData;
    }
   
	
}
