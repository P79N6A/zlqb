package com.nyd.pay.api.zzl;

import java.io.File;

import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserQueryResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserQueryVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserUploadResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserUploadVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderContractSignatureQueryResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderQueryResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderQueryVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderVo;




public interface HelibaoEntrustedLoanService {
	
	/**
	 * 商户用户注册
	 * @param userVo
	 * @return
	 */
    public MerchantUserResVo userRegister(MerchantUserVo userVo,String custInfoId);

    /**
     * 商户用户查询
     * @param userVo
     * @return
     */
    public MerchantUserQueryResVo userQuery(MerchantUserQueryVo userVo,String custInfoId);

    /**
     * 商户用户资质上传
     * @param userVo
     * @param file
     * @return
     */
    public MerchantUserUploadResVo userUpload(MerchantUserUploadVo userVo, File tempFile,String custInfoId);

    /**
     * 用户资质查询
     * @param userVo
     * @return
     */
    public MerchantUserUploadResVo userUploadQuery(MerchantUserUploadVo userVo,String custInfoId);

    /**
     * 委托代付下单
     * @param orderVo
     * @return
     */
    public OrderResVo createOrder(OrderVo orderVo,String custInfoId);

    /**
     * 委托代付订单查询
     * @param orderVo
     * @return
     */
    public OrderQueryResVo orderQuery(OrderQueryVo orderVo,String custInfoId);

    /**
     * 获取合同地址(成功订单才有合同地址)
     * @param orderVo
     * @return
     */
    public OrderContractSignatureQueryResVo contractSignature(OrderQueryVo orderVo,String custInfoId);
    
    
    /**
     * 委托代付结果通知(主动通知，无请求参数)
     * @param orderVo
     * @return
     */
    public String notfyOrderQueryResVo(OrderQueryResVo orderVo);


}
