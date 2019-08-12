package com.zhiwang.zfm.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.common.util.CodeUtil;


public class AppInterceptor implements HandlerInterceptor{
	
	
	public static final String CASHFINANCESECRET = "B9rW6eMAwRxa8";
	
	String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUqRTplrpcgcB6BJG4M0spVNocv"
					+ "3QWS8x24oddd7/C+HMwzAHFnw22YGO8s2VVD0Y4qq1SsFMySKzRTmfnZfXLqmwJPJ35"
					+ "vS7c7oPyEfoA4diQrGZUkhxAZ9+f2JQD98TImMxYBevAUn9ZDTvZONZsj4g+1gC+jk6U5aMvwF7dgQIDAQAB";
	
	String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANSpFOmWulyBwHoEkbgzSylU2hy/dBZLzHbih113v8L4czDMAcWfDbZgY7yzZVUPRjiqrVKwUzJIrNFO"
				   +"Z+dl9cuqbAk8nfm9Ltzug/IR+gDh2JCsZlSSHEBn35/YlAP3xMiYzFgF68BSf1kNO9k41myPiD7WAL6OTpTloy/AXt2BAgMBAAECgYBfgcRDmA8AZsGDyzOKj3bNDC+c"
				   +"w5smPEqKhfgXUSWNMbnCC40sLN+ira19XPBAgEigQH7w6QGkTqFWqyjc8pytVmcrLo0AVp/olpBVQljyojOfE/CqOommnHCwc+YFdXVu7kwXfzpyyNgBeQFnH9WXdJIK"
				   + "uCTlUY2UYohGvBwPwQJBAPbDNHfqIGH2rCstDWLJKFJuML2Sj/HtC0iMwRdkFnigyrYt+2syRH4EmteywAuqUf6EeqUQWy8dcRAy+TQK+7kCQQDcnxEz25k9b7TIGQcn"
				   + "+6Ip0u13Ti2mIEE8zDRKpotLkp+UBGEL0BJS+ORZOS68bRum+xecAKqRRjlKQGzsRiQJAkEA3FDC+sF1siF77Hyn8+gn8RjiuY8up9B9f4P0Lj81dPQSqP9692WFE4Jn"
				   + "AWhd4MxrsrNzkQZ+cx11QgJQzy9zOQJAUaXUufplReVXZE5O5a8snonfNC4MmtSJEDUKjS4BkyBaSm8sczC391kBOTv+XeGLQErgu+1LMqRog2BAb1it8QJBAKrw30/T"
				   + "NcC/vdFUuU0Ohx9jdYSNp5GhNVr/NS2kfl2mgCIScH5x8Xyj1Sgufi9Qz0ZgsofPybpKLd9BD1fyVi4=";
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
		//请求路径
		String path = request.getServletPath();
		// 验签
		String remoteSign = request.getHeader("XD-Signature");
		String nonce = request.getHeader("XD-Nonce");				// UUID
		String timestamp = request.getHeader("XD-Timestamp");		// 时间戳 单位/秒
		
		String osType = request.getHeader("OS-TYPE");				// 1 ANDROID 2 IOS
		String version = request.getHeader("version");				// APP版本号 		
		String device = request.getHeader("deviceType");			// 机型
		
		return true;
		//若key为空或值不对应 
//		if(ChkUtil.isEmpty(remoteSign) || ChkUtil.isEmpty(nonce) || ChkUtil.isEmpty(timestamp) 
//				|| ChkUtil.isEmpty(osType)|| ChkUtil.isEmpty(version)){
//			
//			return false;
//		}
//		
//		//若远程接口无此Header字段 直接返回404
//		if(ChkUtil.isEmpty(remoteSign) || ChkUtil.isEmpty(nonce) || ChkUtil.isEmpty(timestamp)){
//			return false;
//		}
//		String localSign = CodeUtil.hexSHA1(new StringBuilder(pubKey).append(nonce).append(timestamp).toString());
//		//返回比较结果
//		return localSign.equals(remoteSign);
	}

}
