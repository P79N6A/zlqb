package com.nyd.zeus.api.zzl;

import com.nyd.zeus.model.SettleAccount;
import com.nyd.zeus.model.WillSettleListRequest;
import com.nyd.zeus.model.WillSettleListVo;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.helibao.PaymentVo;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台财务管理
 * @author dengqingfeng
 * @date 2019年8月13日
 */
public interface ManagementFinancialService {
    /**
     * 贷后管理 待平账列表
     * @param willSettleListRequest
     * @return
     */
    public PagedResponse<List<WillSettleListVo>> getWillSettleList(WillSettleListRequest willSettleListRequest);
    /**
     * 贷后管理 获取待平账借款信息
     * @param billNo
     * @return
     */
    public PagedResponse<List<WillSettleListVo>> getWillSettleDetail(String billNo);
    /**
     * 平账操作
     * @param settleAccount
     * @return
     */
    public ResponseData settleAccount(SettleAccount settleAccount, PaymentVo paymentVo)throws Exception;
}
