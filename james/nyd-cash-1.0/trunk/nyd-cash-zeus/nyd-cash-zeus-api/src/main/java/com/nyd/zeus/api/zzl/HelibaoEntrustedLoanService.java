package com.nyd.zeus.api.zzl;

import java.io.File;

import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserQueryResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserQueryVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserUploadResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserUploadVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderContractSignatureQueryResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderQueryResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderQueryVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderVo;


public interface HelibaoEntrustedLoanService {
	
	/**
	 * 商户用户注册
	 * @param mVo
	 * @return
	 */
    public MerchantUserResVo userRegister(MerchantUserVo mVo);

    /**
     * 商户用户查询
     * @param userVo
     * @return
     */
    public MerchantUserQueryResVo userQuery(MerchantUserQueryVo userVo);

    /**
     * 商户用户资质上传
     * @param userVo
     * @param file
     * @return
     */
    public MerchantUserUploadResVo userUpload(MerchantUserUploadVo userVo, String filePath);

    /**
     * 用户资质查询
     * @param userVo
     * @return
     */
    public MerchantUserUploadResVo userUploadQuery(MerchantUserUploadVo userVo);

    /**
     * 委托代付下单
     * @param orderVo
     * @return
     */
    public OrderResVo createOrder(OrderVo orderVo);

    /**
     * 委托代付订单查询
     * @param orderVo
     * @return
     */
    public OrderQueryResVo orderQuery(OrderQueryVo orderVo);

    /**
     * 获取合同地址(成功订单才有合同地址)
     * @param orderVo
     * @return
     */
    public OrderContractSignatureQueryResVo contractSignature(OrderQueryVo orderVo);
    
    
    /**
     * 委托代付结果通知(主动通知，无请求参数)
     * @param orderVo
     * @return
     */
    public String notfyOrderQueryResVo(OrderQueryResVo orderVo);


}
