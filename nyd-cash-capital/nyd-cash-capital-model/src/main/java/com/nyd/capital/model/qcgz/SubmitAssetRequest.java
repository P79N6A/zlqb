package com.nyd.capital.model.qcgz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 资产提交请求参数
 * @author cm
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmitAssetRequest {

    //渠道号
    private String channelCode;

    //侬要贷订单号
    private String orderId;

    //姓名
    private String name;

    //投资人列表(非必传)
    private String investorList;

    //标的类型    1:定向标 2:非定向标
    private String bidType;


    //性别   1:表示男性    2:表示女性
    private int sex;

    //手机号
    private String mobile;

    //身份证号
    private String idCardNumber;

    //银行名称
    private String bankName;

    //银行卡号
    private String bankCardNo;

    //借款用途(非必传)
    private String loanUse;

    //收入(非必传)
    private BigDecimal income;

    //住址(非必传)
    private String address;

    //户籍地址(非必传)
    private String birthPlace;

    //婚姻状况(非必传)     0 未婚 1 已婚
    private int marriageState;

    //借款期限   =1 时，表示天;  =2 时，表示期
    private int periods;

    //期限类型     1 表示单期 ;  2 表示多期
    private int periodsType;

    //借款金额
    private BigDecimal amount;

    //借款利率
    private BigDecimal rates;

    //签名
    private String sign;


}
