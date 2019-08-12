package com.nyd.settlement.ws.controller;

import com.nyd.settlement.model.dto.PwdDto;
import com.nyd.settlement.service.PwdService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
@RestController
@RequestMapping("/settlement")
public class PwdController {
    @Autowired
    private PwdService pwdService;

    Logger logger = LoggerFactory.getLogger(PwdController.class);

    @RequestMapping(value = "/bucklePwd", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryNoReFund(@RequestBody PwdDto pwdDto) {
        logger.info("校验密码");
        pwdDto.setType(1);
        return pwdService.validatePwd(pwdDto);
    }
}
