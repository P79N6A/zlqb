package com.nyd.capital.api.service;

import com.nyd.capital.model.enums.ResultEnum;
import com.nyd.capital.model.response.KzjrStatusReponse;
import com.nyd.capital.model.vo.CheckStatusVo;
import com.nyd.capital.model.vo.KzjrOpenAccountVo;
import com.nyd.capital.model.vo.OpenAccountVo;
import com.nyd.capital.model.wt.WtCallbackResponse;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/

/**
 * 空中金融开户&解绑操作服务
 */
public interface RemitService {
//    ResponseData<CheckSendSmsDto> checkIfSendSms(CheckSendSmsVo vo);

    /**
     * 处理空中金融新户开户老户绑卡
     * @param vo OpenAccountVo
     * @return ResultEnum
     */
    ResultEnum processAccount(OpenAccountVo vo);

    /**
     * 请求远端的开户页面，并输出HTML
     * @param vo KzjrOpenAccountVo
     * @return HTML内容
     */
    String accountOpenPage(KzjrOpenAccountVo vo);

    /**
     * 查询空中金融（对应背后的江西银行）的账户状态，进行开户操作、解绑请求短信验证码等
     * @param vo CheckStatusVo
     * @return ResponseData<KzjrStatusReponse>
     */
    ResponseData<KzjrStatusReponse> checkStatusKzjr(CheckStatusVo vo);

    ResponseData wtLoan(List<WtCallbackResponse> wtCallbackList);
}
