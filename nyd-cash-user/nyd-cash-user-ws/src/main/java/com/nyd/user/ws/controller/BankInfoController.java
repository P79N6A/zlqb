package com.nyd.user.ws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.ChannelBankData;
import com.nyd.user.model.ChannelBankInfo;
import com.nyd.user.service.BankInfoService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/1.
 * 银行卡接口
 */
@RestController
@RequestMapping("/user")
public class BankInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(BankInfoController.class);

    @Autowired
    private BankInfoService bankInfoService;

    @RequestMapping(value = "/bank/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveBank(@RequestBody BankInfo bankInfo) throws Throwable{
        ResponseData responseData = bankInfoService.saveBankInfo(bankInfo);
        return responseData;
    }
    @RequestMapping(value = "/bank/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBankList(@RequestBody BankInfo bankInfo) throws Throwable{
    	LOGGER.info("查询银行卡列表请求信息：{}",JSON.toJSONString(bankInfo));
    	ResponseData responseData = bankInfoService.getBankList(bankInfo);
    	return responseData;
    }
    
    @RequestMapping(value = "/bank/xunlianList", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBankXunlianList(@RequestBody BankInfo bankInfo) throws Throwable{
    	LOGGER.info("查询银行卡列表请求信息：{}",JSON.toJSONString(bankInfo));
    	ResponseData responseData = bankInfoService.getXunlianBankList(bankInfo);
    	return responseData;
    }

    @RequestMapping(value = "/bankCard/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBankListNew(@RequestBody BankInfo bankInfo) throws Throwable{
        LOGGER.info("查询银行卡列表请求信息：{}",JSON.toJSONString(bankInfo));
        ResponseData responseData = bankInfoService.getBankList(bankInfo);
        return responseData;
    }


    @RequestMapping(value = "/bank-list/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchBankList(@RequestBody BaseInfo baseInfo) throws Throwable{
        if(baseInfo.getUserId() == null){
            return ResponseData.success();
        }else{
            return bankInfoService.getBankInfos(baseInfo.getUserId());
        }
    }
    
	@RequestMapping(value = "/bank/list/v2", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<List<ChannelBankInfo>> getBankListV2() throws Throwable {
		return bankInfoService.getBankListV2();
	}

	@RequestMapping(value = "/bank/channel", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<ChannelBankData> getBankChannel(
			@RequestBody ChannelBankInfo info) throws Throwable {
		return bankInfoService.getBankChannel(info);
	}

	@RequestMapping(value = "/bank/channel/count", method = RequestMethod.POST, produces = "application/json")
	public String getBankChannel(
			@RequestParam(value = "bankName", required = true) String bankName,
			@RequestParam(value = "count", required = true) int count)
			throws Throwable {

		ChannelBankInfo info = new ChannelBankInfo();
		info.setBankName(bankName);

		List<ChannelBankData> resList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			resList.add(bankInfoService.getBankChannel(info).getData());
		}

		JSONObject json = new JSONObject();

		Map<String, List<ChannelBankData>> map = resList.stream().collect(
				Collectors.groupingBy(data -> data.getChannelName()));
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			json.put(key, map.getOrDefault(key, new ArrayList<>()).size());
		}
		return JSONObject.toJSONString(json);
	}
}
