package com.nyd.capital.service.pocket.util;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liuqiu
 */
@Getter
@Component
public class PocketConfig {

    /**
     *口袋理财支付域名
     */
    @Value("${pocket.withdraw.url}")
    private String pocketWithdrawUrl;

    /**
     * 口袋理财放款
     */
    @Value("${pocket.withdraw.toPush}")
    private String pocketWithdraw;

    /**
     * 口袋理财放款查询
     */
    @Value("${pocket.withdraw.query}")
    private String pocketWithdrawQuery;

    /**
     * 口袋理财放款回调
     */
    @Value("${pocket.withdraw.callback}")
    private String pocketWithdrawCallback;

    /**
     * 口袋理财account
     */
    @Value("${pocket.order.account}")
    private String pocketOrderAccount;

    /**
     * 口袋理财pwd
     */
    @Value("${pocket.order.pwd}")
    private String pocketOrderPwd;

    /**
     * 口袋理财key
     */
    @Value("${pocket.order.key}")
    private String pocketOrderKey;

    /**
     * 口袋理财项目名称
     */
    @Value("${pocket.order.project.name}")
    private String pocketOrderProjectName;

    /**
     * 口袋理财创建订单
     */
    @Value("${pocket.order.create}")
    private String pocketOrderCreate;

    /**
     * 口袋理财查询订单
     */
    @Value("${pocket.order.query}")
    private String pocketOrderQuery;
    
    /**
     * 口袋理财放款key
     */
    @Value("${pocket.LoanOrder.key}")
    private String LoanOrderKey;
    /**
     * 口袋理财放款pwd
     */
    @Value("${pocket.LoanOrder.pwd}")
    private String LoanOrderPwd;
    
    /**
    * 口袋理财放款name
    */
   @Value("${pocket.LoanOrder.name}")
   private String LoanOrderName;
    
   /**
    * 口袋理财回调接口加密的Key
    */
   @Value("${pocket.callback.key}")
   private String pockCallbackKey;
   /**
    * 口袋理财接口版本号
    */
   @Value("${pocket.version}")
   private String pocketVersion;
   /**
    * 口袋理财平台标识
    */
   @Value("${pocket.platNo}")
   private String pocketPlatNo;
   /**
    * 口袋理财签名Key
    */
   @Value("${pocket.SignKey}")
   private String pocketSignKey;
   /**
    * 口袋理财加密Key
    */
   @Value("${pocket.desKey}")
   private String pocketDesKey;
   /**
    * 口袋理财url
    */
   @Value("${pocket.url}")
   private String pocketUrl;
   /**
    * 口袋理财江西银行开户后跳转地址
    */
   @Value("${pocket.openPage.retUrl}")
   private String pocketOpenPageRetUrl;
   /**
    * 口袋理财合规页面跳转地址
    */
   @Value("${pocket.termsAuthPage.retUrl}")
   private String pocketTermsAuthPageRetUrl;
   /**
    * 口袋理财提现页面跳转地址
    */
   @Value("${pocket.withdraw.retUrl}")
   private String pocketWithdrawRetUrl;
   /**
    * 口袋理财提现页面跳转地址
    */
   @Value("${pocket.complianceBorrowPage.retUrl}")
   private String pocketComplianceBorrowPageRetUrl;
   /**
    * 爬虫插件路径
    */
   @Value("${pocket.driver.location}")
   private String driverLocation;
   /**
    * 口袋理财缴费授权最大金额
    */
   @Value("${pocket.pay.amount}")
   private String pocketPayAmount;
   /**
    * 口袋理财缴费授权最后日期
    */
   @Value("${pocket.pay.mandate}")
   private String pocketPayMandate;
   /**
    * 口袋理财还款授权最大金额
    */
   @Value("${pocket.repay.amount}")
   private String pocketRepayAmount;
   /**
    * 口袋理财还款授权最后日期
    */
   @Value("${pocket.repay.mandate}")
   private String pocketRepayMandate;
}
