package com.nyd.admin.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.mapper.JiGuangPushMapper;
import com.nyd.admin.model.dto.JiGuangParamDto;
import com.nyd.admin.service.JiGuangDataService;
import com.tasfe.framework.support.model.ResponseData;

@Service(value="jiGuangDataService")
public class JiGuangDataServiceimpl implements JiGuangDataService{
	
	private static Logger logger = LoggerFactory.getLogger(JiGuangDataServiceimpl.class);
	
	@Autowired
	private JiGuangPushMapper jiGuangPushMapper;

	@Override
	public ResponseData gainJiGuangData(JiGuangParamDto dto) {	
		ResponseData responseData = ResponseData.success();
		if(dto == null || dto.getFlag() > 2 || dto.getFlag() == 0) {
			logger.info("参数异常");
			return ResponseData.error("参数异常");
		}
		Integer pageNum = dto.getPageNum();
        Integer pageSize = dto.getPageSize();
        //设置默认分页条件
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 1000;
        }
        Integer total = 0;
        List<String> resultList = null;
        try {
        	if(dto.getFlag() == 1) {//注册已登录但资料不完整的用户
            	total =	jiGuangPushMapper.findDataIncompleteCount(dto);
            	logger.info("资料不全用户 total is"+total);
            	PageHelper.startPage(pageNum, pageSize);
            	resultList =jiGuangPushMapper.findDataIncomplete(dto);          	
            	logger.info("资料不全的结果集为：" + resultList);
            }else if(dto.getFlag() == 2) {//放款成功的用户
            	total =jiGuangPushMapper.findLoanSuccessCount(dto);
            	logger.info("放款成功的用户 total is"+total);
            	PageHelper.startPage(pageNum, pageSize);
            	resultList = jiGuangPushMapper.findLoanSuccess(dto);
            	logger.info("放款成功的用户结果集为：" + resultList);
            }
        	 PageInfo pageInfo = new PageInfo(resultList);
             pageInfo.setTotal(total);
             responseData.setData(pageInfo);
             logger.info("result{}",JSON.toJSON(responseData));
        }catch(Exception e) {
        	 logger.error("极光推送查询异常！",e);
             return ResponseData.error("服务器开小差！");
        }      
		return responseData;
	}

}
