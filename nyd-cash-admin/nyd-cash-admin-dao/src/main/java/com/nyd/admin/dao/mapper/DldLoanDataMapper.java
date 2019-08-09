package com.nyd.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.admin.entity.WithholdOrder;
import com.nyd.admin.model.dto.DldCreateBillDto;

@Mapper
public interface DldLoanDataMapper {
	
	List<DldCreateBillDto> findTwoLoanData();
	List<DldCreateBillDto> findCollectionData();
	
	void saveWithholdOrder(WithholdOrder withholdOrder);

}
