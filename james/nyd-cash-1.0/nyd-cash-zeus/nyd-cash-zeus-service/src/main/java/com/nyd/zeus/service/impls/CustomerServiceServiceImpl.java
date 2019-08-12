package com.nyd.zeus.service.impls;

import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.zeus.api.zzl.CustomerServiceService;
import com.nyd.zeus.dao.PaymentRiskExcludeInfoDao;
import com.nyd.zeus.entity.PaymentRiskExcludeInfo;
import com.nyd.zeus.model.PaymentRiskExcludeInfoRequest;
import com.nyd.zeus.model.PaymentRiskExcludeInfoResult;
import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.model.common.PagedResponse;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.stereotype.Service;


@Service("customerServiceService")
public class CustomerServiceServiceImpl implements CustomerServiceService {
    private static Logger LOGGER = LoggerFactory.getLogger(CustomerServiceServiceImpl.class);
	@Autowired
	private PaymentRiskExcludeInfoDao paymentRiskExcludeInfoDao;
	/**
	 * 新增风控扣款排除表信息
	 * @param orderNo
	 * @return ResponseData
	 */
	public ResponseData save(String orderNo)  throws Exception {
        ResponseData responseData = ResponseData.success();
        try {
        	PaymentRiskExcludeInfo paymentRiskExcludeInfo=new PaymentRiskExcludeInfo();
        	paymentRiskExcludeInfo.setOrderNo(orderNo);
        	paymentRiskExcludeInfoDao.save(paymentRiskExcludeInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("新增风控扣款排除表信息异常");
            LOGGER.error("save PaymentRiskExcludeInfo error! orderNo = "+orderNo,e);
        }
        return responseData;
	}

    /**
     * 获取风控扣款排除表信息
     * @param qaymentRiskExcludeInfoRequest
     * @return ResponseData
     * @throws Exception
     */
    @Override
	public PagedResponse<List<PaymentRiskExcludeInfoResult>> queryList(PaymentRiskExcludeInfoRequest qaymentRiskExcludeInfoRequest) throws Exception {
		PagedResponse response = new PagedResponse();
        try {
        	Integer total=paymentRiskExcludeInfoDao.queryListCount(qaymentRiskExcludeInfoRequest);
        	if(total>0){
        		List<PaymentRiskExcludeInfoResult> list = paymentRiskExcludeInfoDao.queryList(qaymentRiskExcludeInfoRequest);
        		response.setData(list);
        	}
			response.setTotal(total);
			response.setPageNo(qaymentRiskExcludeInfoRequest.getPageNo());
			response.setPageSize(qaymentRiskExcludeInfoRequest.getPageSize());
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true); 
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("queryList PaymentRiskExcludeInfo error!",e.getMessage());
        }
        return response;
    }
	/**
	 * 根据贷款编号删除风控扣款排除表信息
	 * @param orderNo
	 * @return ResponseData
	 */
    public ResponseData deleteByOrderNo(String orderNo)  throws Exception{
        ResponseData responseData = ResponseData.success();
       
        try {
        	PaymentRiskExcludeInfoRequest qaymentRiskExcludeInfoRequest=new PaymentRiskExcludeInfoRequest();
        	List<String> orderNoList=new ArrayList<String>(1);
        	orderNoList.add(orderNo);
        	qaymentRiskExcludeInfoRequest.setOrderNoList(orderNoList);
        	List<PaymentRiskExcludeInfoResult> list = paymentRiskExcludeInfoDao.queryList(qaymentRiskExcludeInfoRequest);
        	if(list!=null&&list.size()!=0){
        		paymentRiskExcludeInfoDao.deleteById(list.get(0).getId());
        	}else{
        		responseData = ResponseData.error("不存在该条白名单信息");
                LOGGER.error("deleteByOrderNo PaymentRiskExcludeInfo error! orderNo = "+orderNo);
        	}
        } catch (Exception e) {
            responseData = ResponseData.error("删除风控扣款排除表信息异常");
            LOGGER.error("deleteByOrderNo PaymentRiskExcludeInfo error! orderNo = "+orderNo,e.getMessage());
        }
        return responseData;
	}
	/**
	 * 统计风控扣款排除表信息
	 * @param orderNo
	 * @return ResponseData
	 */
    public ResponseData queryListCount(PaymentRiskExcludeInfoRequest qaymentRiskExcludeInfoRequest)  throws Exception{
        ResponseData responseData = ResponseData.success();
        try {
        	long total = paymentRiskExcludeInfoDao.queryListCount(qaymentRiskExcludeInfoRequest);
    		responseData.setData(total);
        } catch (Exception e) {
            responseData = ResponseData.error("删除风控扣款排除表信息失败");
            LOGGER.error("queryList  error! userName = " + qaymentRiskExcludeInfoRequest.getUserName()+", phone="+qaymentRiskExcludeInfoRequest.getPhone(), e.getMessage());
        }
        return responseData;
	}
}
