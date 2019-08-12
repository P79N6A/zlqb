package com.zhiwang.zfm.service.impl.sys;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiwang.zfm.common.request.sys.SysChannelReqVO;
import com.zhiwang.zfm.common.response.bean.sys.SysChannelVO;
import com.zhiwang.zfm.dao.sys.SysChannelMapper;
import com.zhiwang.zfm.entity.sys.query.SysChannelSearchForm;
import com.zhiwang.zfm.service.api.sys.SysChannelService;

@Service
@Transactional
public class SysChannelServiceImpl<T> implements SysChannelService<T> {

	@Autowired
	private SysChannelMapper<T> mapper;

	public SysChannelMapper<T> getMapper() {
		return mapper;
	}

	@Override
	public List<SysChannelVO> pageData(SysChannelReqVO vo) throws Exception {
		SysChannelSearchForm form = new SysChannelSearchForm();
		BeanUtils.copyProperties(vo, form);
		form.setPage(vo.getPageNo());
		form.setRows(vo.getPageSize());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<>();
		orderby.put("PLAT_CODE", "asc");
		orderby.put("CHANNEL_CODE", "asc");
		form.setOrderby(orderby);
		return (List<SysChannelVO>) mapper.pageData(form);
	}

	@Override
	public long pageTotalRecord(SysChannelReqVO vo) throws Exception {
		SysChannelSearchForm form = new SysChannelSearchForm();
		BeanUtils.copyProperties(vo, form);
		return mapper.pageTotalRecord(form);
	}
}
