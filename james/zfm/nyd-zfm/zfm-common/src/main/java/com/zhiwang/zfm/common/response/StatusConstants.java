package com.zhiwang.zfm.common.response;

import java.util.HashMap;
import java.util.Map;


/**
 * 1.基本原则,尽量不修改已经定义的,只增加,不删除<br/>
 * 2.增加的code要加到static初始化map中<br/>
 */
public final class StatusConstants {

	/** 用于存储code-msg对应的map类 */
	public static Map<String, String> statusMap = new HashMap<String, String>();

	private StatusConstants() {

	}
	
	/** 返回状态标识 :responseCode */
	public static final String RESPONSE_CODE = "responseCode";
	
	public static final String ERROR_CODE = "0";

	/** 1 操作成功 - */
	public static final String SUCCESS_CODE = "1";
	public static final String OTHER_CODE_2 = "2";

	public static final String SYSTEM_OPERATION_SUCCESS_MSG = "操作成功";
	public static final String SYSTEM_OPERATION_ERROR_MSG = "操作失败";
	static {
		statusMap.put(SUCCESS_CODE, SYSTEM_OPERATION_SUCCESS_MSG);
		statusMap.put(ERROR_CODE, SYSTEM_OPERATION_SUCCESS_MSG);
	}
	
	/** ========================== 公共异常 =========================== */
	
	//系统标识
	public static final String SYSTEM_SIGN = "XD";
	// 系统异常
	public static final String SYS_ERROR = "A0000";
	public static final String SYS_ERROR_MSG = "操作失败,请稍后重试或联系在线客服";
	
	/** 提示用户参数不正确  ：参数不正确*/
	public static final String PARAM_ERROR = "A0001";
	public static final String PARAM_ERROR_MSG = "参数不正确";
	
	// 参数验证失败
	public static final String VALIDATE_ERROR = "A0002";
	public static final String VALIDATE_ERROR_MSG = "参数验证失败";
	
	// 验签失败
	public static final String SIGNATURE_ERROR = "A0003";
	public static final String SIGNATURE_ERROR_MSG = "验签失败";
	
	// 消息队列通知失败 
	public static final String ADD_QUEUE_ERROR = "A0004";
	public static final String ADD_QUEUE_ERROR_MSG = "加入队列失败";
	
	// 消息队列通知失败 
	public static final String ADD_PENDING_ERROR = "A0005";
	public static final String ADD_PENDING_ERROR_MSG = "挂机订单分配失败,未查询到在线的业务员";
	
	public static final String REFUSE_CODE = "A0006";
	public static final String REFUSE_CODE_MSG ="拒单原因不存在";
	
	// 添加邀请好友活动配置失败 
	public static final String INVITE_ERROR_CODE = "A0007";
	public static final String INVITE_ERROR_MSG ="仅允许存在一条规则";
	
	// 存在交易中的流水
	public static final String TRANSACTION_PROCESSING_CODE = "A0008";
	public static final String TRANSACTION_PROCESSING_MSG ="交易处理中，请稍后再试";
	
	// 没有绑卡记录
	public static final String NO_BANKCARD_ERROR_CODE = "A0009";
	public static final String NO_BANKCARD_ERROR_MSG ="请先绑定银行卡";
		
	// 不满足邀请好友奖励提现的条件
	public static final String RWARD_WITHDRAW_ERROR_CODE = "A0010";
	public static final String RWARD_WITHDRAW_ERROR_MSG ="您当前还不满足提现条件，请努力邀请好友吧";
	
	// 邀请好友活动没有开启
	public static final String INVITE_NO_OPREN_ERROR_CODE = "A0011";
	public static final String INVITE_NO_OPREN_ERROR_MSG ="活动已结束";
	
	static {
		statusMap.put(SYS_ERROR, SYS_ERROR_MSG);
		statusMap.put(PARAM_ERROR, PARAM_ERROR_MSG);
		statusMap.put(VALIDATE_ERROR, VALIDATE_ERROR_MSG);
		statusMap.put(SIGNATURE_ERROR, SIGNATURE_ERROR_MSG);
		statusMap.put(ADD_QUEUE_ERROR, ADD_QUEUE_ERROR_MSG);
	}
	
	/** ========================== 用户操作状态码  =========================== */
	
	public static final String REG_USERNAME_EXIST = "B0001";
	public static final String REG_USERNAME_EXIST_MSG = "手机号码已被注册。";
	
	public static final String SEND_MOBILE_MSG_ERROR = "B0002";
	public static final String SEND_MOBILE_MSG_ERROR_MSG = "发送短信失败";
	
	public static final String SEND_BANK_MOBILE_MSG_ERROR = "B0003";
	public static final String SEND_BANK_MOBILE_MSG_ERROR_MSG = "发送银行短信失败";
	
	public static final String REG_CUSTIC_EXIST = "B0004";
	public static final String REG_CUSTIC_EXIST_MSG = "身份证已被注册";
	
	public static final String LOGIN_ERROR = "B0005";
	public static final String LOGIN_ERROR_MSG = "用户名或密码不正确";
	
	public static final String LOGIN_ERROR_NUMBER_MAX = "B0006";
	public static final String LOGIN_ERROR_NUMBER_MAX_MSG = "今日密码错误已达上限，请明天再试。";
	
	public static final String LOGIN_ERROR_USER_DISABLE = "B0007";
	public static final String LOGIN_ERROR_USER_DISABLE_MSG = "账号无效，请联系客服。";
	
	public static final String UPDATE_PWD_OLDPWD_ERROR = "B0008";
	public static final String UPDATE_PWD_OLDPWD_ERROR_MSG = "原密码错误，请重新输入。";
	
	public static final String UPDATE_PWD_OLDNEWPWD_ERROR = "B0009";
	public static final String UPDATE_PWD_OLDNEWPWD_ERROR_MSG = "新密码不可与原密码一致。";
	
	public static final String CARDBIND_MOBILE_EXISIT_ERROR = "B0010";
	public static final String CARDBIND_MOBILE_EXISIT_ERROR_MSG = "手机号码已被使用";
	
	public static final String UPDATE_PWD_SUCCESS = "B0011";
	public static final String UPDATE_PWD_SUCCESS_MSG = "密码修改成功。";
	
	public static final String SEND_MOBILE_CODE_FREQUENTLY = "B0012";
	public static final String SEND_MOBILE_CODE_FREQUENTLY_MSG = "一分钟内只能发送一次验证码，请稍后重试。";
	
	public static final String CHK_MOBILE_CODE_ERROR = "B0013";
	public static final String CHK_MOBILE_CODE_ERROR_MSG = "请输入正确的短信验证码。";
	
	public static final String BANK_BIND_MOBILE_EXIST = "B0014";
	public static final String BANK_BIND_MOBILE_EXIST_MSG = "手机号码已存在";
	
	public static final String USER_EMPNAME_NOTEXIST = "B0015";
	public static final String USER_EMPNAME_NOTEXIST_MSG = "邀请码不存在，请重新输入。";
	

	public static final String LOGIN_INVEST_CUST_ERROR = "B0016";
	public static final String LOGIN_INVEST_CUST_ERROR_MSG = "理财用户请前往贝尔在线登录。";
	
	public static final String USER_USERNAME_NOTEXIST = "B0017";
	public static final String USER_USERNAME_NOTEXIST_MSG = "用户不存在！";
	
	public static final String PAYMENT_SUCCESS = "B0018";
	public static final String PAYMENT_SUCCESS_MSG = "缴费授权成功。";
	
	public static final String REPAYMENT_SUCCESS = "B0019";
	public static final String REPAYMENT_SUCCESS_MSG = "还款授权成功。";
	
	public static final String ACCOUNTOPEN_SUCCESS = "B0020";
	public static final String ACCOUNTOPEN_SUCCESS_MSG = "用户已开户。";
	
	public static final String UNCLEAREDBALANCE_ERROR = "B0021";
	public static final String UNCLEAREDBALANCE_ERROR_MSG = "电子账户余额不为零，请全部提现后再删除银行卡，如需帮助请拨打全国服务热线：0512-65858626。";
	
	public static final String UNCLEARED_ERROR = "B0022";
	public static final String UNCLEARED_ERROR_MSG = "尚有未结清的订单，无法在线换绑银行卡；如需帮助请拨打全国服务热线：0512-65858626。";
	
	public static final String BIND_SUCCESS= "B0023";
	public static final String BIND_SUCCESS_MSG = "该用户可以换绑卡！";
	
	public static final String OCR_ERROR_CODE= "B0024";
	public static final String OCR_ERROR_MSG = "今日身份证识别已达上限，请明天再试。";
	
	public static final String USER_INTENTIONAMOUNT_CODE= "B0025";
	public static final String USER_INTENTIONAMOUNT_MSG = "用户意向金额为空！";
	
	public static final String 	SAVE_PORTRAIT_SUCCESS = "B0026";
	public static final String  SAVE_PORTRAIT_SUCCESS_MSG = "活体信息保存成功。";
	
	public static final String 	SAVE_PORTRAIT_ERROR = "B0027";
	public static final String 	SAVE_PORTRAIT_ERROR_MSG = "活体信息保存失败。";
	
	public static final String 	OCR_VALIDATE_ICNUMBER_ERROR = "B0028";
	public static final String 	OCR_VALIDATE_ICNUMBER_ERROR_MSG = "此身份证已存在绑定用户，请重新填写。";
	
	public static final String 	QUERY_USER_CRMAPPLAY_ID_ERROR = "B0029";
	public static final String 	QUERY_USER_CRMAPPLAY_ID_ERROR_MSG = "该用户订单不存在！";
	
	public static final String 	QUERY_USER_INFO_NULL_CODE = "B0030";
	public static final String 	QUERY_USER_INFO_NULL_MSG = "暂无信息！";
	
	public static final String 	QUERY_IC_ERROR = "B0031";
	public static final String 	QUERY_IC_ERROR_MSG = "没有该用户的身份证信息。";
	
	public static final String 	QUERY_CREDITAUTH_ERROR = "B0032";
	public static final String 	QUERY_CREDITAUTH_ERROR_MSG = "不存在历史征信数据";
	
	public static final String 	JOB_TYPE_INFO_ERROR = "B0033";
	public static final String 	JOB_TYPE_INFO_ERROR_MSG = "没有填写工作信息";
	
	public static final String 	OCR_FRONT_NULL = "B0034";
	public static final String 	OCR_FRONT_NULL_MSG = "请先上传身份证正面信息。";
	
	public static final String 	QUERY_CREDIT_AUTH_BY_CRMAPPLAYID_NULL = "B0035";
	public static final String 	QUERY_CREDIT_AUTH_BY_CRMAPPLAYID_NULL_MSG = "您还没有完成授权，请完成授权后再次重试。";
	

	public static final String 	PARSE_ADDRESS_ERROR_CODE = "B0036";
	public static final String 	PARSE_ADDRESS_ERROR_MSG = "请输入正确的身份证地址，不可录入特殊字符。";
	
	public static final String 	RULE_ENGINE_REFUSE = "B0037";
	public static final String	RULE_ENGINE_REFUSE_MSG = "规则引擎拒单";
	
	public static final String 	RULE_ENGINE_SUCCESS = "B0038";
	public static final String	RULE_ENGINE_SUCCESS_MSG = "规则引擎通过";
	
	public static final String 	RULE_ENGINE_RECORD_NULL = "B0039";
	public static final String	RULE_ENGINE_RECORD_NULL_MSG = "规则引擎记录表没有数据";
	
	public static final String 	RULE_ENGINE_LIVE_ERROR = "B0040";
	public static final String	RULE_ENGINE_LIVE_ERROR_MSG = "认证失败，请重新尝试";
	
	public static final String 	RULE_ENGINE_FACE_ERROR = "B0041";
	public static final String	RULE_ENGINE_FACE_ERROR_MSG = "规则引擎人脸失败";
	
	public static final String 	USER_NO_REGISTER_ERROR = "B0042";
	public static final String	USER_NO_REGISTER_ERROR_MSG = "手机号码未注册，请注册后再登录";
	
	public static final String  ORDER_CREATE_NO_COOD = "B0043";
	public static final String	ORDER_CREATE_NO_MSG = "该用户存在未结清订单 或审批中订单，暂不能借款";
	
	public static final String  APP_WITHDRAW_AMOUNT_COOD = "B0044";
	public static final String	APP_WITHDRAW_AMOUNT_MSG = "提现金额保持两位小数。";
	
	public static final String  OCR_VALIDATE_THREE_NUMBER_CODE = "B0045";
	public static final String	OCR_VALIDATE_THREE_NUMBER_MSG = "请使用上次申请的身份证信息进行申请。";
	
	public static final String  ORDER_NOTIN_APPLAYSTATUS_CODE = "B0046";
	public static final String	ORDER_NOTIN_APPLAYSTATUS_MSG = "订单已提交成功,请勿重复提交。";
	
	public static final String  FOLLOW_INFO_BY_ICNUMBER_CODE = "B0047";
	public static final String	FOLLOW_INFO_BY_ICNUMBER_MSG = "查无数据!";
	
	public static final String  CUST_MOBILE_ERROR_CODE = "B0048";
	public static final String	CUST_MOBILE_ERROR_MSG = "手机号码格式不正确!";
	
	public static final String TERMS_AUTH_SUCCESS = "B0049";
	public static final String TERMS_AUTH_SUCCESS_MSG = "用户已完成还款授权";
	
	public static final String TERMS_AUTH_ERROR = "B0050";
	public static final String TERMS_AUTH_ERROR_MSG = "签约失败，请勿修改单笔限额和签约有效期。";
	
	public static final String OPENACCOUNT_IC_ERROR = "B0051";
	public static final String OPENACCOUNT_IC_ERROR_MSG = "开户失败,请使用本人身份证开户";
	
	public static final String OPENACCOUNT_BANK_ERROR = "B0052";
	public static final String OPENACCOUNT_BANK_ERROR_MSG = "开户失败,银行内部错误!";
	
	public static final String OPENACCOUNT_BINDCARD_ERROR = "B0053";
	public static final String OPENACCOUNT_BINDCARD_ERROR_MSG = "开户绑卡失败，请联系网站客服。";
	
	public static final String OPENACCOUNT_UNBINDCARD_ERROR = "B0054";
	public static final String OPENACCOUNT_UNBINDCARD_ERROR_MSG = "解绑存管账户绑定银行卡失败，请稍后再试";
	
	public static final String OPENACCOUNT_AUTH_OTHER_ERROR = "B0055";
	public static final String OPENACCOUNT_AUTH_OTHER_ERROR_MSG = "还款授权签约失败，请稍后再试。";
	
	public static final String OPENACCOUNT_BINDCARD_OTHER_ERROR = "B0056";
	public static final String OPENACCOUNT_BINDCARD_OTHER_ERROR_MSG = "存管账户绑卡失败，请稍后再试";
	
	public static final String OPENACCOUNT_BINDCARD_EXISTENCE_CODE = "B0057";
	public static final String OPENACCOUNT_BINDCARD_EXISTENCE_CODE_MSG = "该银行卡已签约，是否切换至该卡进行提现或还款操作？";
	
	public static final String BANKCARD_EXISTENCE_CODE = "B0058";
	public static final String BANKCARD_EXISTENCE_CODE_MSG = "银行卡与当前客户信息不一致，请仔细核对";
	
	public static final String REG_USERCHANNEL_INVALID = "B00059";
	public static final String REG_USERCHANNEL_INVALID_MSG = "该链接已失效。";
	
	static {
		statusMap.put(REG_USERNAME_EXIST, REG_USERNAME_EXIST_MSG);
		statusMap.put(SEND_MOBILE_MSG_ERROR, SEND_MOBILE_MSG_ERROR_MSG);
		statusMap.put(SEND_BANK_MOBILE_MSG_ERROR, SEND_BANK_MOBILE_MSG_ERROR_MSG);
		statusMap.put(LOGIN_ERROR_NUMBER_MAX, LOGIN_ERROR_NUMBER_MAX_MSG);
		statusMap.put(LOGIN_ERROR_USER_DISABLE, LOGIN_ERROR_USER_DISABLE_MSG);
		statusMap.put(UPDATE_PWD_OLDPWD_ERROR, UPDATE_PWD_OLDPWD_ERROR_MSG);
		statusMap.put(UPDATE_PWD_OLDNEWPWD_ERROR, UPDATE_PWD_OLDNEWPWD_ERROR_MSG);
		statusMap.put(CARDBIND_MOBILE_EXISIT_ERROR, CARDBIND_MOBILE_EXISIT_ERROR_MSG);
		statusMap.put(UPDATE_PWD_SUCCESS, UPDATE_PWD_SUCCESS_MSG);
		statusMap.put(SEND_MOBILE_CODE_FREQUENTLY, SEND_MOBILE_CODE_FREQUENTLY_MSG);
		statusMap.put(CHK_MOBILE_CODE_ERROR, CHK_MOBILE_CODE_ERROR_MSG);
		statusMap.put(BANK_BIND_MOBILE_EXIST, BANK_BIND_MOBILE_EXIST_MSG);
		statusMap.put(USER_EMPNAME_NOTEXIST, USER_EMPNAME_NOTEXIST_MSG);
		statusMap.put(LOGIN_INVEST_CUST_ERROR, LOGIN_INVEST_CUST_ERROR_MSG);
		statusMap.put(USER_USERNAME_NOTEXIST, USER_USERNAME_NOTEXIST_MSG);
		statusMap.put(PAYMENT_SUCCESS, PAYMENT_SUCCESS_MSG);
		statusMap.put(REPAYMENT_SUCCESS, REPAYMENT_SUCCESS_MSG);
		statusMap.put(ACCOUNTOPEN_SUCCESS, ACCOUNTOPEN_SUCCESS_MSG);
		statusMap.put(UNCLEAREDBALANCE_ERROR, UNCLEAREDBALANCE_ERROR_MSG);
		statusMap.put(UNCLEARED_ERROR, UNCLEARED_ERROR_MSG);
		
	}
	
	/** ========================== 银行操作状态码C  =========================== */
	public static final String BANK_OPENACCOUNT_ERROR = "C0001";
	public static final String BANK_OPENACCOUNT_ERROR_MSG = "调用银行开户接口失败";
	
	public static final String BANK_QUERYBALANCE_ERROR = "C0002";
	public static final String BANK_QUERYBALANCE_ERROR_MSG = "查询银行余额失败，请稍后重试";
	
	public static final String BANK_OPEN_STATUS_ERROR = "C0003";
	public static final String BANK_OPEN_STATUS_ERROR_MSG = "开通银行存管失败";
	
	public static final String UPDATE_BANKBIND_MOBILE_ERROR = "C0004";
	public static final String UPDATE_BANKBIND_MOBILE_ERROR_MSG = "修改绑定手机号码失败";
	
	public static final String REQ_BANK_SERVICE_ERROR = "C0005";
	public static final String REQ_BANK_SERVICE_ERROR_MSG = "调用银行服务失败";
	
	public static final String SAVE_BANK_LOG_ERROR = "C0006";
	public static final String SAVE_BANK_LOG_ERROR_MSG = "记录boc日志信息失败";
	
	public static final String PASSWORD_SET_NOTEXIST = "C0007";
	public static final String PASSWORD_SET_NOTEXIST_MSG = "交易密码未设置";
	
	
	public static final String PASSWORD_RESET_SUCCESS = "C0008";
	public static final String PASSWORD_RESET_SUCCESS_MSG = "重置交易密码成功！";
	
	public static final String BANK_QUERYBALANCE_SUCCESS = "C0009";
	public static final String BANK_QUERYBALANCE_SUCCESS_MSG = "查询余额成功！";
	
	public static final String UPDATE_BANKBIND_MOBILE_SUCCESS = "C0010";
	public static final String UPDATE_BANKBIND_MOBILE_SUCCESS_MSG = "修改绑定手机号码成功";
	
	public static final String QUERY_ACCOUNT_NULL = "C0011";
	public static final String QUERY_ACCOUNT_NULL_MSG = "用户未开通存管帐户！";
	
	public static final String BANK_SETPWD_ERROR = "C0012";
	public static final String BANK_SETPWD_ERROR_MSG = "调用银行设置交易密码接口失败";
	
	public static final String BANK_WITHDRAW_ERROR = "C0013";
	public static final String BANK_WITHDRAW_ERROR_MSG = "调用银行提现接口失败";
	
	public static final String BANK_SETPASSWORD_ERROR= "C0014";
	public static final String BANK_SETPASSWORD_ERROR_MSG = "存管账户交易密码设置失败,请稍后重试";
	
	public static final String BANK_OTHER_ERROR= "C00154";
	public static final String BANK_OTHER_ERROR_MSG = "未知原因,请联系客服。";
	
	public static final String BAILIAN_DAIFU_PROCESING_ERROR= "C00155";
	public static final String BAILIAN_DAIFU_PROCESING_ERROR_MSG = "交易已受理,请耐心等待。";
	
	
	static {
		statusMap.put(BANK_OPENACCOUNT_ERROR, BANK_OPENACCOUNT_ERROR_MSG);
		statusMap.put(BANK_QUERYBALANCE_ERROR, BANK_QUERYBALANCE_ERROR_MSG);
		statusMap.put(BANK_OPEN_STATUS_ERROR, BANK_OPEN_STATUS_ERROR_MSG);
		statusMap.put(UPDATE_BANKBIND_MOBILE_ERROR, UPDATE_BANKBIND_MOBILE_ERROR_MSG);
		statusMap.put(REQ_BANK_SERVICE_ERROR, REQ_BANK_SERVICE_ERROR_MSG);
		statusMap.put(SAVE_BANK_LOG_ERROR, SAVE_BANK_LOG_ERROR_MSG);
		statusMap.put(PASSWORD_SET_NOTEXIST, PASSWORD_SET_NOTEXIST_MSG);
		statusMap.put(BANK_SETPWD_ERROR, BANK_SETPWD_ERROR_MSG);
		statusMap.put(BANK_WITHDRAW_ERROR, BANK_WITHDRAW_ERROR_MSG);
	}
	
	/** ========================== 订单操作状态码D  =========================== */
	public static final String APP_ORDER_ERROR_CODE = "D0001";
	public static final String APP_ORDER_ERROR_MSG = "调用银行开户接口失败";
	
	public static final String APP_NO_LINKMANSTATUS_ERROR_CODE = "D0002";
	public static final String APP_NO_LINKMANSTATUS_ERROR_MSG = "请先填写婚属状态！";
	
	public static final String APP_NO_JOBSTATUS_ERROR_CODE = "D0003";
	public static final String APP_NO_JOBSTATUS_ERROR_MSG = "请先填写工作信息！";
	
	public static final String APP_LINKMANMOBILE_ERROR_CODE = "D0004";
	public static final String APP_LINKMANMOBILE_ERROR_MSG = "联系人号码不可重复！";
	
	
	public static final String APP_FRISTLINKMAN_MATE_ERROR_CODE = "D0005";
	public static final String APP_FRISTLINKMAN_MATE_ERROR_MSG = "已婚第一联系人必须是配偶！";
	
	public static final String APP_SECONDLINKMAN_MATE_ERROR_CODE = "D0006";
	public static final String APP_SECONDLINKMAN_MATE_ERROR_MSG = "已婚第二联系人必须是直系亲属!";
	
	public static final String APP_THIRDLINKMAN_MATE_ERROR_CODE = "D0007";
	public static final String APP_THIRDLINKMAN_MATE_ERROR_MSG = "已婚有工作 第三联系人必须是同事!";
	
	public static final String APP_FRISTLINKMAN_ERROR_CODE = "D0008";
	public static final String APP_FRISTLINKMAN_ERROR_MSG = "未婚第一联系人必须是直系亲属!";
	
	
	public static final String APP_SECONDLINKMAN_ERROR_CODE = "D0009";
	public static final String APP_SECONDLINKMAN_ERROR_MSG = "未婚有工作第二联系人必须是同事!";
	
	public static final String APP_QUERYJOBINFO_ERROR_CODE = "D0010";
	public static final String APP_QUERYJOBINFO_ERROR_MSG = "该用户订单中未填写工作信息！";
	
	// 拒单时间 未超过30天
	public static final String APP_APPLAY_ORDER_THIRTY_ERROR = "D0011";
	public static final String APP_APPLAY_ORDER_THIRTY_ERROR_MSG = "您暂时还不能再次申请借款，请几天后重新尝试。";
	
	public static final String APP_APPLAY_ORDER_OLDORDER_DATA_ERROR = "D0012";
	public static final String APP_APPLAY_ORDER_OLDORDER_DATA_ERROR_MSG = "历史订单数据异常";
	
	public static final String APP_APPLAY_UNCLERA_ORDER_ERROR = "D0013";
	public static final String APP_APPLAY_UNCLERA_ORDER_ERROR_MSG = "存在未结清的订单,暂不能再次申请借款。";
	
	public static final String APP_APPLAY_EXIST_REVIEW_ORDER_ERROR = "D0014";
	public static final String APP_APPLAY_EXIST_REVIEW_ORDER_ERROR_MSG = "您的借款申请正在审批中，请耐心等待，如有问题请联系客服。";
	
	public static final String APP_APPLAY_EXIST_REVIEW_HANGORDER_ERROR = "D0015";
	public static final String APP_APPLAY_EXIST_REVIEW_HANGORDER_ERROR_MSG = "您的借款申请正在取消中，请耐心等待，如有问题请联系客服。";
	
	public static final String ORDER_REFUSE_CODE = "D0017";
	public static final String ORDER_REFUSE_MSG = "该订单申请失败！";
	
	public static final String ORDER_SENDDATATOSEA_ERROR = "D0016";
	public static final String ORDER_SENDDATATOSEA_ERROR_MSG = "提交数据到审批失败，请稍后重试！";
	
	public static final String ORDER_WITHDRAWAMOUNT_ERROR = "D0017";
	public static final String ORDER_WITHDRAWAMOUNT_ERROR_MSG = "今日申请名额已达上限，请明日再试";
	
	public static final String ORDER_WITHDRAWAMOUNT_REPEAT_ERROR = "D0018";
	public static final String ORDER_WITHDRAWAMOUNT_REPEAT_ERROR_MSG = "提现申请提交，请勿重复申请。";
	
	public static final String ORDER_PAYCONTROL_HASCLEAR_ERROR = "D0019";
	public static final String ORDER_PAYCONTROL_HASCLEAR_ERROR_MSG = "订单已被结清，无法展期。";
	
	public static final String ORDER_NOTEXIST_ERROR = "D0020";
	public static final String ORDER_NOTEXIST_ERROR_MSG = "未查询到订单。";
	
	public static final String ORDER_PAYRECORD_LOAN_ERROR = "D0021";
	public static final String ORDER_PAYRECORD_LOAN_ERROR_MSG = "订单已被结清，无法还款。";
	
	public static final String ORDER_PAYRECORD_PROCESSING_ERROR = "D0022";
	public static final String ORDER_PAYRECORD_PROCESSING_MSG = "订单存在还款中数据，无法还款。";
	
	public static final String ORDER_PAYRECORD_NO_BANK_ERROR = "D0023";
	public static final String ORDER_PAYRECORD_NO_BANK_MSG = "未发现有效银行卡。";
	
	public static final String ORDER_PAYRECORD_UPDATE_ERROR = "D0024";
	public static final String ORDER_PAYRECORD_UPDATE_MSG = "数据更新异常。";
	
	public static final String ORDER_PAYRECORD_INSERT_PROCESSING_ERROR = "D0025";
	public static final String ORDER_PAYRECORD_INSERT__PROCESSING_MSG = "还款已受理,请稍后查看。";
	
	public static final String ORDER_PAYRECORD_PAY_ERROR = "D0026";
	public static final String ORDER_PAYRECORD_PAY_MSG = "还款失败。";
	
	public static final String ORDER_EXTENSION_PROCESSING_ERROR = "D0027";
	public static final String ORDER_EXTENSION_PROCESSING_ERROR_MSG = "订单存在处理中数据，无法展期。";
	
	public static final String ORDER_EXTENSION_APPLY_ERROR = "D0028";
	public static final String ORDER_EXTENSION_APPLY_ERROR_MSG = "申请展期失败，请稍后再试。";
	
	public static final String ORDER_PAYRECORD_EXEMPTION_ERROR = "D0029";
	public static final String ORDER_PAYRECORD_EXEMPTION_ERROR_MSG = "订单已被结清，无法豁免罚息。";
	
	public static final String ORDER_EXTENSION_HAS_PROCESSING_APPLY_ERROR = "D0030";
	public static final String ORDER_EXTENSION_HAS_PROCESSING_APPLY_ERROR_MSG = "申请展期失败，已存在处理中展期订单。";
	
	public static final String ORDER_CONTRACT_ERROR = "D00031";
	public static final String ORDER_CONTRACT_ERROR_MSG = "下载失败，该订单不存在可下载的合同。";
	
	public static final String ORDER_APPLY_EXTENSION_HAS_REPAYMENT_ERROR = "D0032";
	public static final String ORDER_APPLY_EXTENSION_HAS_REPAYMENT_ERROR_MSG = "订单存在还款中数据，无法展期。";
	
	public static final String ORDER_APPLY_REPAYMENT_HAS_EXTENSION_ERROR = "D0033";
	public static final String ORDER_APPLY_REPAYMENT_HAS_EXTENSION_ERROR_MSG = "订单存在展期中数据，无法还款。";
	
	static {
		statusMap.put(APP_NO_LINKMANSTATUS_ERROR_CODE, APP_NO_LINKMANSTATUS_ERROR_MSG);
		statusMap.put(APP_NO_JOBSTATUS_ERROR_CODE, APP_NO_JOBSTATUS_ERROR_MSG);
		statusMap.put(APP_LINKMANMOBILE_ERROR_CODE, APP_LINKMANMOBILE_ERROR_MSG);
		statusMap.put(APP_FRISTLINKMAN_MATE_ERROR_CODE, APP_FRISTLINKMAN_MATE_ERROR_MSG);
		statusMap.put(APP_SECONDLINKMAN_MATE_ERROR_CODE, APP_SECONDLINKMAN_MATE_ERROR_MSG);
		statusMap.put(APP_THIRDLINKMAN_MATE_ERROR_CODE, APP_THIRDLINKMAN_MATE_ERROR_MSG);
		statusMap.put(APP_FRISTLINKMAN_ERROR_CODE, APP_FRISTLINKMAN_ERROR_MSG);
		statusMap.put(APP_SECONDLINKMAN_ERROR_CODE, APP_SECONDLINKMAN_ERROR_MSG);
		statusMap.put(APP_QUERYJOBINFO_ERROR_CODE, APP_QUERYJOBINFO_ERROR_MSG);
	}
	
	
	/** ========================== 系统设置状态码E  =========================== */
	public static final String UPDATE_ERROR = "E0001";
	public static final String UPDATE_ERROR_MSG = "更新失败";
	
	public static final String REORDER_ERROR = "E0002";
	public static final String REORDER_ERROR_MSG ="排序码已存在";
	
	public static final String DICTIONARY_ERROR = "E0003";
	public static final String DICTIONARY_ERROR_MSG ="数据字典名称已存在";
	
	public static final String DICTIONARY_DETAIL_NAME_ERROR = "E0004";
	public static final String DICTIONARY_DETAIL_NAME_ERROR_MSG ="数据字典明细名称已存在";
	
	public static final String DICTIONARY_DETAIL_CODE_ERROR = "E0005";
	public static final String DICTIONARY_DETAIL_CODE_ERROR_MSG ="数据字典明细编码已存在";
	
	public static final String DICTIONARY_DETAIL_PRICE_ERROR = "E0006";
	public static final String DICTIONARY_DETAIL_PRICE_ERROR_MSG ="数据字典明细值已存在";
	
	public static final String DICTIONARY_NAME_ERROR = "E0007";
	public static final String DICTIONARY_NAME_ERROR_MSG ="数据字典名称已存在";
	
	public static final String DICTIONARY_CODE_ERROR = "E0008";
	public static final String DICTIONARY_CODE_ERROR_MSG ="数据字典编码已存在";
	
	public static final String DICTIONARY_PRICE_ERROR = "E0009";
	public static final String DICTIONARY_PRICE_ERROR_MSG ="数据字典值已存在";
	
	public static final String PERIODAMOUNT_DIVIDED_ERROR = "E0010";
	public static final String PERIODAMOUNT_DIVIDED_ERROR_MSG ="金额必须能被100整除";
	
	public static final String AMOUNTMIN_COMPARE_ERROR = "E0011";
	public static final String AMOUNTMIN_COMPARE_ERROR_MSG ="金额最大值必须大于金额最小值";
	
	public static final String AMOUNTSCALE_COMPARE_ERROR = "E0012";
	public static final String AMOUNTSCALE_COMPARE_ERROR_MSG ="金额刻度值必须小于金额最大值";
	
	public static final String AMOUNTSCALE_DIVISOR_ERROR = "E0013";
	public static final String AMOUNTSCALE_DIVISOR_ERROR_MSG ="金额刻度值必须为金额初始值、最小值和最大值的公约数";
	
	public static final String AMOUNTINITIAL_COMPARE_ERROR = "E0014";
	public static final String AMOUNTINITIAL_COMPARE_ERROR_MSG ="金额初始值必须在金额最大值（包含）和金额最小值（包含）之间";
	
	public static final String PERIODMIN_COMPARE_ERROR = "E0015";
	public static final String PERIODMIN_COMPARE_ERROR_MSG ="期限最大值必须大于期限最小值";
	
	public static final String PERIODSCALE_COMPARE_ERROR = "E0016";
	public static final String PERIODSCALE_COMPARE_ERROR_MSG ="期限刻度值必须小于期限最大值";
	
	
	public static final String PERIODINITIAL_COMPARE_ERROR = "E0017";
	public static final String PERIODINITIAL_COMPARE_ERROR_MSG ="期限初始值必须在期限最大值（包含）和期限最小值（包含）之间";
	
	public static final String VERSION_REPEAT_CODE = "E0018";
	public static final String VERSION_REPEAT_MSG ="版本号已存在";
	
	public static final String LENGTH_ERROR = "E0019";
	public static final String LENGTH_ERROR_MSG ="超出规定长度";
	
	public static final String TIME_COMPARE_ERROR = "E0020";
	public static final String TIME_COMPARE_ERROR_MSG ="开始时间不能大于结束时间";
	
	public static final String TYPE_CODE_ERROR = "E0021";
	public static final String TYPE_CODE_ERROR_MSG ="根据字典编码未获取到对应字典名称";
	
	public static final String NO_VERSION_ERROR = "E0022";
	public static final String NO_VERSION_ERROR_MSG ="未查询到最新版本";
	
	public static final String PERIODSCALE_DIVISOR_ERROR = "E0023";
	public static final String PERIODSCALE_DIVISOR_ERROR_MSG ="期限刻度值必须为期限初始值、最小值和最大值的公约数";
	
	//未挂起提示语
	public static final String HANGUP_CODE = "E0024";
	public static final String HANGUP_MSG = "此笔订单没有挂起";
	
	public static final String VERSION_CHK_CODE = "E0025";
	public static final String VERSION_CHK_MSG = "请输入正确的版本号，可输入数字和‘.’,版本号必须包含数字";
	
	static {
		statusMap.put(UPDATE_ERROR, UPDATE_ERROR_MSG);
		statusMap.put(REORDER_ERROR, REORDER_ERROR_MSG);
		statusMap.put(DICTIONARY_ERROR, DICTIONARY_ERROR_MSG);
		statusMap.put(DICTIONARY_DETAIL_NAME_ERROR, DICTIONARY_DETAIL_NAME_ERROR_MSG);
		statusMap.put(DICTIONARY_DETAIL_CODE_ERROR, DICTIONARY_DETAIL_CODE_ERROR_MSG);
		statusMap.put(DICTIONARY_DETAIL_PRICE_ERROR, DICTIONARY_DETAIL_PRICE_ERROR_MSG);
		statusMap.put(DICTIONARY_NAME_ERROR, DICTIONARY_NAME_ERROR_MSG);
		statusMap.put(DICTIONARY_CODE_ERROR, DICTIONARY_CODE_ERROR_MSG);
		statusMap.put(DICTIONARY_PRICE_ERROR, DICTIONARY_PRICE_ERROR_MSG);
		statusMap.put(PERIODAMOUNT_DIVIDED_ERROR, PERIODAMOUNT_DIVIDED_ERROR_MSG);
		statusMap.put(VERSION_REPEAT_CODE, VERSION_REPEAT_MSG);
	}
	
	/** ========================== 获取风控数据F  =========================== */
	
	public static final String 	DATACENTER_API_ERROR = "F0000";
	public static final String 	DATACENTER_API_ERROR_MSG = "请求风控数据中心网络异常";
	
	public static final String 	DATACENTER_GETORDERID_ERROR = "F0001";
	public static final String 	DATACENTER_GETORDERID_ERROR_MSG = "获取腾讯订单号异常";
	
	public static final String 	DATACENTER_GETOCR_INFO_ERROR = "F0002";
	public static final String 	DATACENTER_GETOCR_INFO_ERROR_MSG = "保存OCR订单异常";
	 
	public static final String 	DATACENTER_FINGERPRINT_ERROR = "F0003";
	public static final String 	DATACENTER_FINGERPRINT_ERROR_MSG = "获取OCR数据异常";
	
	public static final String 	DATACENTER_RULEENGINE_ERROR = "F0004";
	public static final String 	DATACENTER_RULEENGINE_ERROR_MSG = "获取规则引擎异常";
	
	public static final String 	DATACENTER_SEND_DEVICES_ERROR = "F0006";
	public static final String 	DATACENTER_SEND_DEVICES_ERROR_MSG = "发送手机设备信息异常";
	
	public static final String 	DATACENTER_SEND_MOBILEAUTH_ERROR = "F0007";
	public static final String 	DATACENTER_SEND_MOBILEAUTH_ERROR_MSG = "发送手机服务密码到运营商异常";
	
	public static final String 	DATACENTER_SEND_MOBILEAUTH_TASK_ERROR = "F0008";
	public static final String 	DATACENTER_SEND_MOBILEAUTH_TASK_ERROR_MSG = "发送手机服务密码到运营商任务失败";
	
	public static final String 	DATACENTER_CHK_MOBILE_SMSCODE_ERROR = "F0009";
	public static final String 	DATACENTER_CHK_MOBILE_SMSCODE_ERROR_MSG = "认证短信验证码不正确";
	
	public static final String 	DATACENTER_MOBILE_DEVICES_ERROR = "F0010";
	public static final String 	DATACENTER_MOBILE_DEVICES_ERROR_MSG = "当日手机设备信息已发送";
	
	public static final String 	DATACENTER_RULE_ERROR = "F0011";
	public static final String 	DATACENTER_RULE_ERROR_MSG = "系统异常，请稍后重试";
	
	public static final String 	DATACENTER_INCOME_ERROR = "F0012";
	public static final String 	DATACENTER_INCOME_ERROR_MSG = "获取收入信息失败";
	
	
	/** ========================== 挂起原因  =========================== */
	//主动挂起
	public static final String ACTIVE_HANGUP_REASON = "客户自主放弃";
	//自动挂起
	public static final String AUTO_HANGUP_REASON = "用户长时间未操作，系统自动挂起";
	//实名升级 环节编码
	public static final	String[] REALNAMEUPGRADE_NODE_CODE = {"000001-0002","000001-0003","000001-0004"};
	//申请填写 环节编码
	public static final	String[] APPLICATIONFILL_NODE_CODE = {"000001-0005","000001-0006","000001-0007","000001-0008","000001-0009","000001-0010"};
	//开户 环节编码
	public static final	String[] OPENACCOUNT_NODE_CODE = {"000002-0006"};
	//面审 环节编码
	public static final String[] VIDEOSIGNING_NODE_CODE = {"000002-0004"};
	//审批中第一步-工作信息编码
	public static final String JOB_INFO_CODE = "000001-0005";
	//审批中第一步-工作信息名称
	public static final String JOB_INFO_NAME = "工作信息";
	//需要复活的订单状态=拒单，且订单属于审批中（信息审核至合规检查环节）状态被拒，显示复活按钮；其他状态订单不显示复活按钮
	public static final String[] REVIEW_REFUSE_CODE = {"000002-0001","000002-0002","000002-0003","000002-0004","000002-0005"};
	//客服编码
	public static final String CUSTOMER_SERVICE_CODE = "000012";
	
	
	
	/** ========================== 消息中心G  =========================== */
	public static final String MESSAGE_PARAM = "G0001";
	public static final String MESSAGE_PARAM_MSG ="环节没有获取到对应消息体";
	
	/** ========================== 总控管理H  =========================== */
	public static final String ORDER_PARAM = "H0001";
	public static final String ORDER_PARAM_MSG ="请输入姓名或手机或身份证号";
	
	public static final String REVIVE_PARAM = "H0002";
	public static final String REVIVE_PARAM_MSG ="该笔订单不为拒单状态";
	
	public static final String CHOICE_PARAM = "H0003";
	public static final String CHOICE_PARAM_MSG ="请输入页面编号(1：审批中订单/挂起订单资源池 2：全部订单/我的处理列表)";
	
	public static final String REFUSE_REASON = "H0004";
	public static final String REFUSE_REASON_MSG ="请选择需要拒绝的订单";
	
	public static final String CUSTOMER_DISTRIBUTION = "H0005";
	public static final String CUSTOMER_DISTRIBUTION_MSG ="请选择需要分配的客户";
	
	public static final String EMPLOYEE_DISTRIBUTION = "H0006";
	public static final String EMPLOYEE_DISTRIBUTION_MSG ="请选择分配的业务员";
	
	public static final String SUCCESSFUL_TRADE ="00000000";
	public static final String SUCCESS ="成功";
	public static final String FAIL ="失败";
	public static final String DIFFERENCE_STATUS_HINT ="不可选择多种订单状态的拒单";
	
	/** ========================== 宝付协议支付F  =========================== */
	public static final String BAO_FOO_BIND_CARD_QUERY_ERROR = "F0001";
	public static final String BAO_FOO_BIND_CARD_QUERY_ERROR_MSG = "未查询到绑定关系";
	
	public static final String BAO_FOO_PRECOND_CARD_ERROR = "F0002";
	public static final String BAO_FOO_PRECOND_CARD_ERROR_MSG = "预绑卡失败";
	
	public static final String BAO_FOO_CONFIRM_CARD_ERROR = "F0003";
	public static final String BAO_FOO_CONFIRM_CARD_ERROR_MSG = "确认绑卡失败";
	
	public static final String BAO_FOO_BIND_CARD_QUERY_SUCCESS = "F0004";
	public static final String BAO_FOO_BIND_CARD_QUERY_SUCCESS_MSG = "存在绑卡记录";
	
	public static final String BAO_FOO_CANCEL_CARD_ERROR = "F0005";
	public static final String BAO_FOO_CANCEL_CARD_ERROR_MSG = "解绑卡失败";
}

