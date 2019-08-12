package com.nyd.batch.model;

import com.nyd.batch.model.annotation.ExportConfig;
import lombok.Data;

/**
 * Cong Yuxiang
 * 2018/1/2
 **/
@Data
public class CuimiInfo {

    @ExportConfig("个案序列号")
    private String serialNo;

    @ExportConfig("逾期阶段")
    private String overduePeriod;

//    @ExportConfig("佣金率")
//    private String commissionRate;

    @ExportConfig("委托日期")
    private String entrustDate;

    @ExportConfig("委托截止日期")
    private String entrustStopDate;

    @ExportConfig("姓名")
    private String name;
    @ExportConfig("性别")
    private String gender;
    @ExportConfig("证件号")
    private String idCard;
    @ExportConfig("本人手机")
    private String mobile;
//    @ExportConfig("家庭号码")
//    private String familyPhone;
    @ExportConfig("家庭地址")
    private String familyAddress;
//    @ExportConfig("家庭地址邮编")
//    private String familyPostCode;
    @ExportConfig("单位号码")
    private String companyPhone;
    @ExportConfig("单位名称")
    private String companyName;
    @ExportConfig("单位地址")
    private String companyAddress;
//    @ExportConfig("传真")
//    private String fax;
    @ExportConfig("省份")
    private String province;
    @ExportConfig("城市")
    private String city;
    @ExportConfig("区县")
    private String county;
//    @ExportConfig("单位地址邮编")
//    private String companyPostCode;

//    @ExportConfig("户籍地址邮编")
//    private String residencePostCode;
    @ExportConfig("户籍地址")
    private String  residenceAddress;
    @ExportConfig("职务")
    private String job;
    @ExportConfig("逾期天数")
    private String overDueDays;
    @ExportConfig("合同号")
    private String contractNo;

//    @ExportConfig("保单号")
//    private String insuranceNo;
//    @ExportConfig("被保险人")
//    private String insurancePeople;
    @ExportConfig("贷款日期")
    private String loanDate;
    @ExportConfig("贷款额")
    private String totalAmount;
    @ExportConfig("剩余本金")
    private String remainAmount;
    @ExportConfig("期数")
    private String periodCount;
    @ExportConfig("已还期数")
    private String periodReturn;
    @ExportConfig("每期还款金额")
    private String periodAmount;
    @ExportConfig("逾期日期")
    private String overDueDate;
//    @ExportConfig("理赔日期")
//    private String claimDate;
//    @ExportConfig("理赔金额")
//    private String claimAmount;
//    @ExportConfig("违约金生效日期")
//    private String damageEffectiveDate;
    @ExportConfig("违约金")
    private  String penalty;

//    @ExportConfig("逾期保费")
//    private String overDueInsurance;
//    @ExportConfig("逾期管理费")
//    private String overDueManFee;

    @ExportConfig("逾期利息")
    private String overDueInterest;
    @ExportConfig("逾期罚息")
    private String overDuePenalInterest;
//    @ExportConfig("扣款失败手续费")
//    private String withHoldFailFee;
    @ExportConfig("委托金额")
    private String entrustmentAmount;
//    @ExportConfig("是否撇账")
//    private String leaveTheBill;
    @ExportConfig("还款银行")
    private String repayBankName;
//    @ExportConfig("还款帐户名称")
//    private String repayBankAccName;
    @ExportConfig("还款帐号")
    private String repayBankAcc;
//    @ExportConfig("社保号")
//    private String socialNo;
//    @ExportConfig("房产1类型")
//    private String fc1lx;
//    @ExportConfig("房产1所有人")
//    private String fc1syr;
//    @ExportConfig("债务人家庭传真1")
//    private String jwrjtcz1;
//    @ExportConfig("债务人住址2")
//    private String jwrzz2;
//    @ExportConfig("房产2类型")
//    private String fc2lx;
//    @ExportConfig("房产2所有人")
//    private String fc2syr;
//    @ExportConfig("债务人家电2")
//    private String jwrjd2;
//    @ExportConfig("债务人家庭传真2")
//    private String jwrjtcz2;
//    @ExportConfig("债务人单位2名称")
//    private String jwrdw2mc;
//    @ExportConfig("债务人单位2地址")
//    private String jwrdw2dz;
//    @ExportConfig("债务人单位2电话")
//    private String jwrdw2dh;
//    @ExportConfig("债务人传真2")
//    private String jwrcz2;
//    @ExportConfig("私营业主公司类型")
//    private String syyzgslx;
//    @ExportConfig("成立日期")
//    private String clrq;
//    @ExportConfig("税务登记号")
//    private String swdjh;
//    @ExportConfig("工商登记号")
//    private String gsdjh;
//
//
//    @ExportConfig("配偶姓名")
//    private String spouseName;
//    @ExportConfig("配偶手机1")
//    private String spouseCellPhone1;
//    @ExportConfig("配偶手机2")
//    private String spouseCellPhone2;
//    @ExportConfig("配偶宅电")
//    private String spouseFamilyPhone;
//    @ExportConfig("配偶公司名称")
//    private String spouseCompanyName;
//    @ExportConfig("配偶公司地址")
//    private String spouseCompanyAddress;
//    @ExportConfig("配偶公司电话")
//    private String spouseCompanyPhone;
//    @ExportConfig("配偶证件号")
//    private String spouseIdCard;

    @ExportConfig("联系人1姓名")
    private String contactName1;
    @ExportConfig("联系人1关系")
    private String contactRelation1;
//    @ExportConfig("联系人1家电")
//    private String contactFamilyPhone1;
    @ExportConfig("联系人1手机")
    private String contactCellphone1;
//    @ExportConfig("联系人1手机2")
//    private String contactCellphone12;
//    @ExportConfig("联系人1证件号")
//    private String contactIdcard1;
//    @ExportConfig("联系人1地址")
//    private String contactAddress1;
//    @ExportConfig("联系人1单位")
//    private String contactCompany1;

    @ExportConfig("联系人2姓名")
    private String contactName2;
    @ExportConfig("联系人2关系")
    private String contactRelation2;
//    @ExportConfig("联系人2家电")
//    private String contactFamilyPhone2;
    @ExportConfig("联系人2手机")
    private String contactCellphone2;
//    @ExportConfig("联系人2手机2")
//    private String contactCellphone22;
//    @ExportConfig("联系人2证件号")
//    private String contactIdcard2;
//    @ExportConfig("联系人2地址")
//    private String contactAddress2;
//    @ExportConfig("联系人2单位")
//    private String contactCompany2;

    @ExportConfig("联系人3姓名")
    private String contactName3;
//    @ExportConfig("联系人3关系")
//    private String contactRelation3;
//    @ExportConfig("联系人3家电")
//    private String contactFamilyPhone3;
    @ExportConfig("联系人3手机")
    private String contactCellphone3;
//    @ExportConfig("联系人3手机2")
//    private String contactCellphone32;
//    @ExportConfig("联系人3证件号")
//    private String contactIdcard3;
//    @ExportConfig("联系人3地址")
//    private String contactAddress3;
//    @ExportConfig("联系人3单位")
//    private String contactCompany3;

    @ExportConfig("联系人4姓名")
    private String contactName4;
//    @ExportConfig("联系人4关系")
//    private String contactRelation4;
//    @ExportConfig("联系人4家电")
//    private String contactFamilyPhone4;
    @ExportConfig("联系人4手机")
    private String contactCellphone4;
//    @ExportConfig("联系人4手机2")
//    private String contactCellphone42;
//    @ExportConfig("联系人4证件号")
//    private String contactIdcard4;
//    @ExportConfig("联系人4地址")
//    private String contactAddress4;
//    @ExportConfig("联系人4单位")
//    private String contactCompany4;

    @ExportConfig("联系人5姓名")
    private String contactName5;
//    @ExportConfig("联系人5关系")
//    private String contactRelation5;
//    @ExportConfig("联系人5家电")
//    private String contactFamilyPhone5;
    @ExportConfig("联系人5手机")
    private String contactCellphone5;
//    @ExportConfig("联系人5手机2")
//    private String contactCellphone52;
//    @ExportConfig("联系人5证件号")
//    private String contactIdcard5;
//    @ExportConfig("联系人5地址")
//    private String contactAddress5;
//    @ExportConfig("联系人5单位")
//    private String contactCompany5;

    @ExportConfig("联系人姓名6")
    private String contactName6;
    @ExportConfig("联系人手机6")
    private String contactCellphone6;

    @ExportConfig("联系人姓名7")
    private String contactName7;
    @ExportConfig("联系人手机7")
    private String contactCellphone7;

    @ExportConfig("联系人姓名8")
    private String contactName8;
    @ExportConfig("联系人手机8")
    private String contactCellphone8;

    @ExportConfig("联系人姓名9")
    private String contactName9;
    @ExportConfig("联系人手机9")
    private String contactCellphone9;

    @ExportConfig("联系人姓名10")
    private String contactName10;
    @ExportConfig("联系人手机10")
    private String contactCellphone10;

//    @ExportConfig("催收员ID")
//    private String cuishouId;
//    @ExportConfig("催收区域ID")
//    private String cuishouAreaId;
//    @ExportConfig("备注6")
//    private String mvalueRatio;
//    @ExportConfig("备注1")
//    private String remark1;
//    @ExportConfig("备注2")
//    private String remark2;
//    @ExportConfig("备注3")
//    private String remark3;
//    @ExportConfig("备注4")
//    private String remark4;
//    @ExportConfig("备注5")
//    private String remark5;
//    @ExportConfig("备注6")
//    private String remark6;
//    @ExportConfig("原催收记录")
//    private String riginalCollectionRecord;






}
