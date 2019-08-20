package com.nyd.zeus.service.util.liandong;

import java.util.HashMap;
import java.util.Map;

public class Const {
	public static final String UMFCHILD_URL = "http://pay.soopay.net/spay_restPay/restpay.do";
	public static Map pkMap = new HashMap();
	public static final String UMPAYSTIE_SERVICE = "/pay/payservice.do";
	public static final String mer_id = "mer_id";
	public static final String MER_ID = "mer_id";
	public static final String CHILD_MER_ID = "merId";
	public static final String SIGN = "sign";
	public static final String PLAIN = "plain";
	public static final String RETCODE = "retCode";
	public static final String RET_CODE = "ret_code";
	public static final String RETMSG = "retMsg";
	public static final String RET_MSG = "ret_msg";
	public static final String trans_type = "trans_type";
	public static final String TRANS_TYPE = "trans_type";
	public static final String PLAT_APP_NAME_PAY = "spay";
	public static final String METHOD_GET = "get";
	public static final String METHOD_POST = "post";
	public static final String SUCCESS = "0000";
	public static final String SERVICE = "service";
	public static final String PAYSERVICE = "payservice";
	public static final String SIGN_TYPE = "sign_type";
	public static final String CHARSET = "charset";
	public static final String USERIP = "userIp";
	public static final String PLAT_URL = "http://pay.soopay.net";
	public static final String Encrypt_Paramters = "card_id,valid_date,cvv2,pass_wd,identity_code,card_holder,recv_account,recv_user_name,identity_holder,identityCode,cardHolder,mer_cust_name,account_name,bank_account,endDate,license_num,document_no";
	public static final String REQ_FRONT_PAGE_PAY = "mer_id,order_id,mer_date,amount,amt_type,interface_type";
	public static final String PAY_REQ_H5_FRONTPAGE = "mer_id,order_id,mer_date,amount,amt_type";
	public static final String PUBLICNUMBER_AND_VERTICALCODE = "mer_id,order_id,mer_date,amt_type,is_public_number";
	public static final String PLATTOMER_QUERYTRANS_FIELD = "mer_id,goods_id,order_id,mer_date,pay_date,amount,amt_type,mobile_id,gate_id,trans_type,trans_state,settle_date,bank_check,mer_priv";
	public static final String PLATTOMER_REVOKE_FIELD = "mer_id,amount";
	public static final String PLATTOMER_REFUND_FIELD = "mer_id,refund_no,amount";
	public static final String PLATTOMER_DIRECTREQPAY_FIELD = "mer_id,goods_id,order_id,mer_date";
	public static final String PAY_REQ_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	public static final String QUERY_ORDER_RULE = "service,charset,sign_type,mer_id,version,mer_date";
	public static final String MER_CANCEL_RULE = "service,charset,sign_type,mer_id,version,order_id,mer_date,amount";
	public static final String MER_REFUND_RULE = "service,charset,sign_type,mer_id,version,refund_no,order_id,mer_date,org_amount";
	public static final String DOWNLOAD_SETTLE_FILE_RULE = "service,sign_type,mer_id,version,settle_date";
	public static final String PAY_REQ_SPLIT_FRONT_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	public static final String PAY_REQ_SPLIT_BACK_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	public static final String SPLIT_REFUND_REQ_RULE = "service,charset,mer_id,sign_type,version,refund_no,order_id,mer_date,org_amount";
	public static final String PAY_REQ_SPLIT_DIRECT_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	public static final String PAY_RESULT_NOTIFY_RULE = "service,charset,mer_id,sign_type,version,trade_no,order_id,mer_date,pay_date,amount,amt_type,pay_type,settle_date,trade_state";
	public static final String SPLIT_REQ_RESULT_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,is_success";
	public static final String SPLIT_REFUND_RESULT_RULE = "service,charset,sign_type,mer_id,version,refund_no,order_id,mer_date";
	public static final String CREDIT_DIRECT_PAY_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,pay_type,card_id,valid_date,cvv2";
	public static final String DEBIT_DIRECT_PAY_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,pay_type,card_id";
	public static final String PRE_AUTH_DIRECT_REQ = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,pay_type,card_id,valid_date,cvv2";
	public static final String PRE_AUTH_DIRECT_PAY = "service,charset,mer_id,sign_type,version,order_id,trade_no,mer_date,amount,amt_type,pay_type";
	public static final String PRE_AUTH_DIRECT_CANCEL = "service,charset,mer_id,sign_type,version,order_id,trade_no,mer_date";
	public static final String PAY_TRANSFER_REGISTER = "service,charset,mer_id,res_format,version,sign_type,req_date,req_time,media_type,media_id,identity_type,identity_code,cust_name";
	public static final String PAY_TRANSFER_REQ = "service,charset,mer_id,ret_url,res_format,version,sign_type,order_id,mer_date,req_time,media_id,media_type,amount,fee_amount,recv_account_type,recv_bank_acc_pro,recv_account,recv_user_name,recv_gate_id,recv_type,purpose";
	public static final String PAY_TRANSFER_ORDER_QUERY = "service,charset,mer_id,res_format,version,sign_type,order_id,mer_date";
	public static final String PAY_TRANSFER_MER_REFUND = "service,charset,mer_id,res_format,version,sign_type,refund_no,order_id,mer_date";
	public static final String PRE_AUTH_DIRECT_QUERY = "service,charset,mer_id,sign_type,version,order_id,mer_date";
	public static final String PRE_AUTH_DIRECT_REFUND = "service,charset,sign_type,mer_id,version,order_id,mer_date,refund_no,refund_amount,org_amount";
	public static final String PRE_AUTH_DIRECT_SETTLE = "service,sign_type,mer_id,version,settle_date";
	public static final String CARD_AUTH = "service,charset,mer_id,sign_type,version,mer_date,card_id";
	public static final String REQ_SMS_VERIFYCODE = "service,mer_id,charset,sign_type,version,trade_no,media_id,media_type";
	public static final String PAY_CONFIRM = "service,mer_id,charset,sign_type,version,trade_no,pay_category,card_id";
	public static final String PAY_REQ_SHORTCUT_FRONT = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,pay_type,gate_id";
	public static final String PAY_REQ_SHORTCUT = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	public static final String FIRST_PAY_CONFIRM_SHORTCUT = "service,mer_id,charset,sign_type,version,trade_no,card_id,identity_code,card_holder";
	public static final String AGREEMENT_PAY_CONFIRM_SHORTCUT = "service,mer_id,charset,sign_type,version,trade_no,usr_pay_agreement_id";
	public static final String REQ_SMSVERIFY_SHORTCUT = "service,mer_id,sign_type,version,trade_no";
	public static final String QUERY_MER_BANK_SHORTCUT = "service,sign_type,charset,mer_id,version,pay_type";
	public static final String QUERY_MERCUST_BANK_SHORTCUT = "service,sign_type,charset,mer_id,version,pay_type";
	public static final String UNBIND_MERCUST_PROTOCOL_SHORTCUT = "service,sign_type,charset,mer_id,version";
	public static final String SPLIT_REQ_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date";
	public static final String QUERY_SPLIT_ORDER_RULE = "service,sign_type,charset,mer_id,version,order_id,mer_date";
	public static final String TRANSFER_DIRECT_REQ_RULE = "service,charset,mer_id,version,sign_type,order_id,mer_date,amount,recv_account_type,recv_bank_acc_pro,recv_account,recv_user_name";
	public static final String TRANSFER_QUERY_RULE = "service,charset,mer_id,version,sign_type,order_id,mer_date";
	public static final String MER_ORDER_INFO_QUERY = "service,sign_type,charset,mer_id,version,mer_date";
	public static final String MER_REFUND_QUERY = "service,sign_type,charset,mer_id,version,refund_no";
	public static final String ACTIVE_SCANCODE_ORDER = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,scancode_type";
	public static final String ACTIVE_SCANCODE_ORDER_NEW = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,scancode_type";
	public static final String PASSIVE_SCANCODE_PAY = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,auth_code,use_desc,scancode_type";
	public static final String QUERY_ACCOUNT_BALANCE = "service,charset,mer_id,version,sign_type";
	public static final String COMM_AUTH = "service,charset,mer_id,sign_type,version,auth_type,order_id";
	public static final String QUICK_PAY = "service,charset,mer_id,sign_type,version,order_id,order_id,mer_date,amount,amt_type,pay_type,gate_id";
	public static final String GET_MESSAGE = "service,charset,mer_id,sign_type,version,trade_no,media_id,media_type";
	public static final String QUICK_PAY_FIRST = "service,charset,mer_id,sign_type,version,trade_no,trade_no,verify_code,media_type,media_id";
	public static final String GET_BANK_MER = "service,charset,mer_id,sign_type,version,pay_type";
	public static final String CANCEL_SURRENDER = "service,charset,mer_id,sign_type,version";
	public static final String REFUND_INFO_REPLENISH = "service,charset,mer_id,sign_type,version,refund_no,card_holder,card_id";
	public static final String REQ_BIND_VERIFY_SHORCUT = "service,mer_id,sign_type,version,media_type,media_id,card_id";
	public static final String REQ_BIND_CONFIRM_SHORCUT = "service,mer_id,sign_type,charset,version,media_type,media_id,card_id";
	public static final String BIND_AGREEMENT_NOTIFY_SHORCUT = "service,mer_id,sign_type,version,mer_cust_id,media_type,media_id,usr_busi_agreement_id,usr_pay_agreement_id,gate_id,last_four_cardid,bank_card_type";
	public static final String BIND_REQ_SHORTCUT_FRONT = "service,mer_id,sign_type,charset,version,pay_type,gate_id,mer_cust_id";
	public static final String VERTICALCODE_ORDER = "service,mer_id,sign_type,charset,version,order_id,mer_date";
	public static final String EPAY_DIRECT_REQ = "service,mer_id,sign_type,charset,version,order_id,mer_date,amount,recv_account_type,recv_bank_acc_pro,recv_account,recv_user_name,cut_fee_type";
	public static final String EDRAW_TRANS_MAIN = "service,charset,mer_id,sign_type,version";
	public static final String QUERY_EDRAW_TRANS_MAIN = "service,charset,mer_id,sign_type,version";
	public static final String QUERY_ACCOUNT_BALANCE_MAIN = "service,charset,mer_id,sign_type,version";
	public static final String DZ_ORDER_REQ = "service,mer_id";
	public static final String DZ_CONFIRM_REQ = "service,mer_id,sign_type,charset,version,order_id,mer_date";
	public static final String DZ_QUERY_REQ = "service,mer_id,sign_type,charset,order_id,mer_date";
	public static final String COM_SIGN_ORDER = "service,mer_id,sign_type,charset,media_id,media_type,card_id";
	public static final String COM_BIND_CONFIRM = "service,mer_id,sign_type,charset,bind_id,verify_code";
	public static final String QUERY_USER_STAGING_COMAMT = "service,mer_id";
	public static final String GET_USERID_UNION = "service,mer_id";
}
