package com.nyd.capital.service.dld.service;

public interface IbankDeviceContract {

	/**
	 * 获取银码头客户身份证下载地址
	 * @param userId
	 * @param type
	 * @return
	 */
	String getAttachmentModelUrl(String userId, String type);
}
