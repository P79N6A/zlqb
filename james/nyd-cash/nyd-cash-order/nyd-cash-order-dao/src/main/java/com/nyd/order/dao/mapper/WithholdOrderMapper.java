package com.nyd.order.dao.mapper;

import com.nyd.order.entity.WithholdOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuqiu
 */
@Mapper
public interface WithholdOrderMapper {

    void save(WithholdOrder order) throws Exception;

    void update(WithholdOrder orderInfo) throws Exception;

    List<WithholdOrder> getObjectsByMemberId(String memberId) throws Exception;
    
    List<WithholdOrder> getObjectsForTask() throws Exception;

    List<WithholdOrder> getObjectsPayOrderNo(String payOrderNo) throws Exception;
    
    List<WithholdOrder> findObjectsByMemberIdDesc(String memberId) throws Exception;

    List<WithholdOrder> selectStatusOne() throws Exception;

}
