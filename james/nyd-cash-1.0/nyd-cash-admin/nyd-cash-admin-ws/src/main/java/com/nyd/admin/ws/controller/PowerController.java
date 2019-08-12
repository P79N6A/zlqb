package com.nyd.admin.ws.controller;

import com.nyd.admin.model.power.dto.PowerDto;
import com.nyd.admin.service.PowerService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peng
 * @create 2018-01-03 15:30
 **/
@RestController
@RequestMapping("/admin/power")
public class PowerController {
    @Autowired
    PowerService powerService;
    @RequestMapping("/create")
    @ResponseBody
    public ResponseData createPower(@RequestBody PowerDto dto){
        return powerService.savePower(dto);
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResponseData updatePower(@RequestBody PowerDto dto){
        return powerService.updatePower(dto);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData deletePower(@RequestBody PowerDto dto){
        return powerService.deletePower(dto);
    }

    @RequestMapping("/query")
    @ResponseBody
    public ResponseData queryPower(){
        return powerService.queryPower();
    }
}
