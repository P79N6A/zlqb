package com.nyd.pay.api.zzl;

import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BankCardUnbindVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BankCardbindVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BindCardSendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BindPaySendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.ConfirmBindCardVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.ConfirmBindPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.FirstPayConfirmPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.FirstPaySendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QueryOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayBindCardPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayBindPayPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayConfirmPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayFirstPayPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BankCardUnbindResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BankCardbindResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindCardPreOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindCardSendValidateCodeResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindPaySendValidateCodeResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.ConfirmBindCardResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.ConfirmBindPayResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QueryOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayBindPayPreOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayConfirmPayResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayCreateOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPaySendValidateCodeResponseVo;



public interface HelibaoQuickPayService {
	

	//--------------首次支付预下单
	public QuickPayCreateOrderResponseVo quickPayFirstPayPreOrder(QuickPayFirstPayPreOrderVo requestVo,String custInfoId);


	//----------------首次支付短信
	public QuickPaySendValidateCodeResponseVo firstPaySendValidateCode(FirstPaySendValidateCodeVo requestVo,String custInfoId);


	//---------------首次支付确认支付
	
	public QuickPayConfirmPayResponseVo firstPayconfirmPay(FirstPayConfirmPayVo requestVo,String custInfoId);


	//--------------绑卡预下单
	
	public BindCardPreOrderResponseVo quickPayBindCardPreOrder(QuickPayBindCardPreOrderVo requestVo,String custInfoId);


	//---------------鉴权绑卡短信

	public BindCardSendValidateCodeResponseVo bindCardSendValidateCode(BindCardSendValidateCodeVo requestVo,String custInfoId);


	//--------------鉴权绑卡
	
	public ConfirmBindCardResponseVo bindCard(ConfirmBindCardVo requestVo,String custInfoId);



	//--------------绑卡支付预下单
	
	public QuickPayBindPayPreOrderResponseVo quickPayBindPayPreOrder(QuickPayBindPayPreOrderVo requestVo,String custInfoId);



	//------------绑卡支付短信
	
	public BindPaySendValidateCodeResponseVo sendValidateCode(BindPaySendValidateCodeVo requestVo,String custInfoId);


	//----------------绑卡支付

	public ConfirmBindPayResponseVo confirmBindPay(ConfirmBindPayVo requestVo,String custInfoId);

	//----------------进入订单查询接口
	public QueryOrderResponseVo queryOrder(QueryOrderVo requestVo);
	
	//----------------银行卡解绑
	public BankCardUnbindResponseVo bankCardUnbind(BankCardUnbindVo requestVo,String custInfoId);
	
	//----------------用户绑定银行卡信息查询
	public BankCardbindResponseVo bankCardbindList(BankCardbindVo requestVo,String custInfoId);
	
	//----------------异步通知接口
	public String quickPayConfirmPay(QuickPayConfirmPayVo requestVo);

	

}
