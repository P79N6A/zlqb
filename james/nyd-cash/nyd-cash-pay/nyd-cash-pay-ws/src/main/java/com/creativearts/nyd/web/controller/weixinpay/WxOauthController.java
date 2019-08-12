package com.creativearts.nyd.web.controller.weixinpay;


import com.creativearts.nyd.pay.config.PropKit;
import com.creativearts.nyd.pay.service.weixinpay.oauth.ApiConfigKit;
import com.creativearts.nyd.pay.service.weixinpay.oauth.SnsAccessToken;
import com.creativearts.nyd.pay.service.weixinpay.oauth.SnsAccessTokenApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 *
 */
@RestController
@RequestMapping("/pay/wxOauth")
public class WxOauthController extends WxOauthApiController{
	static Logger log = LoggerFactory.getLogger(WxOauthController.class);

	@RequestMapping("/index")
	public void index() throws IOException {
		int  subscribe=1;
		//用户同意授权，获取code
		String code= getRequest().getParameter("code");
		String state=getRequest().getParameter("state");
		if (code!=null) {
			String appId= ApiConfigKit.getApiConfig().getAppId();
			String secret=ApiConfigKit.getApiConfig().getAppSecret();
			//通过code换取网页授权access_token
			SnsAccessToken snsAccessToken= SnsAccessTokenApi.getSnsAccessToken(appId,secret,code);
//			String json=snsAccessToken.getJson();
//			String token=snsAccessToken.getAccessToken();
			String openId=snsAccessToken.getOpenid();
			System.out.println("openId>"+openId);
			//拉取用户信息(需scope为 snsapi_userinfo)
//			ApiResult apiResult=SnsApi.getUserInfo(token, openId);
//			
//			log.warn("getUserInfo:"+apiResult.getJson());
//			if (apiResult.isSucceed()) {
//				JSONObject jsonObject=JSON.parseObject(apiResult.getJson());
//				String nickName=jsonObject.getString("nickname");
//				//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
//				int sex=jsonObject.getIntValue("sex");
//				String city=jsonObject.getString("city");//城市
//				String province=jsonObject.getString("province");//省份
//				String country=jsonObject.getString("country");//国家
//				String headimgurl=jsonObject.getString("headimgurl");
//				String unionid=jsonObject.getString("unionid");
//				//获取用户信息判断是否关注
//				ApiResult userInfo = UserApi.getUserInfo(openId);
//				log.warn(JsonKit.toJson("is subsribe>>"+userInfo));
//				if (userInfo.isSucceed()) {
//					String userStr = userInfo.toString();
//					subscribe=JSON.parseObject(userStr).getIntValue("subscribe");
//				}
//				Users.me.save(openId, WeiXinUtils.filterWeixinEmoji(nickName), unionid, headimgurl, country, city, province, sex);
//			}

			getRequest().getSession(true).setAttribute("openId", openId);
			if (subscribe==0) {
				getResponse().sendRedirect(PropKit.get("subscribe_rul"));
			}else {
				//根据state 跳转到不同的页面
				if (state.equals("2222")) {
					getResponse().sendRedirect("http://www.cnblogs.com/zyw-205520/");
				}else if(state.equals("wxsubpay")){
					getResponse().sendRedirect("/towxsubpay");
				}else if(state.equals("wxpay")){
					getResponse().sendRedirect("/towxpay");
				}else{
					getResponse().sendRedirect("/oneqrpay/toPage?partnerId="+state);
				}
			}
			
			
		}else {
			renderText("code is  null");
		}
	}
	
}