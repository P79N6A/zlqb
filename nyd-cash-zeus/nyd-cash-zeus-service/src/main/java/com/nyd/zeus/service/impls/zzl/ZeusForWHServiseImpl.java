package com.nyd.zeus.service.impls.zzl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.zeus.dao.BillDao;
import com.nyd.zeus.dao.enums.BillStatusEnum;
import com.nyd.zeus.dao.enums.UrgeStatusEnum;
import com.nyd.zeus.entity.BillProduct;
import com.nyd.zeus.api.BillRepayService;
import com.nyd.zeus.api.zzl.ZeusForWHServise;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.model.ReadyDistributionVo;
import com.nyd.zeus.model.UrgeOverdueReq;
import com.nyd.zeus.model.UrgeOverdueRespVo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.common.req.BillInfoTask;
import com.nyd.zeus.model.common.req.BillProductVO;
import com.nyd.zeus.model.common.response.BillInfoVO;
import com.nyd.zeus.model.common.response.BillRepayVo;
import com.nyd.zeus.model.helibao.PaymentVo;
import com.nyd.zeus.model.helibao.util.Uuid;

@Service("zeusForWHServise")
public class ZeusForWHServiseImpl implements ZeusForWHServise{
	
	private Logger log = LoggerFactory.getLogger(ZeusForWHServiseImpl.class);
	
	@Autowired
	private BillRepayService billRepayService;
	
	@Autowired
	private ZeusSqlService zeusSqlService;
	
	@Autowired
	private BillDao billDao;

	@Override
	public CommonResponse activeRepayment(PaymentVo paymentVo) {
		if(StringUtils.isNotBlank(paymentVo.getBillNo()) && StringUtils.isNotBlank(paymentVo.getBindId()) && StringUtils.isNotBlank(paymentVo.getBillNo()) && StringUtils.isNotBlank(paymentVo.getMobile())){
			return billRepayService.activeRepayment(paymentVo);
		}
		return null;
	}

	@Override
	public CommonResponse batchRepayTask(PaymentVo paymentVo) {
		
		return billRepayService.batchRepayTask(paymentVo);
	}

	@Override
	public CommonResponse batchOverDueTask(PaymentVo paymentVo) {
		return billRepayService.batchOverDueTask(paymentVo);
	}
	
	@Override
	public CommonResponse manageRepayment(PaymentVo paymentVo) {
		return billRepayService.mannagerRepayment(paymentVo);
	}

	@Override
	public CommonResponse flatAccount(PaymentVo paymentVo) {
		return billRepayService.flatAccount(paymentVo);
	}

	@Override
	public List<BillInfoVO> queryPayList(BillInfoTask task) {
		String sql = "select * from t_bill where delete_flag='0' and (bill_status='%s' or (bill_status ='%s' and promise_repayment_date>='%s' and promise_repayment_date<'%s')) order by promise_repayment_date desc";
		List<BillInfoVO> list = zeusSqlService.queryT(String.format(sql, BillStatusEnum.REPAY_OVEDUE.getCode(),BillStatusEnum.REPAY_ING.getCode(),task.getStartTime(),task.getEndTime()), BillInfoVO.class);
		return list;
	}

	@Override
	public List<BillInfoVO> queryOverDueList(BillInfoTask task) {
		String sql = "select * from t_bill where delete_flag='0' and bill_status !='%s' and promise_repayment_date<'%s'";
		List<BillInfoVO> list = zeusSqlService.queryT(String.format(sql, BillStatusEnum.REPAY_SUCESS.getCode(),task.getEndTime()), BillInfoVO.class);
		return list;
	}

	@Override
	public List<BillRepayVo> queryPayProceList(BillInfoTask task) {
		String sql = "select * from t_bill_repay where result_code ='3'";
		List<BillRepayVo> list = zeusSqlService.queryT(sql, BillRepayVo.class);
		return list;
	}

	@Override
	public boolean saveBillProduct(BillProductVO pro) {
		
		String billProSql = "select * from t_bill_product where order_no ='%s'";
    	List<BillProduct> list = zeusSqlService.queryT(String.format(billProSql, pro.getOrderNo()), BillProduct.class);
    	if(null != list && list.size()>0){
    		return false;
    	}
    	
	    String id= UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24();
	    BillProduct product = new BillProduct();
	    try {
	    	BeanUtils.copyProperties(pro, product);
	    	product.setId(id);
	    	if(StringUtils.isNotBlank(pro.getProductName())){
	    		product.setProductCode(pro.getProductName());
	    	}else{
	    		product.setProductCode("xxd");
	    	}
	    	billDao.saveBillProduct(product);
		} catch (Exception e) {
			log.error("订单产品表保存异常:"+e.getMessage());
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public PagedResponse<List<UrgeOverdueRespVo>> getOverdueList(
			UrgeOverdueReq req) {
		PagedResponse<List<UrgeOverdueRespVo>> pageResp = new PagedResponse<List<UrgeOverdueRespVo>>();
	     StringBuffer buff = new StringBuffer();
	     buff.append("select ");
	     buff.append(" bei.user_name,");
	     buff.append(" bei.user_mobile,");
	     buff.append(" bei.apply_time,");
	     buff.append(" b.user_id,");
	     buff.append(" b.order_no,");
	     buff.append(" b.repay_principle,");
	     buff.append(" b.promise_repayment_date,");
	     buff.append(" b.wait_repay_amount,");
	     buff.append(" b.overdue_days,");
	     buff.append(" (case when b.bill_status ='B003' then 4  else b.urge_status end) as urge_status,");
	     buff.append(" b.already_repay_amount");
	     buff.append(" from ");
	     buff.append(" (select * from t_bill where 1=1");
	     buff.append(" and (bill_status ='").append(BillStatusEnum.REPAY_OVEDUE.getCode()).append("'");
	     buff.append(" or urge_status >0)");
	     if(StringUtils.isNotBlank(req.getPromiseRepaymentStart())){
	    	 buff.append(" and promise_repayment_date >='").append(req.getPromiseRepaymentStart()+" 00:00:00").append("'");
	     }
	     if(StringUtils.isNotBlank(req.getPromiseRepaymentEnd())){
	    	 buff.append(" and promise_repayment_date <'").append(req.getPromiseRepaymentEnd()+" 23:59:59").append("'");
	     }
	     if(StringUtils.isNotBlank(req.getLoanNo())){
	    	 buff.append(" and order_no='").append(req.getLoanNo()).append("'");
	     }
	     buff.append(" ) b");
	     buff.append(" inner join ").append("").append("");
	     buff.append(" (select * from t_bill_extend_info where 1=1");
	     if(StringUtils.isNotBlank((req.getCustName()))){
	    	 buff.append(" and user_name = '").append(req.getCustName()).append("'");
	     }
	     if(StringUtils.isNotBlank(req.getMobile())){
	    	 buff.append(" and user_mobile='").append(req.getMobile()).append("'");
	     }
	     buff.append(" ) bei");
	     buff.append(" on b.order_no = bei.order_no");
	     buff.append(" where not EXISTS ");
	     buff.append("(select 1 from t_bill_distribution_record where order_no = b.order_no and  pay_status='2' and `status`='1') ");
	     buff.append(" order by b.promise_repayment_date desc");
	     String sql = buff.toString();
	     log.info("贷后管理-全部订单:"+sql);
	     List<UrgeOverdueRespVo> list = zeusSqlService.pageT(sql, req.getPageNo(), req.getPageSize(), UrgeOverdueRespVo.class);
	     if(null != list && list.size()>0){
	    	 for(UrgeOverdueRespVo vo:list){
	    		 String code = vo.getUrgeStatus();
	    		 vo.setUrgeStatusName(UrgeStatusEnum.getUrgeName(code));
	    	 }
	     }
	     long total = zeusSqlService.count(sql);
	     pageResp.setData(list);
	     pageResp.setTotal(total);
	     pageResp.setSuccess(true);
	    return pageResp;
	}

	@Override
	public PagedResponse<List<UrgeOverdueRespVo>> getOverdueAssignList(
			UrgeOverdueReq req) {
		PagedResponse<List<UrgeOverdueRespVo>> pageResp = new PagedResponse<List<UrgeOverdueRespVo>>();
	     StringBuffer buff = new StringBuffer();
	     buff.append("select ");
	     buff.append(" bei.user_name,");
	     buff.append(" bei.user_mobile,");
	     buff.append(" bei.apply_time,");
	     buff.append(" b.repay_principle,");
	     buff.append(" b.user_id,");
	     buff.append(" b.order_no,");
	     buff.append(" b.overdue_days,");
	     buff.append(" b.already_repay_amount,");
	     buff.append(" b.promise_repayment_date,");
	     buff.append(" b.wait_repay_amount,");
	     buff.append(" bd.receive_user_name,");
	     buff.append(" bd.create_time,");
	     buff.append(" (case when b.bill_status ='B003' then 4  else b.urge_status end) as urge_status");
	     buff.append(" from ");
	     buff.append(" (select * from t_bill where 1=1");
	     buff.append(" and (bill_status ='").append(BillStatusEnum.REPAY_OVEDUE.getCode()).append("'");
	     buff.append(" or urge_status >0)");
	     if(StringUtils.isNotBlank(req.getPromiseRepaymentStart())){
	    	 buff.append(" and promise_repayment_date >='").append(req.getPromiseRepaymentStart()+" 00:00:00").append("'");
	     }
	     if(StringUtils.isNotBlank(req.getPromiseRepaymentEnd())){
	    	 buff.append(" and promise_repayment_date <'").append(req.getPromiseRepaymentEnd()+" 23:59:59").append("'");
	     }
	     if(StringUtils.isNotBlank(req.getLoanNo())){
	    	 buff.append(" and order_no='").append(req.getLoanNo()).append("'");
	     }
	     if(StringUtils.isNotBlank(req.getUrgeStatus())){
	    	 Integer urge = UrgeStatusEnum.URGE_FOUR.getCode();
	    	 if(urge.toString().equals(req.getUrgeStatus())){
	    		 buff.append(" and bill_status ='").append(BillStatusEnum.REPAY_SUCESS.getCode()).append("'");
	    	 }else{
	    		 Integer status = Integer.valueOf(req.getUrgeStatus());
	    		 if(3 == status){
	    			 buff.append(" and urge_status ='").append(status).append("'");
	    			 buff.append(" and bill_status !='").append(BillStatusEnum.REPAY_SUCESS.getCode()).append("'");
	    		 }else{
	    			 buff.append(" and urge_status ='").append(status).append("'"); 
	    		 }
	    		
	    	 }
	     }
	     buff.append(" ) b");
	     buff.append(" inner join ");
	     buff.append(" (select * from t_bill_extend_info where 1=1");
	     if(StringUtils.isNotBlank((req.getCustName()))){
	    	 buff.append(" and user_name = '").append(req.getCustName()).append("'");
	     }
	     if(StringUtils.isNotBlank(req.getMobile())){
	    	 buff.append(" and user_mobile='").append(req.getMobile()).append("'");
	     }
	     buff.append(" ) bei");
	     buff.append(" on b.order_no = bei.order_no");
	     /*buff.append(" where  EXISTS ");
	     buff.append("(select 1 from t_bill_distribution_record where order_no = b.order_no and  pay_status='2' and `status`='1') ");
	     buff.append(" )t ");*/
	     buff.append(" inner join");
	     buff.append(" (select order_no,receive_user_name,create_time from t_bill_distribution_record where 1=1 and  pay_status='2' and `status`='1'");
	     if(StringUtils.isNotBlank(req.getCreateTimeStart())){
	    	  buff.append(" and create_time>='").append(req.getCreateTimeStart()+" 00:00:00").append("'");
	     }
	     if(StringUtils.isNotBlank(req.getCreateTimeEnd())){
	    	  buff.append(" and create_time<'").append(req.getCreateTimeEnd()+" 23:59:59").append("'");
	     }
	     if(StringUtils.isNotBlank(req.getReceiveUserName())){
	    	 buff.append(" and receive_user_name= '").append(req.getReceiveUserName()).append("'");
	     }
	     if(StringUtils.isNotBlank(req.getUserId())){
	    	 buff.append(" and receive_user_id='").append(req.getUserId()).append("'");
	     }
	      buff.append(" )bd");
	     buff.append(" on b.order_no =  bd.order_no ");
	     buff.append(" order by b.promise_repayment_date desc");
	     String sql = buff.toString();
	     log.info("贷后管理-提醒订单:"+sql);
	     List<UrgeOverdueRespVo> list = zeusSqlService.pageT(sql, req.getPageNo(), req.getPageSize(), UrgeOverdueRespVo.class);
	     if(null != list && list.size()>0){
	    	 for(UrgeOverdueRespVo vo:list){
	    		 String code = vo.getUrgeStatus();
	    		 vo.setUrgeStatusName(UrgeStatusEnum.getUrgeName(code));
	    	 }
	     }
	     long total = zeusSqlService.count(sql);
	     pageResp.setData(list);
	     pageResp.setTotal(total);
	     pageResp.setSuccess(true);
	    return pageResp;
	}

	
	

}
