package com.nyd.user.ws.controller;

import com.nyd.user.api.DrainageChannelService;
import com.nyd.user.entity.UserDrainageChannel;
import com.nyd.user.model.vo.DrainageChannelCheckVo;
import com.nyd.user.model.vo.UserDrainageChannelVo;
import com.tasfe.framework.support.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 引流渠道
 * @author san
 * @version V1.0
 */
@RestController
@RequestMapping("/user/drainage")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DrainageChannelController {

    private final DrainageChannelService drainageChannelService;

    /** 检查渠道状态 */
    @RequestMapping(value = "check/state" ,method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseData<UserDrainageChannel> checkState(@RequestBody DrainageChannelCheckVo drainageChannelCheckVo) {
        return drainageChannelService.checkState(drainageChannelCheckVo);
    }

    @RequestMapping(value = "save" ,method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseData<String> checkState(@RequestBody UserDrainageChannelVo userDrainageChannelVo) {
        return drainageChannelService.save(userDrainageChannelVo);
    }

    @RequestMapping("/test")
    public String test(){
        return "测试";
    }
}
