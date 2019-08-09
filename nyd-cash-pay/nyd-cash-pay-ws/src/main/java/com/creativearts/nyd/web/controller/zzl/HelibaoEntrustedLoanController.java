package com.creativearts.nyd.web.controller.zzl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.nyd.pay.api.zzl.HelibaoEntrustedLoanService;



@RestController
@RequestMapping("/api/entrustedLoan")
@Api(description="合利宝-代付")
public class HelibaoEntrustedLoanController{
	
	
	
	 
	 @Autowired
	 private HelibaoEntrustedLoanService helibaoEntrustedLoanService;

		

	    /**
		 * 商户用户注册
		 * @param userVo
		 * @return
		 */
	    @PostMapping("/userRegister")
		@ApiOperation(value = "4.1 商户用户注册")
		@Produces(value = MediaType.APPLICATION_JSON)
	    public MerchantUserResVo userRegister(@ModelAttribute MerchantUserVo userVo){
	    	return helibaoEntrustedLoanService.userRegister(userVo,"");
	    }

	    /**
	     * 商户用户查询
	     * @param userVo
	     * @return
	     */
	    @PostMapping("/userQuery")
		@ApiOperation(value = "4.2 商户用户查询")
		@Produces(value = MediaType.APPLICATION_JSON)
	    public MerchantUserQueryResVo userQuery(@ModelAttribute MerchantUserQueryVo userVo){
	    	return helibaoEntrustedLoanService.userQuery(userVo,"");
	    }

	    /**
	     * 商户用户资质上传
	     * @param userVo
	     * @param file
	     * @return
	     */
	    @PostMapping("/userUpload")
		@ApiOperation(value = "4.3 商户用户资质上传")
		@Produces(value = MediaType.APPLICATION_JSON)
	    public MerchantUserUploadResVo userUpload(@ModelAttribute MerchantUserUploadVo userVo, @RequestParam MultipartFile file){
	    	return null;//helibaoEntrustedLoanService.userUpload(userVo,file);
	    }

	    /**
	     * 用户资质查询
	     * @param userVo
	     * @return
	     */
	    @PostMapping("/userUploadQuery")
		@ApiOperation(value = "4.4 用户资质查询")
		@Produces(value = MediaType.APPLICATION_JSON)
	    public MerchantUserUploadResVo userUploadQuery(@ModelAttribute MerchantUserUploadVo userVo){
	    	return helibaoEntrustedLoanService.userUploadQuery(userVo,"");
	    }

	    /**
	     * 委托代付下单
	     * @param orderVo
	     * @return
	     */
	    @PostMapping("/createOrder")
		@ApiOperation(value = "4.5 委托代付下单")
		@Produces(value = MediaType.APPLICATION_JSON)
	    public OrderResVo createOrder(@ModelAttribute OrderVo orderVo){
	    	return helibaoEntrustedLoanService.createOrder(orderVo,"");
	    }

	    /**
	     * 委托代付订单查询
	     * @param orderVo
	     * @return
	     */
	    @PostMapping("/orderQuery")
		@ApiOperation(value = "4.6 委托代付订单查询")
		@Produces(value = MediaType.APPLICATION_JSON)
	    public OrderQueryResVo orderQuery(@ModelAttribute OrderQueryVo orderVo){
	    	return helibaoEntrustedLoanService.orderQuery(orderVo,"");
	    }

	    /**
	     * 获取合同地址(成功订单才有合同地址)
	     * @param orderVo
	     * @return
	     */
	    @PostMapping("/contractSignature")
		@ApiOperation(value = "4.7 获取合同地址(成功订单才有合同地址)")
		@Produces(value = MediaType.APPLICATION_JSON)
	    public OrderContractSignatureQueryResVo contractSignature(@ModelAttribute OrderQueryVo orderVo){
	    	return helibaoEntrustedLoanService.contractSignature(orderVo,"");
	    }

	
}
