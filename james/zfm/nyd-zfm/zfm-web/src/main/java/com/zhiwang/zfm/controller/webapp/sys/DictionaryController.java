package com.zhiwang.zfm.controller.webapp.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.request.sys.SysDictionaryVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.service.api.sys.SysDictionaryService;


/**
 * 数据字典
 * 
 * @author huangzhenggang
 * @date 2018年5月14日
 */
@RestController
@RequestMapping("/api/sys/dictionary")
@Api(description = "数据字典")
public class DictionaryController {
	

	private Logger logger = LoggerFactory.getLogger(DictionaryController.class);

	@Autowired
	private SysDictionaryService sysDictionaryService;

	/*
	 * 根据条件查询
	 * zhenggang.Huang
	 */
	@ApiOperation(value = "数据字典-分页查询")
	@RequestMapping(value = "/queryPageDictionary", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public PagedResponse<List<SysDictionaryVO>> queryDictionaryList(@ModelAttribute SysDictionaryVO sysDictionaryRequest) {
		logger.info("数据字典-分页查询,入参：{}",JSONObject.toJSON(sysDictionaryRequest));
		return sysDictionaryService.queryDictionaryList(sysDictionaryRequest);
	}

	/*
	 * 新增，修改(id)，删除(状态改为0)
	 * zhenggang.Huang
	 */
	@ApiOperation(value = "数据字典-新增/修改(不能修改状态)")
	@RequestMapping(value = "/updateDictionary", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public CommonResponse<SysDictionaryVO> updateSysDictionary(SysDictionaryVO loanSysDictionaryEntityRequest) {
		logger.info("数据字典-新增/修改(不能修改状态),入参：{}",JSONObject.toJSON(loanSysDictionaryEntityRequest));
		// 保存
		if (ChkUtil.isEmpty(loanSysDictionaryEntityRequest.getId())) {
			return sysDictionaryService.save(loanSysDictionaryEntityRequest);
		} else {
			// 修改
			return sysDictionaryService.update(loanSysDictionaryEntityRequest);
		}
	}
}
