package com.nyd.admin.ws.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.admin.model.RefundOrderExportVo;
import com.nyd.admin.model.RefundOrderInfo;
import com.nyd.admin.service.RefundOrderService;
import com.nyd.admin.service.excel.ExcelKit;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/admin")
public class RefundOrderCollector {

	@Autowired
	RefundOrderService refundOrderService;

	@RequestMapping(value = "/refund/order/Query", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData refundQuery(@RequestBody RefundOrderInfo req) {
		return refundOrderService.query(req);
	}
	
	@RequestMapping(value="/refund/order/export", method = RequestMethod.GET)
	public void remitResendExport(RefundOrderInfo req,HttpServletResponse response) {
		ResponseData<List<RefundOrderInfo>> resp = refundOrderService.queryExport(req);
		List<RefundOrderInfo> list = resp.getData();
		List<RefundOrderExportVo> re = new ArrayList<RefundOrderExportVo>();
		for(RefundOrderInfo info : list) {
			RefundOrderExportVo vo = new RefundOrderExportVo();
			BeanUtils.copyProperties(info, vo);
			re.add(vo);
		}
		ExcelKit.$Export(RefundOrderExportVo.class, response)
        .toExcel(re, "refund_order_list");
	}
}
