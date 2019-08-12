package com.nyd.application.service.util;


import lombok.Data;

/**
 * @ClassName: Ticket
 * @Description: TODO
 * @Author:
 * @Date:
 */
@Data
public class Ticket {

    // 查询时间
    private String queryTime;
    // 报告编码
    private String reportCode;

    // 用户姓名
    private String userName;
    // 身份证号
    private String userCard;
    // 身份证地址
    private String userAddress;
    // 手机号
    private String userPhone;
    // 手机运营商
    private String phoneType;

    /**
     * 汇总
     */
    // 黑名单
    private String sumBlack;
    // 异常行为信息
    private String sumBehavior;
    // 多重借贷
    private String sumLoan;
    // 逾期信息
    private String sumOverdue;
    // 信息不一致风险
    private String sumMessDiff;
    // 负面信息关联数据
    private String sumNativeMess;
    // 信息多人共用风险
    private String sumShareMess;
    // 司法信息
    private String sumJudMess;
    // 总计
    private String sumTotal;

    /**
     * 黑名单
     */
    // 同行业贷款不良信息名单
    private String blackA001;
    // 联系人存在不良信息记录
    private String blackA002;
    // 联系人命中同行贷款不良信息名单
    private String blackA003;
    // 社交人群命中不良信息名单
    private String blackA004;
    // 不良信息名单关联过多
    private String blackA005;

    /**
     * 异常行为信息
     */
    // 用户激活借款行为异常
    private String behaviorB001;
    // 申请人网络异常
    private String behaviorB002;
    // 申请人异常评分过高
    private String behaviorB003;
    // 运营商信息不良
    private String behaviorB004;
    // 社交通讯信息异常
    private String behaviorB005;
    // 联系人号码异常数量过多
    private String behaviorB006;

    /**
     * 多重借贷
     */
    // 同行业贷款平台数目较多
    private String loanC001;
    // 多头申请信息关联过多
    private String loanC002;
    // 多头申请借贷过多
    private String loanC003;

    /**
     * 逾期信息
     */
    // 同地区逾期风险太高
    private String overdueD001;
    // 同单位异常信息太多
    private String overdueD002;
    // 命中征信源逾期名单
    private String overdueD003;
    // 命中征信源欺诈名单
    private String overdueD004;
    // 历史借贷逾期表现较差
    private String overdueD005;
    // 设备关联客户逾期大于7天用户较多
    private String overdueD006;

    /**
     * 信息不一致风险
     */
    // 联系人地区信息不符
    private String messDiffE001;
    // 申请人地区信息不符
    private String messDiffE002;
    // 申请人基本信息不符
    private String messDiffE003;
    // 联系人验证不通过
    private String messDiffE004;
    // 用户基本信息不一致
    private String messDiffE005;
    // 用户偿还验证不一致
    private String messDiffE006;
    // 申请人查询姓名不一致
    private String messDiffE007;
    // 联系人基本信息不一致
    private String messDiffE008;

    /**
     * 负面信息关联数据
     */
    // 申请人与不良信息人群联络频繁
    private String nativeMessF001;
    // 手机运营商信息不良
    private String nativeMessF002;
    // 手机运营商信息异常
    private String nativeMessF003;
    // 社交信息负面增加
    private String nativeMessF004;
    // 社交恶劣信息增加
    private String nativeMessF005;
    // 申请人与历史被拒客户联络频繁
    private String nativeMessF006;
    // 申请人重度负面信息过多
    private String nativeMessF007;
    // 申请人负面信息过多
    private String nativeMessF008;
    // 申请人与联系人联络异常
    private String nativeMessF009;
    // 申请人关联不良客户过多
    private String nativeMessF010;
    // 使用不良信息过多
    private String nativeMessF011;

    /**
     * 信息多人共用风险
     */
    // 申请人设备匹配数异常
    private String shareMessG001;
    // 申请人共用信息异常
    private String shareMessG002;
    // 申请人网络共用信息异常
    private String shareMessG003;
    // 设备共用信息不良
    private String shareMessG004;
    // 设备最近登陆异常
    private String shareMessG005;

    /**
     * 司法信息
     */
    // 司法信息查询异常
    private String judMessH001;
    // 手机存在不良信息
    private String judMessH002;
    // 欺诈风险信息评测较高
    private String judMessH003;
    // 银行卡风险得分较高
    private String judMessH004;
    // 银行卡套现风险较高
    private String judMessH005;
}