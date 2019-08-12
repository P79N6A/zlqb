package com.nyd.admin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.admin.service.RefundAppService;
import com.nyd.user.api.RefundAppContract;
import com.nyd.user.model.RefundAppInfo;
import com.tasfe.framework.support.model.ResponseData;

@Service
public class RefundAppServiceImpl implements RefundAppService {
	private static Logger logger = LoggerFactory.getLogger(RefundAppServiceImpl.class);

	@Autowired
	RefundAppContract refundAppContract;

	@Override
	public ResponseData save(RefundAppInfo req) {
		ResponseData res = ResponseData.success();
		try {
			refundAppContract.save(req);
		} catch (Exception e) {
			logger.info("保存口子信息失败：" + e.getMessage());
			return res.error("保存失败！");
		}
		return res;
	}

	@Override
	public ResponseData update(RefundAppInfo req) {
		ResponseData res = ResponseData.success();
		try {
			refundAppContract.update(req);
		} catch (Exception e) {
			logger.info("更新口子信息失败：" + e.getMessage());
			return res.error("更新失败！");
		}
		return res;

	}

	@Override
	public ResponseData query(RefundAppInfo param) {
		ResponseData re = ResponseData.success();
		try {
			return refundAppContract.queryRefundApp(param);
		} catch (Exception e) {
			logger.error("查询统计信息失败： " + e.getMessage());
			return re.error("查询详情失败");
		}
	}
}
