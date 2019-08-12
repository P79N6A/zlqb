package com.zhiwang.zfm.common.util.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Mail {	
	private static String account ;
	private static String pwd ;
	private static String server ;
	private static String port ;
	
	@Value("${mail.account}")
	public void setAccount(String account) {
		Mail.account = account;
	}
	@Value("${mail.pwd}")
	public void setPwd(String pwd) {
		Mail.pwd = pwd;
	}
	@Value("${mail.server}")
	public void setServer(String server) {
		Mail.server = server;
	}
	@Value("${mail.port}")
	public void setPort(String port) {
		Mail.port = port;
	}
	
	//发送邮件
	public static String sentMail(String subject,String page,String email)throws Exception{  
	    MailSenderInfo mailInfo = setMail();    
	    mailInfo.setToAddress(email);    
	    mailInfo.setSubject(subject);    
	    mailInfo.setContent(page);
	    
	    //这个类主要来发送邮件   
	    SimpleMailSender.sendHtmlMail(mailInfo);//发送html格式
        return "success";    
   }
	
	public static MailSenderInfo setMail(){
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(server);    
	    mailInfo.setMailServerPort(port);    
	    mailInfo.setValidate(true);    
	    mailInfo.setUserName(account);    
	    mailInfo.setPassword(pwd);//您的邮箱密码    
	    mailInfo.setFromAddress(account);
	    mailInfo.setSubject("来自 芜湖润泰小额贷款股份有限公司的验证邮件");
	    return mailInfo;
	}
	
	public static String getHtml(String email, String randomNumber){
		StringBuffer url =new StringBuffer();
		url.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
		url.append("<head>");
		url.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
		url.append("<title>无标题文档</title>");
		url.append("<style>");
		url.append(" *{ padding:0; margin:0; font-size:13px; font-family: '宋体' ,'Arial Black', Gadget, sans-serif;}");
		url.append(" .emailcont{width:645px;  min-height:200px; margin:10px auto;  border:#CCC solid 1px;}");
		url.append(" .emailmain{ padding:10px; margin-top:0px;}"); 
		url.append(".welcome{ font-weight:700; color:#333; margin-bottom:15px; }");
		url.append(" .desfont{ line-height:30px;  padding-bottom:15px;}");
		url.append(" .footdes{ background:#f7f7f7; text-align:center; line-height:30px; margin-top:10px; border-top:#ccc dashed 1px;}");
		url.append(" .hideA { display:block;overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }");
		url.append(" .hideAuto { display:block;overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }");
		url.append(".tel {text-align: left;margin-left: 10px;} .footerText {text-align: right;margin-right: 10px;}");
		url.append("</style>");
		url.append("</head>");
		url.append("<body>");  
		url.append("  <div class='emailcont'><p></p>");
		url.append("   <div class='emailmain'>");
		url.append("     <div class='welcome'>亲爱的用户，您好！</div>");
		url.append("    <p class='desfont'>您已选择【" + email + "】作为您 【快捷易贷】 的联系电子邮件地址。为验证此电子邮件地址属于您，请在您的【快捷易贷】 完善信息页面输入下方的验证码:<br>");
		url.append(randomNumber + "</p>");
		url.append("       <p class='tel'>如有疑问，请联系我们，全国客服热线：0512-65858626</p>");
		url.append("   </div>");
		url.append("    <div class='footdes'>");	
		url.append("       <p class='footerText'>芜湖润泰小额贷款股份有限公司service@wuhuxiaodai.com</p>");
		url.append("    </div>");
		url.append("  </div>");
		url.append("</body>");
		url.append("</html>");
		return url.toString();
	}
	
	public static void main(String[] args) {
		try {
//			MsgContentEnum enu = MsgContentEnum.valueOf("PASSWORDTemplet");
//			JSONObject param = new JSONObject();
//			param.put("userName", "wengcyan");
//			String[] temp = enu.getMsg(param.toString());
//			Mail.sentMail(temp[2],temp[3], "907234164@qq.com");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
