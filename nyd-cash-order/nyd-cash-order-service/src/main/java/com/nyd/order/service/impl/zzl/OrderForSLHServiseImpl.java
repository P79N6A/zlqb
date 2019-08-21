package com.nyd.order.service.impl.zzl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.support.json.JSONUtils;
import com.nyd.order.model.order.*;
import com.nyd.zeus.api.zzl.ZeusForGYTServise;
import com.nyd.zeus.model.attendance.TimeAttendance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.order.api.zzl.OrderForSLHServise;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.entity.Order;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.enums.OrderStatus;

@Service("orderForSLHServise")
public class OrderForSLHServiseImpl implements OrderForSLHServise{
	private static final Logger log = LoggerFactory.getLogger(OrderForSLHServiseImpl.class);
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderSqlService orderSqlService;
	@Autowired
	private ZeusForGYTServise zeusForGYTServise;

	/**
	 * 订单分配列表查询
	 * @param orderParam
	 * @return
	 */
	@Override
	public PagedResponse<List<OrderListVO>> orderApportionAndUndistributed(OrderParamVO orderParam) {
		PagedResponse<List<OrderListVO>> data = new PagedResponse<List<OrderListVO>>();
		try {
			Integer total=orderMapper.queryOrderByUserTypeCount(orderParam);
			if (total != null&&total.intValue()> 0) {
				List<OrderListVO> temp = orderMapper.queryOrderByUserType(orderParam);
				data.setSuccess(true);
				data.setData(temp);
				data.setTotal(total);
			} else {
				data.setData(new ArrayList<OrderListVO>());
				data.setSuccess(true);
			}
		}catch (Exception e){
			log.error("订单分配列表查询异常 e="+e.getMessage()+" requestParam="+ JSONUtils.toJSONString(orderParam));
			data.setSuccess(false);
			data.setMsg("操作异常 请联系管理员");
		}
		return data;
	}


	/**
	 * 订单分配
	 * @param orderAllocationVO
	 * @return
	 */
	@Override
	public CommonResponse orderAllocationToUser(OrderAllocationVO orderAllocationVO) {
		CommonResponse common = new CommonResponse();
		try {
			List<String> orderList = orderAllocationVO.getOrderNo();
			for (String orderNo : orderList) {
				String sql = "select * from t_order where order_no = '%s'";
				List<Order> list = orderSqlService.queryT(String.format(sql, orderNo), Order.class);
				if (null == list || list.size() < 1) {
					continue;
				}
				Order order = list.get(0);
				if (!(OrderStatus.AUDIT.getCode().equals(order.getOrderStatus())) || 1 != order.getWhoAudit()) {
					continue;
				}
				String updateSql = "update t_order set assign_id='%s',assign_name='%s',assign_time=now() where order_no='%s' and order_status='%s' and who_audit='%s'";
				String updSql = String.format(updateSql, orderAllocationVO.getUserId(), orderAllocationVO.getUserName(), orderNo, OrderStatus.AUDIT.getCode(), 1);
				orderSqlService.updateSql(updSql);
			}

			return common.success("订单分配成功");
		}catch (Exception e){
			log.error("订单分配异常 e="+e.getMessage());
			return CommonResponse.error("订单分配异常");
		}
	} 

	

	@Override
	public UserPicture queryUserPicture(OrderDetialsVO orderDetials) {
		return new UserPicture();
	}
	/**
	 * 获取最新待审的分配的订单
	 * @return
	 */
	public OrderListVO queryNewAutoAllocatnOrder(){
		return orderMapper.queryNewAutoAllocatnOrder();
	}

	/**
	 * 自动分配待审订单
	 * @param list
	 */
	public CommonResponse autoOrderAllocationToUser(List<OrderAllocationVO> list){
		//获取信审人员
		log.info("自动分配待审订单 开始------------------------------------------");
		log.info("自动分配待审订单 开始获取信审人员 ");
		CommonResponse res=new CommonResponse();
		List<TimeAttendance> voList=new ArrayList<TimeAttendance>(list.size());
		for(OrderAllocationVO v:list){
			TimeAttendance t=new TimeAttendance();
			t.setSysUserId(v.getUserId());
			t.setSysUserName(v.getUserName());
			voList.add(t);
		}
		com.nyd.zeus.model.common.CommonResponse<List<TimeAttendance>> response=zeusForGYTServise.getNowAttendance(voList);
		if(response==null||(!response.isSuccess())){
			log.info("自动分配待审订单 获取信审人员异常 ");
			return CommonResponse.error("获取信审人员异常");
		}
		List<TimeAttendance> taList=response.getData();
		if(taList==null||taList.size()==0){
			log.info("自动分配待审订单 没有执勤信审人员");
			return CommonResponse.error("没有执勤信审人员");
		}
		//获取最新待审的分配的订单
		OrderListVO order=orderMapper.queryNewAutoAllocatnOrder();
		OrderParamVO orderParam=new OrderParamVO();
		orderParam.setIsExistAssignId(1);
		Integer total=orderMapper.queryOrderByUserTypeCount(orderParam);
		if(total==null||total==0){
			log.info("没有待分配的订单 自动分配待审订单结束");
			return CommonResponse.error("没有待分配的订单");
		}
		orderParam.setPageNo(1);
		orderParam.setPageSize(total);
		List<OrderListVO> orderList = orderMapper.queryOrderByUserType(orderParam);
		if(orderList==null||orderList.size()==0){
			log.info("没有待分配的订单 自动分配待审订单结束");
			return CommonResponse.error("没有待分配的订单");
		}
		int index=0;//获取初始位置
		for(int i=0;i<taList.size();i++){
			TimeAttendance ta=taList.get(i);
			if(ta.getSysUserId().equals(order.getAssignId())){
				index=i+1;
				break;
			}
		}
		for(OrderListVO vo:orderList){
			if(index==taList.size()){
				index=0;
			}
			TimeAttendance ta=taList.get(index);
			OrderAllocationVO orderAllocationVO=new OrderAllocationVO();
			orderAllocationVO.setUserId(ta.getSysUserId());
			orderAllocationVO.setUserName(ta.getSysUserName());
			List orderNoList=new ArrayList<String>();
			orderNoList.add(vo.getOrderNo());
			orderAllocationVO.setOrderNo(orderNoList);
			//执行订单分配
			CommonResponse cr=orderAllocationToUser(orderAllocationVO);
			if(!cr.isSuccess()){
				log.info("自动分配待审订单 失败");
			}
			//执行分配
			index++;
		}
		log.info("自动分配待审订单 结束------------------------------------------");
		return CommonResponse.success("自动分配待审订单成功");
	}

	/**
	 * 待信审订单 分配撤回
	 * @param withdrawOrderVo
	 * @return
	 */
	public CommonResponse withdrawOrder(WithdrawOrderVo withdrawOrderVo){
		CommonResponse common = new CommonResponse();
		try {
			List<String> orderList = withdrawOrderVo.getOrderNoList();
			for (String orderNo : orderList) {
				String sql = "select * from t_order where order_no = '%s'";
				List<Order> list = orderSqlService.queryT(String.format(sql, orderNo), Order.class);
				if (null == list || list.size() < 1) {
					continue;
				}
				Order order = list.get(0);
				if (!(OrderStatus.AUDIT.getCode().equals(order.getOrderStatus())) || 1 != order.getWhoAudit()) {
					continue;
				}
				String updateSql = "update t_order set assign_id=null,assign_name=null,assign_time=null where order_no='%s' and order_status='%s' and who_audit='%s'";
				String updSql = String.format(updateSql, orderNo, OrderStatus.AUDIT.getCode(), 1);
				orderSqlService.updateSql(updSql);
			}
			return common.success("撤回成功");
		}catch (Exception e){
			log.error("撤回异常 e="+e.getMessage());
			return common.error("撤回异常");
		}
	}

	/**
	 * 待信审订单 分配全部撤回
	 * @return
	 */
	public CommonResponse withdrawAllOrder(){
		CommonResponse common = new CommonResponse();
		try {
			String updateSql = "update t_order set assign_id=null,assign_name=null,assign_time=null where order_status = 10 " + " AND who_audit =1 and (reviewed_id is null or reviewed_id = '') and "
					+ " (assign_id is not null and assign_id !='')";
			orderSqlService.updateSql(updateSql);
		}catch (Exception e){
			log.error("一键回撤异常 e="+e.getMessage());
			return common.error("撤回异常");
		}
		return common.success("撤回成功");
	}
}
