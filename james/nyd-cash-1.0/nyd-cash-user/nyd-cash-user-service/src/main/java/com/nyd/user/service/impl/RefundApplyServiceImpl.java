package com.nyd.user.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.user.api.RefundApplyApi;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.dao.mapper.RefundApplyMapper;
import com.nyd.user.model.RefundApplyInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.enums.RefundApplyCode;
import com.nyd.user.service.RefundApplyService;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

import antlr.StringUtils;

/**
 * 
 * @author zhangdk
 *
 */
@Service(value = "refundApplyApi")
public class RefundApplyServiceImpl implements RefundApplyService, RefundApplyApi {
	private static Logger LOGGER = LoggerFactory.getLogger(RefundApplyServiceImpl.class);

	
    @Autowired
    private UserAccountContract userAccountContract;
	@Autowired
	RefundApplyMapper refundApplyMapper;
    @Autowired
    private IdGenerator idGenerator;
	
	@Override
	public ResponseData apply(RefundApplyInfo info) {
		info.setPhone(info.getAccountNumber());
		if(info.getTypeCode().equals("ref")) {
			List<RefundApplyInfo> list = refundApplyMapper.jugeList(info);
			if(list != null && list.size()>0) {
				return ResponseData.error("您的退款申请已提交！客服会在1-3个工作日内联系您，请等待审核");
			}
		}
		try {
			info.setStatus(10);
			info.setRequestNo(idGenerator.generatorId(BizCode.ORDER_NYD).toString());
			save(info);
		}catch(Exception ex) {
			LOGGER.error("客户申请入库失败：" + ex.getMessage());
			return ResponseData.error("客户申请入库失败");
		}
		return ResponseData.success("提交成功");
	}
	@Override
	public ResponseData save(RefundApplyInfo info) {
		try {
			refundApplyMapper.insert(info);
		}catch(Exception ex) {
			LOGGER.error("客户申请入库失败：" + ex.getMessage());
			return ResponseData.error("客户申请入库失败");
		}
		return ResponseData.success();
	}

	@Override
	public ResponseData update(RefundApplyInfo info) {
		if(info.getRequestNo() == null) {
			return ResponseData.error("请求参数异常");
		}
		try {
			refundApplyMapper.update(info);
		}catch(Exception ex) {
			LOGGER.error("客户申请更新失败：" + ex.getMessage());
			return ResponseData.error("客户申请更新失败");
		}
		return ResponseData.success();
	}

	@Override
	public ResponseData getRefundApplyList(RefundApplyInfo apply) {
		List<RefundApplyInfo> result = null;
		try {
			result = refundApplyMapper.getRefundApplyList(apply);
		}catch(Exception ex) {
			LOGGER.error("客户申请更新失败：" + ex.getMessage());
			return ResponseData.error("客户申请更新失败");
		}
		return ResponseData.success(result);
	}

}
