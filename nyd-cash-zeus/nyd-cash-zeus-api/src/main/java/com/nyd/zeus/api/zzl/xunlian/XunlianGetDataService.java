package com.nyd.zeus.api.zzl.xunlian;

import java.util.Map;

import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.model.xunlian.req.IdentifyauthVO;
import com.nyd.zeus.model.xunlian.req.XunlianCancelBindVO;
import com.nyd.zeus.model.xunlian.req.XunlianChargeEnterVO;
import com.nyd.zeus.model.xunlian.req.XunlianChargeVO;
import com.nyd.zeus.model.xunlian.req.XunlianPaymentVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryChargeVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryPayVO;
import com.nyd.zeus.model.xunlian.req.XunlianSignVO;

/**
 * 讯联支付
 * @author admin
 *
 */
public interface XunlianGetDataService {

	/**
	 * 身份认证
	 * @param identifyauthVO
	 * @return
	 */
	public Map getIdentifyAuthData(IdentifyauthVO identifyauthVO,PayConfigFileVO payConfigFileVO);

	/**
	 * 签约
	 * @param xunlianSignVO
	 * @param payConfigFileVO
	 * @return
	 */
	public Map getSignData(XunlianSignVO xunlianSignVO,PayConfigFileVO payConfigFileVO);
	
	/**
	 * 协议支付
	 * @param xunlianSignVO
	 * @param payConfigFileVO
	 * @return
	 */
	public Map getPayData(XunlianPaymentVO xunlianSignVO,PayConfigFileVO payConfigFileVO);
	
	/**
	 * 协议支付查询
	 * @param xunlianSignVO
	 * @param payConfigFileVO
	 * @return
	 */
	public Map getQueryPayData(XunlianQueryPayVO xunlianQueryPayVO,PayConfigFileVO payConfigFileVO);

	
	public Map getCancelBindData(XunlianCancelBindVO xunlianCancelBindVO,PayConfigFileVO payConfigFileVO);

	
	public Map getChargeData(XunlianChargeVO xunlianChargeVO,PayConfigFileVO payConfigFileVO);

	public Map getQueryChargeData(XunlianQueryChargeVO xunlianQueryChargeVO,PayConfigFileVO payConfigFileVO);
	
	public Map getChargeEnter(XunlianChargeEnterVO xunlianChargeVO,PayConfigFileVO payConfigFileVO);

	

}
