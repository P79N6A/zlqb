package com.zhiwang.zfm.controller.webapp.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
import com.zhiwang.zfm.common.request.sys.SysDictionaryDetailVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.service.api.sys.SysDictionaryDetailService;


/**
 * 数据字典明细
 * 
 * @author zhenggang.Huang
 * @date 2018年5月14日
 */
@RestController
@RequestMapping("/api/sys/dictionaryDetail")
@Api(description = "数据字典明细")
public class DictionaryDetailController {

	private Logger logger = LoggerFactory.getLogger(DictionaryDetailController.class);

	@Autowired
	private SysDictionaryDetailService sysDictionaryDetailService;

	/**
	 * 根据条件查询
	 * zhenggang.Huang
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "数据字典明细-分页查询")
	@RequestMapping(value = "/queryPageDictionaryDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public PagedResponse<List<SysDictionaryDetailVO>> queryDictionaryList(@ModelAttribute SysDictionaryDetailVO sysDictionaryDetailRequset) {
		logger.info("数据字典明细-分页查询,入参：{}",JSONObject.toJSON(sysDictionaryDetailRequset));
		return sysDictionaryDetailService.queryDictionaryDetailList(sysDictionaryDetailRequset);
	}

	/**
	 * 新增，修改(id)，删除(状态改为0)
	 * zhenggang.Huang
	 */
	@ApiOperation(value = "数据字典明细-新增/修改")
	@RequestMapping(value = "/updateDictionaryDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public CommonResponse<SysDictionaryDetailVO> updateSysDictionary(SysDictionaryDetailVO loanSysDictionaryDetailEntityRequest) {
		logger.info("数据字典明细-新增/修改,入参：{}",JSONObject.toJSON(loanSysDictionaryDetailEntityRequest));
		// 保存
		if (ChkUtil.isEmpty(loanSysDictionaryDetailEntityRequest.getId())) {
			return sysDictionaryDetailService.save(loanSysDictionaryDetailEntityRequest);
		} else {
			// 修改
			return sysDictionaryDetailService.update(loanSysDictionaryDetailEntityRequest);
		}
	}

	/**
	 * 查询 申请中、审批中、还款中、已结清对应的数据字典明细 多个以","隔开
	 * zhenggang.Huang
	 */
	@ApiOperation(value = "数据字典明细-查询数据字典对应明细，多个以逗号隔开")
	@RequestMapping(value = "/queryDetailOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "数据字典大类编码,多个以逗号隔开", required = true)
	public CommonResponse<List<SysDictionaryDetailVO>> queryDetailOrder(String code) {
		return sysDictionaryDetailService.queryDetailOrder(code);
	}
}
