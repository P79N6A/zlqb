package com.nyd.capital.model;

import com.nyd.capital.model.annotation.EncryptField;
import com.nyd.capital.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
public @Data
class WsmRequest implements Serializable {
    @RequireField
    private Integer mid;//商户id
    @RequireField
    private String shmyc; //商户秘钥串
    @RequireField
    private Integer cpm;//产品名称
    @RequireField
    private Integer timestamp;
    @RequireField
    private String fkxx;        //拼接风控信息（fkxx）字段，安文档填写。没有信息，键值照常填写，值为""
    @RequireField
    private String zfmx;        //拼接支付明细（zfmx）字段,字段内value的总和等于sqje
//    @RequireField
    private String sign; //签名 需要 先 加密 在 生成签名

    /**
     * ========================以下属于我方数据===============================
     **/
    @RequireField
    private String shddh; //商户订单号

    @RequireField
    @EncryptField
    private String xm; //姓名

    @RequireField
    @EncryptField
    private String sfzh;//身份证号

    private String sfzzpdz;//身份证照片地址

//    private String sfzzpbddz;//本地地址

    @RequireField
    private String sflx;//身份类型 1.在职 2.在读 3.待业
    @RequireField
    @EncryptField
    private String sjh;//手机号
    @RequireField
    private String dzxx;//地址信息
    @RequireField
    private String hyzk;//婚姻状况 1.未婚 2.已婚 3.丧偶 4.离婚
    @RequireField
    private String jkzk;//健康状态1.健康 2.良好 3.一般 4.较弱
    @RequireField
    private String zgxl;//最高学历 1.博士研究生 2.硕士研究生 3.大学本科 4.大学专科和专科学校 5.中等专业学校或中等技术学校 6.技术学院 7.高中 8.初中 9.小学 10.未知

    private String zy;//专业

    private String yx;//院校
    @RequireField
    @EncryptField
    private String gsmc;//公司名称
    @RequireField
    @EncryptField
    private String gsdh;//公司电话
    @RequireField
    private String yhklx;//1.借记卡 2.信用卡 银行卡类型
    @RequireField
    @EncryptField
    private String kh;//卡号
    @RequireField
    private String khh;//开户行
    @EncryptField
    private String ylsjh;//预留手机号
    //以分为单位的正整数
    @RequireField
    private Integer sqje;//申请金额
    @RequireField
    private String dkyt;//贷款用途1.消费2.汽车3.医美4.旅游5.教育6. 3C 7.家装 8.租房9.租赁 10. 农业
    private String cpmx;// 产品明细【贷款用途】不为1时，该项必填
    @RequireField
    private Integer qx;//期限
    private String jjfwxyqysj;//居间服务协议签约时间
    private String jjfwxyckdz;//居间服务协议查看地址
    private String casqxyqysj;//CA授权协议签约时间
    private String casqxyckdz;//CA授权协议查看地址
    private String dkxyqysj;//代扣协议签约时间
    private String dkxyckdz;//代扣协议查看地址
    @RequireField
    @EncryptField
    private String lxrxm1;//联系人姓名
    @RequireField
    @EncryptField
    private String lxrdh1;//联系人电话
    @RequireField
    private String lxrgx1;//联系人关系1.父母 2.配偶 3.亲属 4.朋友 5.同事 6.老师 7.同学 8.子女
    @EncryptField
    private String lxrxm2;//
    @EncryptField
    private String lxrdh2;//
    private String lxrgx2;//
    @RequireField
    private String qyszsheng;//签约所在省
    @RequireField
    private String qyszshi;//签约所在市
    private String sfdf;//是否代付
    private String dgkhh;//对公开户行
    private String dggsmc;//对公开户行公司名称
    private String dgkhhbh;//对公开户行编号
    private String dgkhhkh;//对公开户行卡号
    private String dgkhhsheng;//对公开户行省
    private String dgkhhshi;//对公开户行市
    //期限为1时 必填 期限大于1可以为空
    private Integer ts;//天数
    @RequireField
    private String on_line;//1.线上订单 0.线下订单

    private String dsfzfjybh;//第三方支付交易编号

    private String sfdk;//是否代扣
    private String zfje;//支付金额
    //居间服务费金额
    private String ktje;

}
