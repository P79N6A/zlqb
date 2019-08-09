package com.nyd.admin.ws.controller;

import com.nyd.admin.model.KzjrProductConfigVo;
import com.nyd.admin.model.KzjrQueryVo;
import com.nyd.admin.service.KzjrService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dengw on 2017/12/15
 */
@RestController
@RequestMapping("/admin/kzjr")
public class KzjrController {
    @Autowired
    KzjrService kzjrService;

    /**
     * 空中金融保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveKzjr(@RequestBody KzjrProductConfigVo vo){
        boolean flag = kzjrService.saveKzjr(vo);
        if(flag) {
            return ResponseData.success();
        }else {
            return ResponseData.error();
        }
    }

    /**
     * 空中金融更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateKzjr(@RequestBody KzjrProductConfigVo vo){
        boolean flag = kzjrService.updateKzjr(vo);
        if(flag) {
            return ResponseData.success();
        }else {
            return ResponseData.error();
        }
    }

    /**
     * 空中金融查询
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryKzjr(@RequestBody KzjrQueryVo vo){
        try {
            return ResponseData.success(kzjrService.findPage(vo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }
}
