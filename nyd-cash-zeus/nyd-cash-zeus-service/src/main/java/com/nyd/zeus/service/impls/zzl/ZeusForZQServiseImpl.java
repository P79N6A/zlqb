package com.nyd.zeus.service.impls.zzl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusForZQServise;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.dao.BillDao;
import com.nyd.zeus.dao.enums.BillStatusEnum;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.BillRepayListVO;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.common.response.BillRepayVo;
import com.nyd.zeus.service.enums.RequestCodeEnum;
import com.nyd.zeus.service.enums.RequestTypeEnum;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

@Service(value = "zeusForZQServise")
public class ZeusForZQServiseImpl implements ZeusForZQServise{
	
    private static Logger logger = LoggerFactory.getLogger(ZeusForZQServiseImpl.class);

    @Autowired
    private BillDao billDao;
    
    @Autowired
    private ZeusSqlService zeusSqlService;
    
    @Autowired
    private IdGenerator idGenerator;
    
	@Override
	public CommonResponse<BillInfo> queryBillInfoByOrderNO(String orderNo) {
		CommonResponse<BillInfo> common = new CommonResponse<BillInfo>();
		 try {
			 	String sql = "select * from t_bill where order_no = '%s'";
	        	List<BillInfo> billInfoList = zeusSqlService.queryT(String.format(sql, orderNo), BillInfo.class);
	            if(billInfoList.size()>0){
	            	common.setData(billInfoList.get(0));
	            }
	            common.setCode("1");
	            common.setMsg("操作成功");
	            common.setSuccess(true);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            logger.error("根据订单编号查询还款计划出现异常,请求参数:orderNo:" + orderNo);
	            common.setCode("0");
	            common.setMsg("操作失败");
	            common.setSuccess(false);
	        }
		return common;
	}
	
	
	
	@Override
	public CommonResponse<JSONObject> saveBill(BillInfo billInfo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		 try {
			 //bill编号
			 String billNo = idGenerator.generatorId(BizCode.BILL_NYD).toString();
			 billInfo.setBillNo(billNo);
			 billInfo.setBillStatus(BillStatusEnum.REPAY_ING.getCode());
			 billDao.save(billInfo);
	         common.setCode("1");
	         common.setMsg("操作成功");
	         common.setSuccess(true);
	     } catch (Exception e) {
        	e.printStackTrace();
            logger.error("根据订单编号查询还款计划出现异常,请求参数:{}",JSONObject.toJSONString(billInfo));
            common.setCode("0");
            common.setMsg("操作失败");
            common.setSuccess(false);
	     }
		return common;
	}

	
	@Override
	public CommonResponse<JSONObject> getBillProduct(String orderId) throws Exception{
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		 //根据订单id 查询 产品
//		 String billProductSql = "select * from t_bill_product";
		 String billProductSql = "select * from t_bill_product where order_no = '"+orderId+"'";
		 JSONObject jsonObject = zeusSqlService.queryOne(billProductSql);
		 common.setData(jsonObject);
		return common;
	}
	
	
	

	@Override
	public PagedResponse<List<BillRepayVo>> getBillRepayList(BillRepayListVO vo){
		PagedResponse<List<BillRepayVo>> pageResponse = new PagedResponse<List<BillRepayVo>>();
		 //根据订单id 还款记录
		try{
			 String sql = "select * from t_bill_repay where order_no = '"+vo.getOrderNo()+"' order by create_time desc";
			 List<BillRepayVo> list = zeusSqlService.pageT(sql,vo.getPageNo(), vo.getPageSize(), BillRepayVo.class);
			 if(null != list && list.size()>0){
				 for(BillRepayVo repay:list){
					 repay.setPayType(RequestTypeEnum.getValue(repay.getPayType()));
					 repay.setResultCode(RequestCodeEnum.getValue(repay.getResultCode()));
				 }
			 }
			 long total = zeusSqlService.count(sql.toString());
			 pageResponse.setCode("1");
			 pageResponse.setData(list);
			 pageResponse.setTotal(total);
			 pageResponse.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("根据订单id查询还款记录出现异常,请求参数:{}",JSONObject.toJSONString(vo));
			logger.error(e.getMessage());
			pageResponse.setCode("0");
			pageResponse.setSuccess(false);
		}
		return pageResponse;
	}
}
