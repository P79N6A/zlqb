package com.nyd.admin.ws.controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.OrderExceptionReportVo;
import com.nyd.admin.model.RemitReportVo;
import com.nyd.admin.model.Info.OrderExceptionRequst;
import com.nyd.admin.service.RemitResendService;
import com.nyd.admin.service.UrgentDeductMoneyService;
import com.nyd.admin.service.excel.ExcelKit;
import com.nyd.order.model.OrderExceptionInfo;
import com.tasfe.framework.support.model.ResponseData;

@RestController
@RequestMapping(value="/admin")
public class RemitFailController {
	
	public static Logger logger =LoggerFactory.getLogger(RemitFailController.class);
	 
	@Autowired
	private RemitResendService  remitResendService;
	
	
	@RequestMapping(value="/remitResendAudit", method = RequestMethod.POST)
	public ResponseData remitResendAudit(@RequestBody OrderExceptionRequst req) {
		return remitResendService.audit(req);
		
		
	}
	@RequestMapping(value="/remitResendQuery", method = RequestMethod.POST)
	public ResponseData remitResendQuery(@RequestBody OrderExceptionRequst req) throws Exception{
		return remitResendService.query(req);
	}
	@RequestMapping(value="/remitResendExport", method = RequestMethod.GET)
	public void remitResendExport(String startDate,String endDate,String auditStatu,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		/*map.put("pageNum", req.getPageNum());
		map.put("pageSize", req.getPageSize());*/
		if(auditStatu != null && auditStatu != "") {
			map.put("auditStatus", Integer.parseInt(auditStatu));
		}
		ResponseData<List<OrderExceptionInfo>> resp = remitResendService.export(map);
		List<OrderExceptionInfo> list = resp.getData();
		List<OrderExceptionReportVo> re = new ArrayList<OrderExceptionReportVo>();
		for(OrderExceptionInfo info : list) {
			OrderExceptionReportVo vo = new OrderExceptionReportVo();
			BeanUtils.copyProperties(info, vo);
			re.add(vo);
		}
		ExcelKit.$Export(OrderExceptionReportVo.class, response)
        .toExcel(re, "remit_fail_list");
	}

}
