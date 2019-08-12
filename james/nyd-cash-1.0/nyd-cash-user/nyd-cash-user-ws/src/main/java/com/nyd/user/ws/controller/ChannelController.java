package com.nyd.user.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.user.dao.mapper.GeetestConfigMapper;
import com.nyd.user.entity.Download;
import com.nyd.user.entity.UserSource;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.ClickInfo;
import com.nyd.user.model.SmsInfo;
import com.nyd.user.model.enums.RegisterCode;
import com.nyd.user.service.*;
import com.nyd.user.ws.util.ControllerUntil;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujx on 2017/12/5.
 * 流量接入接口
 */
@RestController
@RequestMapping("/")
public class ChannelController {

    private static Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private ClickService clickService;

    @Autowired
    DownloadService downloadService;

    @Autowired
    private LuosimaoService luosimaoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GeetestConfigMapper mapper;
    /**
     * 引流发送验证码
     * @param accountInfo
     * @return
     */
    @RequestMapping(value = "user/channel/smscode", method = RequestMethod.POST, produces = "application/json")
    public ResponseData click(@RequestBody AccountInfo accountInfo){
        ResponseData responseData = ResponseData.success();
        logger.info("user/channel/smscode" + accountInfo.toString());
        if (StringUtils.isBlank(accountInfo.getSequence()) || StringUtils.isBlank(accountInfo.getSole()) || StringUtils.isBlank(accountInfo.getDeviceId())){
            return ResponseData.error("参数错误");
        }
        ResponseData test = ifGeetest(accountInfo, accountInfo.getAppName(), accountInfo.getDeviceId());
        if (test != null) return test;
        if(accountInfo != null && StringUtils.isNotEmpty(accountInfo.getAccountNumber())){
            ResponseData luosimaoResult = luosimaoService.verifyResult(accountInfo);
            if(null!=luosimaoResult && "1".equals(luosimaoResult.getStatus())){
                logger.error("人机交互校验结果返回日志 ："+JSON.toJSONString(luosimaoResult));
                return luosimaoResult;
            }
            SmsInfo smsRequest = new SmsInfo();
            smsRequest.setMobile(accountInfo.getAccountNumber());
            smsRequest.setSmsType("1");
            smsRequest.setAppName(accountInfo.getAppName());
            responseData = userLoginService.sendMsgCode(smsRequest);
            redisTemplate.delete("geeTest:second" + accountInfo.getDeviceId());
        }
        return responseData;
    }
    private ResponseData ifGeetest(AccountInfo accountInfo, String appName, String key) {
        //查询所有配置极验的app
        List<String> strings = mapper.selectAllAppCode();
        if (strings.contains(appName)) {
            ResponseData test = getTest(accountInfo, key);
            if (!"0".equals(test.getStatus())) {
                return test;
            }
        }
        return null;
    }

    private ResponseData getTest(AccountInfo accountInfo,String key){
        //即验判断
        try {
            String sole = null;
            if ("1".equals(accountInfo.getSequence())) {
                sole = (String) redisTemplate.opsForValue().get("geeTest:first" + key);
            } else if ("2".equals(accountInfo.getSequence())) {
                sole = (String) redisTemplate.opsForValue().get("geeTest:second" + key);
            } else {
                return ResponseData.error("极验验证失败");
            }

            if (StringUtils.isBlank(sole)) {
                return ResponseData.error("极验验证失败");
            }
            if (!sole.equals(accountInfo.getSole())) {
                return ResponseData.error("极验验证失败");
            }
            return ResponseData.success();
        }catch (Exception e){
            logger.error("极验验证失败",e);
            return ResponseData.error("极验验证失败");
        }
    }

    /**
     * 引流点击
     * @param clickInfo
     * @return
     */
    @RequestMapping(value = "user/channel/click", method = RequestMethod.POST, produces = "application/json")
    public ResponseData click(@RequestBody ClickInfo clickInfo,HttpServletRequest request){
        logger.info("user/channel/click" + clickInfo.toString());
        String ip = ControllerUntil.getIPAddr(request);
        String referOrigin = request.getHeader("X-Requested-With");
        ResponseData responseData = ResponseData.success();
        if(clickInfo != null ){
            clickInfo.setCount(1);
            clickInfo.setIp(ip);
            clickInfo.setReferOrigin(referOrigin);
            responseData = clickService.saveClickInfo(clickInfo);
        }
        return responseData;
    }

    /**
     *引流渠道注册
     * @param accountInfo
     * @return
     */
    @RequestMapping(value = "user/channel/register", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveUser(@RequestBody AccountInfo accountInfo){
        logger.info("user/channel/register" + accountInfo.toString());
        ResponseData responseData;
        if(accountInfo != null && StringUtils.isNotEmpty(accountInfo.getAccountNumber())
                && StringUtils.isNotEmpty(accountInfo.getSource())){
            if(StringUtils.isNotEmpty(accountInfo.getSmsCode())){
                responseData = userLoginService.register(accountInfo);
            }else{
                responseData = userLoginService.channelRegister(accountInfo);
            }
        }else{
            //参数有误
            responseData = ResponseData.error(RegisterCode.PARAMETERS_ERROR.getMsg());
        }
        if (RegisterCode.ACCOUNT_EXISTS.getMsg().equals(responseData.getMsg())) {
            responseData.setStatus("2");
        }
        return responseData;
    }

    /**
     * 营销下载重定向
     * @param request
     * @param response
     */
    @RequestMapping(value = "user/down/redirect", method = RequestMethod.GET, produces = "application/json")
    public void redirect(String appName,HttpServletRequest request,HttpServletResponse response){
        try {
            Enumeration e = request.getHeaders("User-Agent");
            if (StringUtils.isBlank(appName)){
                appName = "nyd";
            }
            while (e.hasMoreElements()) {
                String name = (String)e.nextElement();
                if(name.toUpperCase().contains("Android".toUpperCase())){
                    Download download = downloadService.getDownload("Android",appName);
                    if(download != null){
                        response.sendRedirect(download.getDownloadUrl());
                    }
                }else if(name.toUpperCase().contains("iPhone".toUpperCase())){
                    Download download = downloadService.getDownload("iPhone",appName);
                    if(download != null){
                        response.sendRedirect(download.getDownloadUrl());
                    }
                }
            }
        } catch (Exception e) {
            logger.info("user/down/redirect" + e);
        }
    }

    @RequestMapping(value = "user/down/url",method = RequestMethod.POST, produces = "application/json")
    public ResponseData getMarketUrl(@RequestBody UserSource source) {
        ResponseData responseData = ResponseData.success();
        String appName = null;
        try {
            if (StringUtils.isBlank(source.getAppName())){
                appName = "xxd";
            }else {
                appName = source.getAppName();
            }
            Map map = new HashMap<>();
            Download android = downloadService.getDownload("Android",appName);
            if(android != null){
                map.put("android",android.getDownloadUrl());
            }
            Download ios = downloadService.getDownload("iPhone",appName);
            if(ios != null){
                map.put("ios",ios.getDownloadUrl());
            }
            responseData.setData(map);
        } catch (Exception e) {
            logger.error("get down url has exception ",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }
}
