package com.nyd.user.api.zzl;

import java.util.List;

import com.nyd.user.entity.Contact;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.CallRecordVO;
import com.nyd.user.model.vo.CustInfoQueryVO;

public interface UserForZLQServise {
	public List<Contact> findByUserId(String userId);
	public List<Contact> findContactByUserId(String userId);
	
	/**
	 * 获取手机通话记录
	 */
	public PagedResponse<List<CallRecordVO>> queryCallRecord(CustInfoQueryVO vo);
	

}
