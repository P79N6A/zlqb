package com.zhiwang.zfm.service.impl.sys;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.request.sys.SysParameterVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.common.util.DateUtils;
import com.zhiwang.zfm.common.util.bean.BeanCommonUtils;
import com.zhiwang.zfm.dao.sys.SysParameterMapper;
import com.zhiwang.zfm.entity.sys.SysParameter;
import com.zhiwang.zfm.service.api.sys.SysParameterService;

@Service
@Transactional
public class SysParameterServiceImpl<T> implements SysParameterService<T> {
	
	private Logger logger = LoggerFactory.getLogger(SysParameterServiceImpl.class);
	
	@Autowired
    private SysParameterMapper<SysParameter> mapper;

	public SysParameterMapper<SysParameter> getMapper() {
		return mapper;
	}
	
	
	
	@Override
	public CommonResponse<JSONObject> save(SysParameterVO vo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			SysParameter sysParameter = BeanCommonUtils.copyProperties(vo,SysParameter.class);
			sysParameter.setId(UUID.randomUUID().toString().replace("-", ""));
			sysParameter.setCreateTime(DateUtils.getCurrentTimeDate());
			sysParameter.setUpdateTime(DateUtils.getCurrentTimeDate());
			mapper.insert(sysParameter);
			common.setCode(StatusConstants.SUCCESS_CODE);
			common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("新增系统参数数据失败{}", e.getMessage());
			common.setSuccess(false);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setCode(StatusConstants.SYS_ERROR);
		}
		return common;
		
	}

	/**
	 * 参数配置 修改
	 * @return
	 */
	@Override
	public CommonResponse<JSONObject> update(SysParameterVO vo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		common.setCode(StatusConstants.ERROR_CODE);
		common.setSuccess(false);
		if (ChkUtil.isEmpty(vo.getId())) {
			common.setCode(StatusConstants.PARAM_ERROR);
			common.setMsg(StatusConstants.PARAM_ERROR_MSG);
			return common;
		}

		// 个人基本信息认证失效时间(天)
		Integer userinfoVerifyDay = vo.getUserinfoVerifyDay();
		// 运营商信息认证失效时间(天)
		Integer mobileVerifyDay = vo.getMobileVerifyDay();
		// 体现大额值
		Double withdrawCashAmount = vo.getWithdrawCashAmount().doubleValue();
		// 拒单天数间隔
		Integer refuseDay = vo.getRefuseDay();
		if (ChkUtil.isEmpty(userinfoVerifyDay) || userinfoVerifyDay <= 0) {
			common.setCode(StatusConstants.VALIDATE_ERROR);
			common.setMsg(StatusConstants.VALIDATE_ERROR_MSG);
			return common;
		}
		if (ChkUtil.isEmpty(mobileVerifyDay) || mobileVerifyDay <= 0) {
			common.setCode(StatusConstants.VALIDATE_ERROR);
			common.setMsg(StatusConstants.VALIDATE_ERROR_MSG);
			return common;
		}
		if (ChkUtil.isEmpty(withdrawCashAmount) || withdrawCashAmount <= 0) {
			common.setCode(StatusConstants.VALIDATE_ERROR);
			common.setMsg(StatusConstants.VALIDATE_ERROR_MSG);
			return common;
		}
		// 长度不能大于9
		if (withdrawCashAmount.toString().length() > 9) {
			common.setCode(StatusConstants.LENGTH_ERROR);
			common.setMsg(StatusConstants.LENGTH_ERROR_MSG);
			return common;
		}
		if (ChkUtil.isEmpty(refuseDay) || refuseDay <= 0) {
			common.setCode(StatusConstants.VALIDATE_ERROR);
			common.setMsg(StatusConstants.VALIDATE_ERROR_MSG);
			return common;
		}
		// 长度不能大于6
		if (String.valueOf(refuseDay).length() > 6) {
			common.setCode(StatusConstants.LENGTH_ERROR);
			common.setMsg(StatusConstants.LENGTH_ERROR_MSG);
			return common;
		}
		try {
			SysParameter sysParameter = BeanCommonUtils.copyProperties(vo,SysParameter.class);
			sysParameter.setUpdateTime(DateUtils.getCurrentTimeDate());
			mapper.updateBySelective(sysParameter);
			common.setCode(StatusConstants.SUCCESS_CODE);
			common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("跟新系统参数失败{}", e.getMessage());
			common.setSuccess(false);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setCode(StatusConstants.SYS_ERROR);
			return common;
		}
		return common;
	}

	/**
	 * 查询最新一条数据 
	 */
	@Override
	public CommonResponse<SysParameterVO> queryNew() {
		CommonResponse<SysParameterVO> common = new CommonResponse<SysParameterVO>();
		try {
			SysParameterVO vo = BeanCommonUtils.copyProperties(mapper.queryNew(), SysParameterVO.class);
			common.setData(vo);
			common.setCode(StatusConstants.SUCCESS_CODE);
			common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("查询系统参数数据失败{}", e.getMessage());
			common.setSuccess(false);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setCode(StatusConstants.SYS_ERROR);
			return common;
		}
		return common;
	}

}
