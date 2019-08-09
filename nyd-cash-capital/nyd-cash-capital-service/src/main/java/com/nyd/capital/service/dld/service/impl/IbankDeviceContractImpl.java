package com.nyd.capital.service.dld.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibank.application.api.DeviceInfoContract;
import com.nyd.capital.service.dld.service.IbankDeviceContract;

@Service
public class IbankDeviceContractImpl implements IbankDeviceContract{

	@Autowired
    private DeviceInfoContract deviceInfoContractYmt;
	@Override
	public String getAttachmentModelUrl(String userId, String type) {
		return deviceInfoContractYmt.getAttachmentModelUrl(userId,type);
	}

}
