package com.nyd.zeus.service.impls.zzl;

import com.nyd.order.model.YmtKzjrBill.enums.BillStatusEnum;
import com.nyd.zeus.api.zzl.ManagementFinancialService;
import com.nyd.zeus.api.zzl.ZeusForWHServise;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.dao.SettleAccountDao;
import com.nyd.zeus.dao.enums.UrgeStatusEnum;
import com.nyd.zeus.model.SettleAccount;
import com.nyd.zeus.model.WillSettleListRequest;
import com.nyd.zeus.model.WillSettleListVo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.helibao.PaymentVo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 后台财务管理
 * @author dengqingfeng
 * @date 2019年8月13日
 */
@Service("managementFinancialService")
public class ManagementFinancialServiceImpl implements ManagementFinancialService {

    private Logger log = LoggerFactory.getLogger(ManagementFinancialServiceImpl.class);
    @Autowired
    private ZeusSqlService zeusSqlService;
    @Autowired
    private ZeusForWHServise zeusForWHServise;
    @Autowired
    private SettleAccountDao settleAccountDao;
    /**
     * 贷后管理 待平账列表
     * @param willSettleListRequest
     * @return
     */
    public PagedResponse<List<WillSettleListVo>> getWillSettleList(WillSettleListRequest willSettleListRequest){
        PagedResponse<List<WillSettleListVo>> pageResp = new PagedResponse<List<WillSettleListVo>>();
        try {
            StringBuffer buff = new StringBuffer();
            buff.append("select b.user_id as userId, b.bill_no as billNo,");
            buff.append("i.loan_num as loanNum,");
            buff.append("b.bill_status as billStatus,");
            buff.append("b.promise_repayment_date as promiseRepaymentDate,");
            buff.append("i.user_name as userName,");
            buff.append("i.user_mobile as userMobile,");
            buff.append("b.cur_repay_amount+b.late_fee+b.penalty_fee as repayTotalAmount,");
            buff.append("b.late_fee as lateFee,");
            buff.append("b.penalty_fee as penaltyFee,");
            buff.append("b.manager_fee as managerFee,");
            buff.append("b.wait_repay_amount as waitRepayAmount,");
            buff.append("i.loan_time as loanTime,");
            buff.append("b.order_no as orderNo,");
            buff.append("b.repay_principle as repayPrinciple,");
            buff.append("b.cur_period as curPeriod,");
            buff.append("b.overdue_days as overdueDays,");
            buff.append("b.repay_interest as repayInterest,");
            buff.append("b.already_repay_amount as alreadyRepayAmount ");
            buff.append(" FROM t_bill b join t_bill_extend_info i on b.order_no=i.order_no where b.bill_status !='B003' ");

            if (StringUtils.isNotBlank(willSettleListRequest.getUserName())) {
                buff.append(" and i.user_name='").append(willSettleListRequest.getUserName()).append("'");
            }
            if (StringUtils.isNotBlank(willSettleListRequest.getUserMobile())) {
                buff.append(" and i.user_mobile='").append(willSettleListRequest.getUserMobile()).append("'");
            }
            if (StringUtils.isNotBlank(willSettleListRequest.getOrderNo())) {
                buff.append(" and i.order_no='").append(willSettleListRequest.getOrderNo()).append("'");
            }
            buff.append(" order by b.promise_repayment_date desc");
            String sql = buff.toString();
            log.info("贷后管理-:待平账列表sql=" + sql);
            List<WillSettleListVo> list = zeusSqlService.pageT(sql, willSettleListRequest.getPageNo(), willSettleListRequest.getPageSize(), WillSettleListVo.class);
            if (null != list && list.size() > 0) {
                for (WillSettleListVo vo : list) {
                    String code = vo.getBillStatus();
                    vo.setBillStatus(BillStatusEnum.getByCode(code).getValue());
                }
            }
            long total = zeusSqlService.count(sql);
            pageResp.setData(list);
            pageResp.setTotal(total);
            pageResp.setSuccess(true);
        }catch (Exception e){
            pageResp.setSuccess(false);
            pageResp.setMsg("系统异常，请联系管理员");
            log.error("贷后管理-:待平账列表异常, e="+e.getMessage());
        }
        return pageResp;
    }
    /**
     * 贷后管理 获取待平账借款信息
     * @param billNo
     * @return
     */
    public PagedResponse<List<WillSettleListVo>> getWillSettleDetail(String billNo){
        PagedResponse<List<WillSettleListVo>> pageResp = new PagedResponse<List<WillSettleListVo>>();
        try {
            StringBuffer buff = new StringBuffer();
            buff.append("select b.user_id as userId, b.bill_no as billNo,");
            buff.append("i.loan_num as loanNum,");
            buff.append("b.bill_status as billStatus,");
            buff.append("b.promise_repayment_date as promiseRepaymentDate,");
            buff.append("i.user_name as userName,");
            buff.append("i.user_mobile as userMobile,");
            buff.append("b.cur_repay_amount+b.late_fee+b.penalty_fee as repayTotalAmount,");
            buff.append("b.late_fee as lateFee,");
            buff.append("b.penalty_fee as penaltyFee,");
            buff.append("b.manager_fee as managerFee,");
            buff.append("b.wait_repay_amount as waitRepayAmount,");
            buff.append("i.loan_time as loanTime,");
            buff.append("b.order_no as orderNo,");
            buff.append("b.repay_principle as repayPrinciple,");
            buff.append("b.cur_period as curPeriod,");
            buff.append("b.overdue_days as overdueDays,");
            buff.append("b.repay_interest as repayInterest,");
            buff.append("b.already_repay_amount as alreadyRepayAmount ");
            buff.append(" FROM t_bill b join t_bill_extend_info i on b.order_no=i.order_no where 1=1 ");
            if (StringUtils.isNotBlank(billNo)) {
                buff.append(" and b.bill_no='").append(billNo).append("'");
            }
            String sql = buff.toString();
            log.info("贷后管理-:获取待平账借款信息sql=" + sql);
            List<WillSettleListVo> list = zeusSqlService.queryT(sql, WillSettleListVo.class);
            long total = 0L;
            if (null != list && list.size() > 0) {
                for (WillSettleListVo vo : list) {
                    String code = vo.getBillStatus();
                    vo.setStatusName(BillStatusEnum.getByCode(code).getValue());
                }
                total = list.size();
            }
            pageResp.setData(list);
            pageResp.setTotal(total);
            pageResp.setSuccess(true);
            return pageResp;
        }catch (Exception e){
            pageResp.setSuccess(false);
            pageResp.setMsg("系统异常，请联系管理员");
            log.error("贷后管理-:获取待平账借款信息异常 billNo="+billNo+", e="+e.getMessage());
            return pageResp;
        }
    }

    /**
     * 平账操作
     * @param settleAccount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseData settleAccount(SettleAccount settleAccount, PaymentVo paymentVo) throws Exception{
        ResponseData responseData=ResponseData.success();
        responseData.setMsg("平账成功");
        CommonResponse response=zeusForWHServise.flatAccount(paymentVo);
        if(response==null||(!response.isSuccess())){
            responseData.setMsg(response.getMsg());
            responseData.setStatus("1");
            return responseData;
        }
        settleAccountDao.save(settleAccount);
        return responseData;
    }
}
