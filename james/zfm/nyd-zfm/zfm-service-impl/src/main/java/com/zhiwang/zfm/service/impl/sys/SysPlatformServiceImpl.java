package com.zhiwang.zfm.service.impl.sys;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiwang.zfm.common.request.sys.SysPlatformReqVO;
import com.zhiwang.zfm.common.response.bean.sys.SysPlatformVO;
import com.zhiwang.zfm.dao.sys.SysPlatformMapper;
import com.zhiwang.zfm.entity.sys.query.SysPlatformSearchForm;
import com.zhiwang.zfm.service.api.sys.SysPlatformService;

@Service
@Transactional
public class SysPlatformServiceImpl<T> implements SysPlatformService<T> {

	@Autowired
	private SysPlatformMapper<T> mapper;

	public SysPlatformMapper<T> getMapper() {
		return mapper;
	}

	@Override
	public List<SysPlatformVO> pageData(SysPlatformReqVO vo) throws Exception {
		SysPlatformSearchForm form = new SysPlatformSearchForm();
		BeanUtils.copyProperties(vo, form);
		form.setPage(vo.getPageNo());
		form.setRows(vo.getPageSize());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<>();
		orderby.put("PLAT_CODE", "asc");
		form.setOrderby(orderby);
		return (List<SysPlatformVO>) mapper.pageData(form);
	}

	@Override
	public long pageTotalRecord(SysPlatformReqVO vo) throws Exception {
		SysPlatformSearchForm form = new SysPlatformSearchForm();
		BeanUtils.copyProperties(vo, form);
		return mapper.pageTotalRecord(form);
	}
}
