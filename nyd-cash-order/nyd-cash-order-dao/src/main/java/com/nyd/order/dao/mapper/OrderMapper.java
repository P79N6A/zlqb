package com.nyd.order.dao.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.model.JudgePeople;
import com.nyd.order.model.OrderCheckQuery;
import com.nyd.order.model.OrderCheckVo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.OrderRecordHisVo;
import com.nyd.order.model.UserNoParam;
import com.nyd.order.model.common.SqlSearchForm;
import com.nyd.order.model.dto.OrderQcgzDto;
import com.nyd.order.model.order.OrderListVO;
import com.nyd.order.model.order.OrderParamVO;
import com.nyd.order.model.order.UserInfo;
import com.nyd.order.model.vo.OrderlistVo;

@Repository
public interface OrderMapper {

    List<OrderInfo> getObjectsByOrderNo(String orderNo);

    List<OrderInfo> getObjectsByIbankOrderNo(String ibankOrderNo);

    /**
     * 根据放款渠道查询
     * @param fundCode
     * @return
     */
    int selectTodayOrderCountByFundCode(String fundCode);

    /**
     * 通过订单号查询资产编号
     * @param orderNo
     * @return
     */
    OrderQcgzDto selectAssetNo(String orderNo);

    /**
     * 根据资产号查询订单详情
     * @param assetNo
     * @return
     */
    OrderInfo selectOrderInfo(String assetNo);

    List<OrderInfo> selectOrderInfos(List<String> assetIds);

    void updateFundCode(String orderNo);

    List<OrderInfo> selectOrderInfosFromJx();

    /**
     * 根据资产渠道查询待放款订单
     * @param fundCode
     * @return
     */
    List<OrderInfo> queryOrdersWhenOrderStatusIsWait(String fundCode);

    OrderInfo getOrderByIbankOrderNo(String orderNo);
    
    List<OrderInfo> getWaitLoan(@Param("funCode") String funCode);
    
    OrderInfo getOrderByMemberId(@Param("memberId") String memberId);
    
    List<OrderInfo> getRefusedOrders(@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    BigDecimal getSuccessRemitAmount(@Param("funCode") String funCode);
   
    List<OrderListVO> queryOrderByUserType(OrderParamVO param );
    Integer queryOrderByUserTypeCount(OrderParamVO orderParam);
    /**
     * 把订单分配给信审人员
     * @param param
     * @return
     */
    Integer orderApplicationToUser(Map<String, Object> param );
    /**
     * 把订单分配给信审人员 做为记录保存  暂未实现
     * @param param
     * @return
     */
    Integer orderApplicationRecord(Map<String, Object> param);
   
    /**
     * 后台查询订单列表
     * @param vo
     * @return
     */
    List<OrderlistVo> getOrderList(OrderlistVo vo);
    
    long getOrderListCount(OrderlistVo vo);
    
    List<OrderCheckVo> getOrderCheck(OrderCheckQuery orderCheckQuery);
    
    Long pageTotalOrderCheck(OrderCheckQuery orderCheckQuery) throws Exception;
    
    
    List<OrderRecordHisVo> getOrderHisRecord(UserNoParam user);
    
   	List<JSONObject> sqlQuery(SqlSearchForm form);
   	
   	Long sqlCount(SqlSearchForm form);
   	
   	List<JSONObject>  sqlPage(SqlSearchForm form);
   	
   	
   	void deleteSql(SqlSearchForm form);
   	
   
   	void insertSql(SqlSearchForm form);
   	
   	
   	void updateSql(SqlSearchForm form);
   	
   
   	Long sqlSum(SqlSearchForm form);
    List<OrderInfo> getOrderInfoByOrderNo(String orderNo);
}
