package com.nyd.zeus.dao.mapper;

import com.nyd.zeus.model.OrderRecordHisVo;
import com.nyd.zeus.model.RemitInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface RemitMapper {
    List<RemitInfo> selectTime(String assetNo)throws Exception;

    List<RemitInfo> getSuccessRemit(@Param("startTime") Date startTime,@Param("endTime") Date endTime);
    
    OrderRecordHisVo getOverdueDays(String orderNo);
}
