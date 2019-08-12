package com.zhiwang.zfm.service.impl.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiwang.zfm.common.request.sys.SysDictionaryDetailVO;
import com.zhiwang.zfm.common.response.AppCommonResponse;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.common.util.bean.BeanCommonUtils;
import com.zhiwang.zfm.dao.sys.SysDictionaryDetailMapper;
import com.zhiwang.zfm.entity.sys.SysDictionaryDetail;
import com.zhiwang.zfm.entity.sys.query.SysDictionaryDetailSearchForm;
import com.zhiwang.zfm.service.api.sys.SysDictionaryDetailService;

@Service
@Transactional
public class SysDictionaryDetailServiceImpl<T> implements SysDictionaryDetailService<T> {

	private Logger logger = LoggerFactory.getLogger(SysDictionaryDetailServiceImpl.class);
	
	@Autowired
    private SysDictionaryDetailMapper<SysDictionaryDetail> sysDictionaryDetailDao;

	public SysDictionaryDetailMapper<SysDictionaryDetail> getMapper() {
		return sysDictionaryDetailDao;
	}
	
	/**
	 * 保存数据字典明细
	 */
	@Override
	public CommonResponse<SysDictionaryDetailVO> save(SysDictionaryDetailVO SysDictionaryDetailVO) {
		CommonResponse<SysDictionaryDetailVO> commonResponse = new CommonResponse<>();
		try {
			SysDictionaryDetail loanSysDictionaryDetail = BeanCommonUtils.copyProperties(SysDictionaryDetailVO, SysDictionaryDetail.class);
			// 数据字典名称
			String name = ChkUtil.removeBlank(loanSysDictionaryDetail.getName());
			// 状态
			Integer status = loanSysDictionaryDetail.getStatus();
			// 值
			String price = ChkUtil.removeBlank(loanSysDictionaryDetail.getPrice());
			// 大类编码
			String code = ChkUtil.removeBlank(loanSysDictionaryDetail.getCode());
			// 明细编码
			String detailCode = ChkUtil.removeBlank(loanSysDictionaryDetail.getDetailCode());
			//排序码
			Long reorder = loanSysDictionaryDetail.getReorder();
			commonResponse.setCode(StatusConstants.PARAM_ERROR);
			commonResponse.setSuccess(false);
			if (ChkUtil.isEmpty(code)) {
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			// 名称不能为空
			if (ChkUtil.isEmpty(name)) {
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			// 长度大于50
			if (name.length() > 50) {
				commonResponse.setCode(StatusConstants.LENGTH_ERROR);
				commonResponse.setMsg(StatusConstants.LENGTH_ERROR_MSG);
				return commonResponse;
			}
			if (ChkUtil.isEmpty(code)) {
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			// 长度大于20
			if (code.length() > 20) {
				commonResponse.setCode(StatusConstants.LENGTH_ERROR);
				commonResponse.setMsg(StatusConstants.LENGTH_ERROR_MSG);
				return commonResponse;
			}
			// 值不能为空
			if (ChkUtil.isEmpty(price)) {
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			// 排序码不能为空
			if (ChkUtil.isEmpty(reorder)) {
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			if (!ChkUtil.isEmpty(status) && status != 1 && status != 0) {
				commonResponse.setCode(StatusConstants.VALIDATE_ERROR);
				commonResponse.setMsg(StatusConstants.VALIDATE_ERROR_MSG);
				return commonResponse;
			}
			if (ChkUtil.isEmpty(status)) {
				loanSysDictionaryDetail.setStatus(1);
			}
			// 判断编码
			SysDictionaryDetailSearchForm form = new SysDictionaryDetailSearchForm();
			form.setDetailCode(detailCode);
			List<SysDictionaryDetail> codeList = sysDictionaryDetailDao.pageData(form);
			if (!ChkUtil.isEmpty(codeList) && codeList.size() > 0) {
				commonResponse.setCode(StatusConstants.DICTIONARY_DETAIL_CODE_ERROR);
				commonResponse.setMsg(StatusConstants.DICTIONARY_DETAIL_CODE_ERROR_MSG);
				return commonResponse;
			}
			// 判断排序码
			form = new SysDictionaryDetailSearchForm();
			form.setReorder(reorder);
			form.setCode(code);
			List<SysDictionaryDetail> orderList = sysDictionaryDetailDao.pageData(form);
			if (!ChkUtil.isEmpty(orderList) && orderList.size() > 0) {
				commonResponse.setCode(StatusConstants.REORDER_ERROR);
				commonResponse.setMsg(StatusConstants.REORDER_ERROR_MSG);
				return commonResponse;
			}
			// 判断值
			loanSysDictionaryDetail.setCreateTime(new Date());
			loanSysDictionaryDetail.setUpdateTime(new Date());
			loanSysDictionaryDetail.setCode(code);
			loanSysDictionaryDetail.setDetailCode(detailCode);
			loanSysDictionaryDetail.setPrice(price);
			loanSysDictionaryDetail.setName(name);
			loanSysDictionaryDetail.setId(UUID.randomUUID().toString().replace("-", ""));
		
			sysDictionaryDetailDao.insert(loanSysDictionaryDetail);
			commonResponse.setCode(StatusConstants.SUCCESS_CODE);
			commonResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			commonResponse.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增数据字典明细失败{}", e.getMessage());
			commonResponse.setSuccess(false);
			commonResponse.setMsg(StatusConstants.SYS_ERROR_MSG);
			commonResponse.setCode(StatusConstants.SYS_ERROR);
			return commonResponse;
		}
		return commonResponse;
	}

	/**
	 * 更新数据字典明细
	 */
	@Override
	public CommonResponse<SysDictionaryDetailVO> update(SysDictionaryDetailVO SysDictionaryDetailVO) {
		CommonResponse<SysDictionaryDetailVO> commonResponse = new CommonResponse<>();
		try {
			SysDictionaryDetail loanSysDictionaryDetail = BeanCommonUtils.copyProperties(SysDictionaryDetailVO, SysDictionaryDetail.class);
			// 数据字典名称
			String name = ChkUtil.removeBlank(loanSysDictionaryDetail.getName());
			// 状态
			Integer status = loanSysDictionaryDetail.getStatus();
			// 值
			String price = ChkUtil.removeBlank(loanSysDictionaryDetail.getPrice());
			// 大类编码
			String code = ChkUtil.removeBlank(loanSysDictionaryDetail.getCode());
			// 明细编码
			String detailCode = ChkUtil.removeBlank(loanSysDictionaryDetail.getDetailCode());
			// 排序码
			Long reorder = loanSysDictionaryDetail.getReorder();
	
			commonResponse.setCode(StatusConstants.ERROR_CODE);
			commonResponse.setSuccess(false);
			// 判断状态
			if (ChkUtil.isEmpty(status)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			if (!ChkUtil.isEmpty(status)) {
				if ((status != 1) && (status != 0)) {
					commonResponse.setCode(StatusConstants.VALIDATE_ERROR);
					commonResponse.setMsg(StatusConstants.VALIDATE_ERROR_MSG);
					return commonResponse;
				}
			}
			if (status != null && ChkUtil.isEmpty(name) && ChkUtil.isEmpty(price) && ChkUtil.isEmpty(code)&& ChkUtil.isEmpty(detailCode)) {
				loanSysDictionaryDetail.setUpdateTime(new Date());
				sysDictionaryDetailDao.updateBySelective(loanSysDictionaryDetail);
				commonResponse.setSuccess(true);
				commonResponse.setCode(StatusConstants.SUCCESS_CODE);
				commonResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
				return commonResponse;
			}
			if (ChkUtil.isEmpty(name)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			// 长度大于50
			if (name.length() > 50) {
				commonResponse.setCode(StatusConstants.LENGTH_ERROR);
				commonResponse.setMsg(StatusConstants.LENGTH_ERROR_MSG);
				return commonResponse;
			}
			// 不能修改编码
			if (!ChkUtil.isEmpty(code) || !ChkUtil.isEmpty(detailCode)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			if (ChkUtil.isEmpty(price)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			if (ChkUtil.isEmpty(reorder)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			SysDictionaryDetail dictionaryDetail = sysDictionaryDetailDao.selectById(loanSysDictionaryDetail.getId());
			// 说明修改了
			if (ChkUtil.isEmpty(dictionaryDetail.getReorder()) || !dictionaryDetail.getReorder().equals(loanSysDictionaryDetail.getReorder())) {
				
				// 判断编码
				SysDictionaryDetailSearchForm form = new SysDictionaryDetailSearchForm();
				form.setReorder(loanSysDictionaryDetail.getReorder());
				form.setCode(dictionaryDetail.getCode());
				List<SysDictionaryDetail> orderList = sysDictionaryDetailDao.pageData(form);
				
				if (!ChkUtil.isEmpty(orderList) && orderList.size() > 0) {
					commonResponse.setCode(StatusConstants.REORDER_ERROR);
					commonResponse.setMsg(StatusConstants.REORDER_ERROR_MSG);
					return commonResponse;
				}
			}
			loanSysDictionaryDetail.setUpdateTime(new Date());
			// loanSysDictionaryDetail.setCode(code);
			// loanSysDictionaryDetail.setDetailCode(detailCode);
			loanSysDictionaryDetail.setPrice(price);
			loanSysDictionaryDetail.setName(name);
			commonResponse.setCode(StatusConstants.SUCCESS_CODE);
			commonResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			commonResponse.setSuccess(true);
			sysDictionaryDetailDao.updateBySelective(loanSysDictionaryDetail);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新数据字典明细失败{}", e.getMessage());
			commonResponse.setSuccess(false);
			commonResponse.setMsg(StatusConstants.SYS_ERROR_MSG);
			commonResponse.setCode(StatusConstants.SYS_ERROR);
			return commonResponse;
		}
		return commonResponse;
	}


	/**
	 * 查询数据字典明细列表
	 */
	@Override
	public PagedResponse<List<SysDictionaryDetailVO>> queryDictionaryDetailList(SysDictionaryDetailVO sysDictionaryDetailRequest) {
		PagedResponse<List<SysDictionaryDetailVO>> pagedResponse = new PagedResponse<>();
		try {
			SysDictionaryDetailSearchForm form = new SysDictionaryDetailSearchForm();
			form.setName(sysDictionaryDetailRequest.getName());
			form.setDictionaryId(sysDictionaryDetailRequest.getDictionaryId());
			form.setCode(sysDictionaryDetailRequest.getCode());
			form.setPrice(sysDictionaryDetailRequest.getPrice());
			form.setReorder(sysDictionaryDetailRequest.getReorder());
			form.setDetailCode(sysDictionaryDetailRequest.getDetailCode());
			form.setPage(sysDictionaryDetailRequest.getPageNo());
			form.setRows(sysDictionaryDetailRequest.getPageSize());
			form.setStatus(sysDictionaryDetailRequest.getStatus());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("reorder", "asc");
			form.setOrderby(orderby);
			List<SysDictionaryDetail> sysDictionarys = sysDictionaryDetailDao.pageData(form);
			
			long total = sysDictionaryDetailDao.count(form);
			pagedResponse.setTotal(total);
			pagedResponse.setData(BeanCommonUtils.copyListProperties(sysDictionarys, SysDictionaryDetailVO.class));
			
			pagedResponse.setPageSize(sysDictionaryDetailRequest.getPageSize());
			pagedResponse.setPageNo(sysDictionaryDetailRequest.getPageNo());
			pagedResponse.setCode(StatusConstants.SUCCESS_CODE);
			pagedResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			pagedResponse.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询数据字典明细列表失败{}", e.getMessage());
			pagedResponse.setSuccess(false);
			pagedResponse.setMsg(StatusConstants.SYS_ERROR_MSG);
			pagedResponse.setCode(StatusConstants.SYS_ERROR);
			return pagedResponse;
		}
		return pagedResponse;
	}


	@Override
	public CommonResponse<List<SysDictionaryDetailVO>> queryDetailOrder(String detailCode) {
		CommonResponse<List<SysDictionaryDetailVO>> commonResponse = new CommonResponse<>();
		if(ChkUtil.isEmpty(detailCode)) {
			commonResponse.setMsg(StatusConstants.PARAM_ERROR);
			commonResponse.setCode(StatusConstants.PARAM_ERROR_MSG);
			return commonResponse;
		}
		String[] codes = detailCode.split(",");
		List<String> codeList = new ArrayList<>();
		for(String code : codes ) {
			codeList.add(ChkUtil.removeBlank(code));
		}
		Map<String,Object> param = new HashMap<>();
		param.put("codeList", codeList);
		
		List<SysDictionaryDetailVO> data = BeanCommonUtils.copyListProperties(sysDictionaryDetailDao.queryDetailOrder(param), SysDictionaryDetailVO.class);
		commonResponse.setCode(StatusConstants.SUCCESS_CODE);
		commonResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
		commonResponse.setSuccess(true);
		commonResponse.setData(data);
		return commonResponse;
	}

}
