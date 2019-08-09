package com.nyd.zeus.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFile;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileSearchForm;


/**
 * FuiouConfigFile Mapper
 *
 */
@Mapper
public interface PayConfigFileMapper {
	
	List<PayConfigFile> pageData(PayConfigFileSearchForm  form) throws Exception;
}
