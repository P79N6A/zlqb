package com.creativearts.nyd.pay.service.yinshengbao;

import com.creativearts.nyd.pay.model.yinshengbao.DaiKouRequest;
import com.creativearts.nyd.pay.model.yinshengbao.NydYsbVo;
import com.creativearts.nyd.pay.model.yinshengbao.SubContractIdDelay;
import com.creativearts.nyd.pay.model.yinshengbao.YsbNotifyResponseVo;
import com.tasfe.framework.support.model.ResponseData;

public interface YsbPayService {

    public String signSimpleSubContract(NydYsbVo nydYsbVo);

    ResponseData collect(DaiKouRequest daiKouRequest);

    ResponseData signAndDaikou(NydYsbVo nydYsbVo, String type);

    ResponseData queryOrderStatus(NydYsbVo orderStatusQueryVo);

    ResponseData querySubContractId(NydYsbVo nydYsbVo);

    ResponseData subContractIdExtension(SubContractIdDelay subContractIdDelay);


    String callBackProcess(YsbNotifyResponseVo notifyResponseVo);
}
