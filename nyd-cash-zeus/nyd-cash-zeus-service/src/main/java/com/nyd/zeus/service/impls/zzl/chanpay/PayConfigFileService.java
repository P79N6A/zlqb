package com.nyd.zeus.service.impls.zzl.chanpay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nyd.order.model.common.BeanCommonUtils;
import com.nyd.zeus.dao.mapper.PayConfigFileMapper;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFile;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileSearchForm;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;

import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
public class PayConfigFileService {

	@Autowired
    private PayConfigFileMapper mapper;

	
	public List<PayConfigFileVO> queryByCodeId(String code){
		PayConfigFileSearchForm form = new PayConfigFileSearchForm();
		try {
			form.setPage(1);
			form.setRows(100);
			form.setCode(code);
			List<PayConfigFile> list = mapper.pageData(form);
			return BeanCommonUtils.copyListProperties(list, PayConfigFileVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询畅捷配置文件异常" + e);
		}
		return null;
	}
	
}
