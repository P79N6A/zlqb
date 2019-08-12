package com.nyd.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.admin.model.dto.JiGuangParamDto;

@Mapper
public interface JiGuangPushMapper {
	
	/**
	 * 注册已登录但资料不完整的用户总数
	 * @param dto
	 * @return
	 */
	Integer findDataIncompleteCount(JiGuangParamDto dto);
	
	/**
	 * 注册已登录但资料不完整用户
	 * @param dto
	 * @return
	 */	
	List<String> findDataIncomplete(JiGuangParamDto dto);
	
	/**
	 * 放款成功个数
	 * @param jiGuangDto
	 * @return
	 */
	Integer findLoanSuccessCount(JiGuangParamDto jiGuangDto);
	
	/**
	 * 放款成功
	 */
	List<String> findLoanSuccess(JiGuangParamDto jiGuangDto);
}
