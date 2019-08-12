package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 放款查询响应实体类
 * @author cm
 */
@Data
public class JxLoanQueryResponse implements Serializable {

    //状态码
    private String statusCode;

    //返回信息
    private String message;

    //标Id
    private String loanId;


    //状态   1-募集中；2-已放款；3-退单
    private int status;

    //满标时间(非必响应字段)
    private Date soldOutAt;

    //放款时间(非必响应字段)
//    private Date createdAt;
    private String createdAt;


    /**
     * status=2时  borrowerContract , investments, counselingContract有值
     */
    //借款协议下载地址
    private String borrowerContract;

    //出借人明细
    private investments Investments;

    //咨询服务协议下载地址
    private String counselingContract;


    /**
     * 出借人明细实体类
     */
    @Data
    private static class investments{

        //出借人姓名
        private String investorName;

        //出借金额
        private BigDecimal amount;

        //出借合同编号
        private String contractCode;

        //出借协议地址
        private String contractDownloadUrl;

        //出借人身份证号
        private String investorIdCardNumber;

    }




}
