package com.nyd.order.service.impl.zzl;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.order.api.spring.OrderToBillByProductService;
import com.nyd.order.api.zzl.OrderForZLQServise;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.entity.Order;
import com.nyd.order.entity.OrderReviewed;
import com.nyd.order.model.OrderCheckQuery;
import com.nyd.order.model.OrderCheckVo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.OrderRecordHisVo;
import com.nyd.order.model.UserNoParam;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.vo.ExamReq;
import com.nyd.order.model.vo.ProductOrderVo;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfoForZzlVO;
import com.nyd.zeus.model.helibao.util.Uuid;


@Service(value="orderForZLQServise")
public class OrderForZLQServiseImpl implements OrderForZLQServise{
	private static final Logger logger= LoggerFactory.getLogger(OrderForZLQServiseImpl.class);
	@Autowired
    private OrderDao orderDao;
	
	@Autowired
    private OrderMapper mapper;
	
	@Autowired
	OrderSqlService orderSqlService;
	
	@Autowired
	private OrderToBillByProductService orderToBillByProductService;
	
	@Autowired
	private ProductContract productContract;

	/**
	 * 订单审批列表查询
	 * @param orderCheckQuery
	 * @return
	 */
	@Override
	public PagedResponse<List<OrderCheckVo>> findByParam(OrderCheckQuery orderCheckQuery){
		PagedResponse<List<OrderCheckVo>> result = new PagedResponse<List<OrderCheckVo>>();
		try {
			String sql = "select xo.user_id as userId,xo.assign_id as assignId, xo.assign_name as assignName,"
					+ " xo.loan_number loanNumber,od.real_name userName, od.mobile mobile,od.source channel,"
					+ " xo.loan_time loanTime,xo.app_name appName,xo.assign_time assignTime,xo.order_no orderNo,"
					+ " case when xo.order_status = " + OrderStatus.AUDIT.getCode() + " then 1 "
					+ " when xo.audit_status = 1 and xo.reviewed_id is not null and xo.reviewed_id != '' then 2 "
					+ " when xo.audit_status = 0 and xo.reviewed_id is not null and xo.reviewed_id != '' then 3 "
					+ " end as auditStatus"
					+ " from t_order xo INNER JOIN t_order_detail od on xo.order_no=od.order_no "
					+ " WHERE  1=1 and xo.who_audit='1' "
					+ " and (xo.assign_id is not null or xo.assign_id !='')";
			StringBuffer buff = new StringBuffer();
			if (orderCheckQuery.getAuditStatus() == null) {
				buff.append(" and (xo.order_status = " + OrderStatus.AUDIT.getCode() + " or (xo.reviewed_id is not null and xo.reviewed_id != '')) ");
			} else if (orderCheckQuery.getAuditStatus().intValue() == 1) {
				buff.append(" and xo.order_status = " + OrderStatus.AUDIT.getCode() + " ");
			} else if (orderCheckQuery.getAuditStatus().intValue() == 2) {
				buff.append(" and xo.audit_status = 1 and xo.reviewed_id is not null and xo.reviewed_id != '' ");
			} else if (orderCheckQuery.getAuditStatus().intValue() == 3) {
				buff.append(" and xo.audit_status = 0  and xo.reviewed_id is not null and xo.reviewed_id != '' ");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getUserName())) {
				buff.append(" and od.real_name ='").append(orderCheckQuery.getUserName()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getAssignId())) {
				buff.append(" and xo.assign_id ='").append(orderCheckQuery.getAssignId()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getAccountNumber())) {
				buff.append(" and od.mobile='").append(orderCheckQuery.getAccountNumber()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getCheckPersonnel())) {
				buff.append(" and  xo.assign_name='").append(orderCheckQuery.getCheckPersonnel()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getAppName())) {
				buff.append(" and  od.source='").append(orderCheckQuery.getAppName()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getBeginTime())) {
				buff.append(" and xo.assign_time >='").append(orderCheckQuery.getBeginTime()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getEndTime())) {
				buff.append(" and xo.assign_time <='").append(orderCheckQuery.getEndTime()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getIntoTimeStart())) {
				buff.append(" and xo.create_time >='").append(orderCheckQuery.getIntoTimeStart()).append("'");
			}
			if (StringUtils.isNotBlank(orderCheckQuery.getIntoTimeEnd())) {
				buff.append(" and xo.create_time <='").append(orderCheckQuery.getIntoTimeEnd()).append("'");
			}
			buff.append(" ORDER BY xo.assign_time");

			String querySql = sql + buff.toString();
			List<OrderCheckVo> orderList = orderSqlService.pageT(querySql, orderCheckQuery.getPageNo(), orderCheckQuery.getPageSize(), OrderCheckVo.class);
			Long ct = orderSqlService.count(querySql);
			result.setData(orderList);
			result.setTotal(ct);
			result.setSuccess(true);
			return result;
		}catch (Exception e){
			logger.error("订单审批列表查询异常 e="+e.getMessage());
			result.setSuccess(false);
			result.setMsg("操作异常，请联系管理员");
			return result;
		}
	}

	@Override
	public List<OrderRecordHisVo> findHisByUserId(UserNoParam user) {
		//CommonResponse<OrderRecordHisVo>  his=new CommonResponse<OrderRecordHisVo>();
		//OrderRecordHisVo orderRecordHisVo=new OrderRecordHisVo();
		List<OrderRecordHisVo> orderHisList=(List<OrderRecordHisVo>) mapper.getOrderHisRecord(user);
		
		// TODO Auto-generated method stub
		return orderHisList;
	}

	@Override
	public CommonResponse checkApproval(ExamReq req) {
		CommonResponse common = new CommonResponse<>();
		if(null == req){
			common.setSuccess(false);
			common.setMsg("产品不能为空");
			return common;
		}
		
		String sql = "select * from t_order where order_no = '%s'";
		List<OrderCheckVo> list = orderSqlService.queryT(String.format(sql, req.getOrderNo()), OrderCheckVo.class);
		if(null == list || list.size()<1){
			common.setSuccess(false);
			common.setMsg("无订单号");
			return common;
		}
		OrderCheckVo order = list.get(0);
		if(!(req.getLoanUserId().equals(order.getAssignId()))){
			common.setSuccess(false);
			common.setMsg("订单已分配他人");
			return common;
		}
		if(10 != order.getOrderStatus()){
			common.setSuccess(false);
			common.setMsg("订单已不在审核中");
			return common;
		}
		
		if("0".equals(req.getCheckProduct())){
			String updSql = "update t_order set order_status='%s',audit_status='%s',reviewed_id='%s',reviewed_name='%s',reviewed_time=now(),reviewed_remark='%s' where order_no='%s'";
			orderSqlService.updateSql(String.format(updSql, OrderStatus.AUDIT_REFUSE.getCode(),"0",req.getLoanUserId(),req.getLoanUserName(),req.getRemark(),req.getOrderNo()));
		}else{
			ProductOrderVo product = new ProductOrderVo();
			product.setOrderNo(order.getOrderNo());
			product.setUserId(order.getUserId());
			Boolean flag = orderToBillByProductService.getProductInfo(product);
			if(flag){
				String updSql = "update t_order set order_status='%s',audit_status='%s',reviewed_id='%s',reviewed_name='%s',reviewed_time=now(),reviewed_remark='%s',real_loan_amount ='%s' where order_no='%s'";
				orderSqlService.updateSql(String.format(updSql, OrderStatus.AUDIT_SUCCESS.getCode(),"1",req.getLoanUserId(),req.getLoanUserName(),req.getRemark(),new BigDecimal(req.getCheckProduct()),req.getOrderNo()));
			}else{
				common.setSuccess(false);
				common.setMsg("订单产品已存在该订单号");
				return common;
			}
			
		}
		String id= UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24();
		OrderReviewed reviewed = new OrderReviewed();
		reviewed.setId(id);
		reviewed.setOrderNo(order.getOrderNo());
		reviewed.setUserId(order.getUserId());
		reviewed.setReviewedId(req.getLoanUserId());
		reviewed.setReviewedName(req.getLoanUserName());
		reviewed.setReviewedTime(new Date());
		reviewed.setRemark(req.getRemark());
		orderDao.saveOrderReviewed(reviewed);
		
		common.setSuccess(true);
		common.setMsg("操作成功");
		return common;
	}
	
	@Override
	public Order findManagerFee(OrderInfo orderInfo) {
		Order order=new Order();
		String sql ="select * from  t_order  where  order_no='"+orderInfo.getOrderNo()+"'";
		List<Order> list=orderSqlService.queryT(sql, Order.class);
		order=list.get(0);
		return order;
	}

	@Override
	public Boolean findOrderStatus(OrderInfo orderinfo) {
		Boolean bool=false;
		try {
			if(orderinfo.getMark().equals("1")) {
				//order_status='"+orderinfo.getOrderStatus()+"'  and 
				String sql ="select * from  t_order  where    order_no='"+orderinfo.getOrderNo()+"'";
				List<OrderInfo> orderList=orderSqlService.queryT(sql, OrderInfo.class);
				if(orderList!=null &&orderList.size()>0) {
					//存在记录修改审核状态是审核通过
					//String sqlupd ="update  t_order set  order_status=''   where  order_status='"+order.getOrderStatus()+"'  and   order_no='"+order.getOrderNo()+"'";
					//bool=orderSqlService.updateSql(sqlupd);
					for(OrderInfo or:orderList) {
						or.setOrderStatus(1200);
						or.setUpdateTime(new Date());
						orderDao.update(or);
					}
					bool=true;
				}
			}else if(orderinfo.getMark().equals("2")) {
				String sql ="select * from  t_order  where  order_status='20'  and order_no='"+orderinfo.getOrderNo()+"'";
				List<OrderInfo> orderList=orderSqlService.queryT(sql, OrderInfo.class);
				if(orderList!=null &&orderList.size()>0) {
					for(OrderInfo or:orderList) {
						or.setOrderStatus(1100);
						or.setUpdateTime(new Date());
						orderDao.update(or);
					}
					bool=true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bool;
	}

	@Override
	public CommonResponse<Boolean> check(String orderId) {
		CommonResponse<Boolean> common = new CommonResponse<Boolean>();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT");
		sb.append(" *");
		sb.append(" FROM");
		sb.append(" t_order");
		sb.append(" WHERE 1=1");
		sb.append(" and order_no = '" + orderId +"'");
		sb.append(" and who_audit = 0 ");
		sb.append(" AND order_status <= " + OrderStatus.AUDIT.getCode());
		Long ret = orderSqlService.count(sb.toString());
		common.setSuccess(true);
		if(ret > 0){
			common.setData(true);	
		}else if(ret == 0){
			common.setData(false);	
		}else{
			common.setSuccess(false);
		}
		return common;
	}

	@Override
	public CommonResponse<List<ExamReq>> queryProduct(ExamReq req) {
		CommonResponse<List<ExamReq>> comon = new CommonResponse<List<ExamReq>>();
		ProductInfoForZzlVO info = productContract.getProductForZzl("");
		List<ExamReq> list = new ArrayList<ExamReq>();
		if(null != info){
			ExamReq exam1 = new ExamReq();
			String m = info.getMoney();
			if(m.contains(".")){
				String[] arr = m.split("\\.");
				exam1.setCheckProduct(arr[0]);
				exam1.setCheckProductName(arr[0]);
			}else{
				exam1.setCheckProduct(info.getMoney());
				exam1.setCheckProductName(info.getMoney());
			}
			
			
			list.add(exam1);
		}
		ExamReq exam2 = new ExamReq();
		exam2.setCheckProduct("0");
		exam2.setCheckProductName("拒绝");
		list.add(exam2);
		comon.setData(list);
		comon.setSuccess(true);
		return comon;
	}

}
