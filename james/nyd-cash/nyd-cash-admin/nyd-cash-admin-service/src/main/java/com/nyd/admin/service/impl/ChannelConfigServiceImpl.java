package com.nyd.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nyd.admin.service.ChannelConfigService;
import com.nyd.order.api.OrderChannelContract;
import com.nyd.order.entity.ChannelProportionConfig;
import com.nyd.order.model.BankCodeInfo;
import com.nyd.order.model.ChannelInfo;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.model.BankInfo;
import com.tasfe.framework.support.model.ResponseData;


/**
 * 
 * @author zhangdk
 *
 */
@Service
public class ChannelConfigServiceImpl implements ChannelConfigService {

	@Autowired
	private OrderChannelContract orderChannelContract;
	
	@Autowired
	private UserBankContract userBankContract;

	@Override
	public ResponseData channelConfigQuery(ChannelInfo channelInfo) {
		ResponseData resp = ResponseData.success();
		if(!StringUtils.isBlank(channelInfo.getIfUseStr())) {
			channelInfo.setIfUse(Integer.parseInt(channelInfo.getIfUseStr()));
		}
		List<ChannelInfo> list = orderChannelContract.getAllChannelConfig(channelInfo);
		List<ChannelInfo> respL = new ArrayList<ChannelInfo>();
		if(list != null) {
			String bankStrs = null;
			ResponseData data = getBankList();
			List<BankCodeInfo> banks = new ArrayList<BankCodeInfo>();
			if(data != null && data.getStatus().equals("0")) {
				String json = JSON.toJSONString(data.getData());
				banks = JSON.parseArray(json, BankCodeInfo.class);
			}
			for(ChannelInfo info1 : list) {
				bankStrs = info1.getChannelBank();
				if(!StringUtils.isBlank(bankStrs)) {
					String[] split1 = bankStrs.split(";");
					List<BankCodeInfo> ll = new ArrayList<BankCodeInfo>();
					for(String code:split1) {
						for (BankCodeInfo in:banks) {
							if(in.getBankCode().equals(code)) {
								ll.add(in);
							}
						}
					}
					info1.setBankList(ll);
				}
				respL.add(info1);
			}
			
		}
		return resp.setData(JSONObject.parse(JSONArray.toJSONString(respL, SerializerFeature.DisableCircularReferenceDetect)));
	}
	
	@Override
	public ResponseData getBankList() {
		ResponseData re = userBankContract.getBankList(new BankInfo());
		return re;
	}

	@Override
	public ResponseData channelConfigSave(ChannelInfo channelInfo) {
		orderChannelContract.save(channelInfo);
		return null;
	}
	@Override
	public ResponseData getChannelConfigByCode(ChannelInfo channelInfo) {
		if(channelInfo == null || StringUtils.isBlank(channelInfo.getChannelCode())) {
			return ResponseData.error("请求参数异常！");
		}
		return orderChannelContract.getChannelByCode(channelInfo.getChannelCode());
	}

	@Override
	public ResponseData channelConfigUpdate(ChannelInfo channelInfo) {
		/*if(channelInfo != null && channelInfo.getChannelBanks()!= null ) {
			String banks = "";
			for(String bank : channelInfo.getChannelBanks()) {
				if(!StringUtils.isBlank(bank)) {
					banks += bank+";";
				}
			}
			channelInfo.setChannelBank(banks);
		}*/
		/*if(channelInfo != null && channelInfo.getChannelBank() == null) {
			channelInfo.setChannelBank("");
		}*/
		if("s".equals(channelInfo.getMaxAmountStr())) {
			channelInfo.setMaxAmount(null);
		}
		orderChannelContract.update(channelInfo);
		return ResponseData.success();
	}
	
	public static void main(String[] args) {
		ChannelInfo info = new ChannelInfo();
		info.setChannelCode("渠道编码");
		info.setChannelLimit(100);
		info.setChannelName("渠道名称");
		info.setChannelRatio(20);
		info.setCloseTimes("11:00:00-12:00:00;");
		info.setIfRisk(1);
		info.setIfUse(1);
		info.setLimitUse(2);
		info.setRatioUse(3);
		System.out.println(JSON.toJSONString(info));
		
		System.out.println(JSON.toJSONString(ResponseData.success()));
		ResponseData re = ResponseData.success();
		System.out.println(JSON.toJSONString(re));
		List<ChannelInfo> list = new ArrayList<ChannelInfo>();
		list.add(info);
		re.setData(list);
		System.out.println(JSON.toJSONString(re));
	}
}
