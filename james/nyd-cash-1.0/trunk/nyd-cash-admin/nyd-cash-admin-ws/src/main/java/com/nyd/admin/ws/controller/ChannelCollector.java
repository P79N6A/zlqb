package com.nyd.admin.ws.controller;
import com.nyd.admin.model.enums.ChannelEnum;
import com.nyd.admin.service.ChannelConfigService;
import com.nyd.order.model.ChannelInfo;
import com.tasfe.framework.support.model.ResponseData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peng
 * @create 2017-12-18 10:47
 **/
@RestController
@RequestMapping("/admin")
public class ChannelCollector {

	@Autowired
	ChannelConfigService channelConfigService;

    @RequestMapping("/channelQuery")
    @ResponseBody
    public ResponseData channelQuery() {
        return ResponseData.success(ChannelEnum.toMap());
    }
    @RequestMapping("/channelBankList")
    @ResponseBody
    public ResponseData getBankList() {
    	return channelConfigService.getBankList();
    }
    @RequestMapping(value="/channelConfigQuery", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData channelConfigQuery(@RequestBody ChannelInfo channelInfo) {
    	return channelConfigService.channelConfigQuery(channelInfo);
    }
    @RequestMapping(value="/getChannelConfigByCode", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getChannelConfigByCode(@RequestBody ChannelInfo channelInfo) {
    	return channelConfigService.getChannelConfigByCode(channelInfo);
    }
    @RequestMapping(value="/channelConfig/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData channelConfigUpdate(@RequestBody ChannelInfo channelInfo) {
    	return channelConfigService.channelConfigUpdate(channelInfo);
    }
    @RequestMapping(value="/channelConfig/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData channelConfigSave(@RequestBody ChannelInfo channelInfo) {
    	return channelConfigService.channelConfigSave(channelInfo);
    }
}
