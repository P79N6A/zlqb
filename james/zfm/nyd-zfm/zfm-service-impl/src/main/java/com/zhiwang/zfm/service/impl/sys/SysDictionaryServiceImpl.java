package com.zhiwang.zfm.service.impl.sys;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhiwang.zfm.common.request.sys.SysDictionaryDetailVO;
import com.zhiwang.zfm.common.request.sys.SysDictionaryVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.common.util.bean.BeanCommonUtils;
import com.zhiwang.zfm.dao.sys.SysDictionaryMapper;
import com.zhiwang.zfm.entity.sys.SysDictionary;
import com.zhiwang.zfm.entity.sys.query.SysDictionarySearchForm;
import com.zhiwang.zfm.service.api.sys.SysDictionaryService;

@Service
@Transactional
public class SysDictionaryServiceImpl<T> implements SysDictionaryService<T> {

	private Logger logger = LoggerFactory.getLogger(SysDictionaryServiceImpl.class);
	
	@Autowired
    private SysDictionaryMapper<SysDictionary> sysDictionaryDao;

	public SysDictionaryMapper<SysDictionary> getMapper() {
		return sysDictionaryDao;
	}
	
	/**
	 * 保存
	 * 
	 * @author huangzhenggang
	 * @date 2018年5月29日
	 */
	@Override
	public CommonResponse save(SysDictionaryVO sysDictionaryRequest) {
		CommonResponse commonResponse = new CommonResponse<>();
		try {
			SysDictionary loanSysDictionary = BeanCommonUtils.copyProperties(sysDictionaryRequest,SysDictionary.class);
			commonResponse.setCode(StatusConstants.ERROR_CODE);
			commonResponse.setSuccess(false);
			// 数据字典名称
			String name = ChkUtil.removeBlank(loanSysDictionary.getName());
			// 状态
			Integer status = loanSysDictionary.getStatus();
			// 数据字典编码
			String code = ChkUtil.removeBlank(loanSysDictionary.getCode());
			// 不能为空或长度大于50
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
			if (ChkUtil.isEmpty(code)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			// 长度大于20
			if (code.length() > 20) {
				commonResponse.setCode(StatusConstants.LENGTH_ERROR);
				commonResponse.setMsg(StatusConstants.LENGTH_ERROR_MSG);
				return commonResponse;
			}
			if (ChkUtil.isEmpty(status)) {
				loanSysDictionary.setStatus(1);
			}
	
			// 判断名称
			SysDictionarySearchForm form = new SysDictionarySearchForm();
			form.setName(name);
			long ct = sysDictionaryDao.count(form);
			if (ct > 0) {
				commonResponse.setCode(StatusConstants.DICTIONARY_NAME_ERROR);
				commonResponse.setMsg(StatusConstants.DICTIONARY_NAME_ERROR_MSG);
				return commonResponse;
			}
			// 判断编码
			form = new SysDictionarySearchForm();
			form.setCode(code);
			ct = sysDictionaryDao.count(form);
			if (ct > 0) {
				commonResponse.setCode(StatusConstants.DICTIONARY_CODE_ERROR);
				commonResponse.setMsg(StatusConstants.DICTIONARY_CODE_ERROR_MSG);
				return commonResponse;
			}
			loanSysDictionary.setCreateTime(new Date());
			loanSysDictionary.setUpdateTime(new Date());
			loanSysDictionary.setCode(code);
			loanSysDictionary.setName(name);
			loanSysDictionary.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			sysDictionaryDao.insert(loanSysDictionary);
			commonResponse.setCode(StatusConstants.SUCCESS_CODE);
			commonResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			commonResponse.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("新增数据字典失败,参数:{}", JSON.toJSONString(sysDictionaryRequest));
			commonResponse.setCode(StatusConstants.SYS_ERROR);
			commonResponse.setMsg(StatusConstants.SYS_ERROR_MSG);
			return commonResponse;
		}
		return commonResponse;
	}

	/**
	 * 更新 huangzhenggang 2018/5/29
	 */
	@Override
	public CommonResponse update(SysDictionaryVO SysDictionaryRequest) {
		CommonResponse<SysDictionaryVO> commonResponse = new CommonResponse<>();
		try {
			SysDictionary loanSysDictionary = BeanCommonUtils.copyProperties(SysDictionaryRequest,SysDictionary.class);
			// 数据字典名称
			String name = ChkUtil.removeBlank(loanSysDictionary.getName());
			// 数据字典编码
			String code = ChkUtil.removeBlank(loanSysDictionary.getCode());
			// 状态
			Integer status = loanSysDictionary.getStatus();
			commonResponse.setCode(StatusConstants.ERROR_CODE);
			if (!ChkUtil.isEmpty(status)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
			// 不能为空或长度大于50
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
			if (!ChkUtil.isEmpty(code)) {
				commonResponse.setCode(StatusConstants.PARAM_ERROR);
				commonResponse.setMsg(StatusConstants.PARAM_ERROR_MSG);
				return commonResponse;
			}
	
			// 根据修改id查询数据
			SysDictionaryVO oldSysDictionary = BeanCommonUtils.copyProperties(sysDictionaryDao.selectById(SysDictionaryRequest.getId()),SysDictionaryVO.class);
			// 判断名称是否已存在
			if (!name.equals(oldSysDictionary.getName())) {
				SysDictionarySearchForm form = new SysDictionarySearchForm();
				form.setName(name);
				long ct = sysDictionaryDao.count(form);
				if (ct > 0) {
					commonResponse.setCode(StatusConstants.DICTIONARY_NAME_ERROR);
					commonResponse.setMsg(StatusConstants.DICTIONARY_NAME_ERROR_MSG);
					return commonResponse;
				}
			}
			loanSysDictionary.setUpdateTime(new Date());
			loanSysDictionary.setCode(code);
			loanSysDictionary.setName(name);
		
			sysDictionaryDao.updateBySelective(loanSysDictionary);
			commonResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			commonResponse.setCode(StatusConstants.SUCCESS_CODE);
			commonResponse.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新数据字典失败{}", e.getMessage());
			commonResponse.setCode(StatusConstants.SYS_ERROR);
			commonResponse.setMsg(StatusConstants.SYS_ERROR_MSG);
			return commonResponse;
		}
		return commonResponse;
	}

	@Override
	public PagedResponse<List<SysDictionaryDetailVO>> queryDictionaryList(SysDictionaryVO sysDictionaryRequest) {

		PagedResponse<List<SysDictionaryDetailVO>> pagedResponse = new PagedResponse<>();
		try {
			
			SysDictionarySearchForm form = new SysDictionarySearchForm();
			form.setName(sysDictionaryRequest.getName());
			form.setCode(sysDictionaryRequest.getCode());
			form.setStatus(sysDictionaryRequest.getStatus());
			form.setPage(sysDictionaryRequest.getPageNo());
			form.setRows(sysDictionaryRequest.getPageSize());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("code", "asc");
			form.setOrderby(orderby);
			
			List<SysDictionary> sysDictionarys = sysDictionaryDao.pageData(form);
			long total = sysDictionaryDao.count(form);
			
			pagedResponse.setTotal(total);
			pagedResponse.setData(BeanCommonUtils.copyListProperties(sysDictionarys, SysDictionaryDetailVO.class));
			pagedResponse.setPageSize(sysDictionaryRequest.getPageSize());
			pagedResponse.setPageNo(sysDictionaryRequest.getPageNo());
			pagedResponse.setCode(StatusConstants.SUCCESS_CODE);
			pagedResponse.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			pagedResponse.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询数据字典列表失败{}", e.getMessage());
			pagedResponse.setMsg(StatusConstants.SYS_ERROR_MSG);
			pagedResponse.setCode(StatusConstants.SYS_ERROR);
		}
		return pagedResponse;
	
	}
	
	
	
}
