package com.nyd.admin.model;

import com.nyd.admin.model.annotation.ExportConfig;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class WenTongExcelVo {


    @ExportConfig("订单号")
    private String orderNo;
    /**
     *
     */
    @ExportConfig("姓名")
    private String customerName;
    /**
     *
     */
    @ExportConfig("性别")
    private String sex;
    /**
     *
     */
    @ExportConfig("年龄")
    private Integer age;
    /**
     *
     */
    @ExportConfig("身份证号")
    private String IDNumber;

    /**
     *
     */
    @ExportConfig("移动电话")
    private String mobile;

    /**
     *
     */
    @ExportConfig("婚姻状态")
    private String maritalStatus;

    /**
     *
     */
    @ExportConfig("户口所在地")
    private String domicilePlace;

    /**
     *
     */
    @ExportConfig("学历")
    private String  education;

    /**
     *
     */
    @ExportConfig("现住宅地址")
    private String address;

    /**
     *
     */
    @ExportConfig("单位名称")
    private String company;

    /**
     *
     */
    @ExportConfig("职位级别")
    private String position;

    /**
     *
     */
    @ExportConfig("单位地址")
    private String companyAddress;

    /**
     *
     */
    @ExportConfig("工作薪资")
    private String salary;

    /**
     *
     */
    @ExportConfig("业余收入")
    private Integer amateurSalary;

    /**
     *
     */
    @ExportConfig("月总收入")
    private String totalSalary;

    /**
     *
     */
    @ExportConfig("姓名1(其他联系人)")
    private String otherName1;

    /**
     *
     */
    @ExportConfig("移动电话1")
    private String otherPhone1;

    /**
     *
     */
    @ExportConfig("关系1")
    private String relation1;

    /**
     *
     */
    @ExportConfig("姓名2(其他联系人)")
    private String otherName2;

    /**
     *
     */
    @ExportConfig("移动电话2")
    private String otherPhone2;

    /**
     *
     */
    @ExportConfig("关系2")
    private String relation2;

    /**
     *
     */
    @ExportConfig("银行卡号")
    private String cardNO;

    /**
     *
     */
    @ExportConfig("开户行名")
    private String bankName;

    /**
     *
     */
    @ExportConfig("开户省")
    private String bankProvince;

    /**
     *
     */
    @ExportConfig("开户市/县")
    private String bankCity;

    /**
     *
     */
    @ExportConfig("申请金额")
    private BigDecimal appliedAmount;

    /**
     *
     */
    @ExportConfig("申请期限")
    private Integer projectPeriod;

    /**
     *
     */
    @ExportConfig("借款用途")
    private String purpose;

    /**
     *
     */
    @ExportConfig("风控描述")
    private String describe;

    /**
     *
     */
    @ExportConfig("证件")
    private String credentials;


}
