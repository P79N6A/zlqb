package com.nyd.zeus.api.zzl;

import com.nyd.zeus.model.helibao.vo.pay.req.BankCardUnbindVo;
import com.nyd.zeus.model.helibao.vo.pay.req.BankCardbindVo;
import com.nyd.zeus.model.helibao.vo.pay.req.BindCardSendValidateCodeVo;
import com.nyd.zeus.model.helibao.vo.pay.req.BindPaySendValidateCodeVo;
import com.nyd.zeus.model.helibao.vo.pay.req.ConfirmBindCardVo;
import com.nyd.zeus.model.helibao.vo.pay.req.ConfirmBindPayVo;
import com.nyd.zeus.model.helibao.vo.pay.req.FirstPayConfirmPayVo;
import com.nyd.zeus.model.helibao.vo.pay.req.FirstPaySendValidateCodeVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QueryOrderVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QuickPayBindCardPreOrderVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QuickPayBindPayPreOrderVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QuickPayConfirmPayVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QuickPayFirstPayPreOrderVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BankCardUnbindResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BankCardbindResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BindCardPreOrderResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BindCardSendValidateCodeResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BindPaySendValidateCodeResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.ConfirmBindCardResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.ConfirmBindPayResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.QueryOrderResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.QuickPayBindPayPreOrderResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.QuickPayConfirmPayResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.QuickPayCreateOrderResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.QuickPaySendValidateCodeResponseVo;


public interface HelibaoQuickPayService {
	

	//--------------首次支付预下单
	public QuickPayCreateOrderResponseVo quickPayFirstPayPreOrder(QuickPayFirstPayPreOrderVo requestVo);


	//----------------首次支付短信
	public QuickPaySendValidateCodeResponseVo firstPaySendValidateCode(FirstPaySendValidateCodeVo requestVo);


	//---------------首次支付确认支付
	
	public QuickPayConfirmPayResponseVo firstPayconfirmPay(FirstPayConfirmPayVo requestVo);


	//--------------绑卡预下单
	
	public BindCardPreOrderResponseVo quickPayBindCardPreOrder(QuickPayBindCardPreOrderVo requestVo);


	//---------------鉴权绑卡短信

	public BindCardSendValidateCodeResponseVo bindCardSendValidateCode(BindCardSendValidateCodeVo requestVo);


	//--------------鉴权绑卡
	
	public ConfirmBindCardResponseVo bindCard(ConfirmBindCardVo requestVo);



	//--------------绑卡支付预下单
	
	public QuickPayBindPayPreOrderResponseVo quickPayBindPayPreOrder(QuickPayBindPayPreOrderVo requestVo);



	//------------绑卡支付短信
	
	public BindPaySendValidateCodeResponseVo sendValidateCode(BindPaySendValidateCodeVo requestVo);


	//----------------绑卡支付

	public ConfirmBindPayResponseVo confirmBindPay(ConfirmBindPayVo requestVo);

	//----------------进入订单查询接口
	public QueryOrderResponseVo queryOrder(QueryOrderVo requestVo);
	
	//----------------银行卡解绑
	public BankCardUnbindResponseVo bankCardUnbind(BankCardUnbindVo requestVo);
	
	//----------------用户绑定银行卡信息查询
	public BankCardbindResponseVo bankCardbindList(BankCardbindVo requestVo);
	
	//----------------异步通知接口
	public String quickPayConfirmPay(QuickPayConfirmPayVo requestVo);

	

}
