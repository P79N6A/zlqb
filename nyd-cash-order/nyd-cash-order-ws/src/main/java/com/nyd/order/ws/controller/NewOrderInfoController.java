package com.nyd.order.ws.controller;

import com.nyd.order.model.dto.BorrowMqConfirmDto;
import com.nyd.order.model.dto.QueryWithholdOrderByBusinessOrderNoReqDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.WithHoldOrderContract;
import com.nyd.order.model.BaseInfo;
import com.nyd.order.model.OrderUpdatInfo;
import com.nyd.order.model.WithholdCallBackInfo;
import com.nyd.order.model.dto.BorrowConfirmDto;
import com.nyd.order.model.dto.BorrowDto;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.order.service.NewOrderInfoService;
import com.nyd.user.entity.User;
import com.tasfe.framework.support.model.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuqiu
 */
@RestController
@RequestMapping("/order")
public class NewOrderInfoController {
    private static Logger logger = LoggerFactory.getLogger(NewOrderInfoController.class);

    @Autowired
    private NewOrderInfoService orderInfoService;
    
    @Autowired
    private WithHoldOrderContract withholdOrderContract;


    /**
     * 借款信息
     *
     * @param borrowDto
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowInfo(@RequestBody BorrowDto borrowDto) {
        ResponseData responseData = orderInfoService.getBorrowInfo(borrowDto);
        return responseData;
    }

    /**
     * 借款信息(代扣版)
     *
     * @param borrowDto
     * @return ResponseData
     */
    @RequestMapping(value = "/borrowForPay/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowForPay(@RequestBody BorrowDto borrowDto) {
        ResponseData responseData = orderInfoService.getBorrowInfoForPay(borrowDto);
        return responseData;
    }

    /**
     * 借款确认
     *
     * @param borrowConfirmDto
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/newConfirm/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowNewConfirm(@RequestBody BorrowConfirmDto borrowConfirmDto) throws Exception{
            return orderInfoService.newBorrowInfoConfirm(borrowConfirmDto);
    }

    /**
     * 借款确认(代扣版)
     *
     * @param borrowConfirmDto
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/confirm/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowConfirm(@RequestBody BorrowConfirmDto borrowConfirmDto) throws Exception{
            return orderInfoService.borrowInfoConfirm(borrowConfirmDto);
    }


    @RequestMapping(value = "/lend/confirm", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowConfirmNew(@RequestBody BorrowConfirmDto borrowConfirmDto) throws Exception{
        return orderInfoService.borrowInfoConfirm(borrowConfirmDto);
    }

    @RequestMapping(value = "/mq/repost", method = RequestMethod.POST, produces = "application/json")
    public ResponseData repost(@RequestBody BorrowMqConfirmDto dto) throws Exception{
        logger.info("mq重新跑repost" + JSON.toJSONString(dto));
        return orderInfoService.mqRepost(dto);
    }

    @RequestMapping(value = "/mq/repost/all", method = RequestMethod.POST, produces = "application/json")
    public ResponseData repostAll(@RequestBody BorrowMqConfirmDto dto) throws Exception{
        List<String> lists = new ArrayList<>();
        lists.add("191401000004,101558322251123001");
        lists.add("191401000005,101558321045035001");
        lists.add("191401000012,101558367384185001");
        lists.add("191401000009,101558320778586001");
        lists.add("191401000010,101558320623296001");
        lists.add("191401100026,101558335878984001");
        lists.add("191401300007,101558332848331001");
        lists.add("191401000011,101558326240118001");
        lists.add("191401000013,101558320793789001");
        lists.add("191401000020,101558322347041001");
        lists.add("191401000026,101558327144667001");
        lists.add("191401000017,101558324986830001");
        lists.add("191401000021,101558322295534001");
        lists.add("191401000022,101558321703425001");
        lists.add("191401500004,101558338582846001");
        lists.add("191401100001,101558331317960001");
        lists.add("191401100004,101558321966128001");
        lists.add("191401100005,101558322588577001");
        lists.add("191401100006,101558322464161001");
        lists.add("191401100009,101558324832482001");
        lists.add("191401100007,101558322405110001");
        lists.add("191401100015,101558327494776001");
        lists.add("191401100017,101558324287592001");
        lists.add("191401100016,101558327245103001");
        lists.add("191401400047,101558335085663001");
        lists.add("191401100027,101558324776436001");
        lists.add("191401100018,101558324390406001");
        lists.add("191401400016,101558349492086001");
        lists.add("191401100020,101558324261139001");
        lists.add("191401100021,101558325514926001");
        lists.add("191401100023,101558324812100001");
        lists.add("191401200016,101558326507853001");
        lists.add("191401100034,101558324835488001");
        lists.add("191401100035,101558339193074001");
        lists.add("191401200002,101558326686274001");
        lists.add("191401200015,101558326167790001");
        lists.add("191401200017,101558326650098001");
        lists.add("191401200020,101558327647849001");
        lists.add("191401200018,101558326833657001");
        lists.add("191401200019,101558327664932001");
        lists.add("191401200033,101558327791355001");
        lists.add("191401200032,101558326796223001");
        lists.add("191401200040,101558327940623001");
        lists.add("191401200041,101558330361761001");
        lists.add("191401200042,101558328373122001");
        lists.add("191401200044,101558356415433001");
        lists.add("191401200043,101558328503877001");
        lists.add("191401200046,101558328885041001");
        lists.add("191401300001,101558329718742001");
        lists.add("191401400003,101558349507241001");
        lists.add("191401300002,101558331082170001");
        lists.add("191401300003,101558330717664001");
        lists.add("191401300006,101558331502340001");
        lists.add("191401400004,101558333675595001");
        lists.add("191401400013,101558333858977001");
        lists.add("191401400009,101558334701290001");
        lists.add("191401400007,101558333938206001");
        lists.add("191401400011,101558333620772001");
        lists.add("191401400046,101558343529906001");
        lists.add("191401400045,101558335136736001");
        lists.add("191401800005,101558349535984001");
        lists.add("191401400049,101558335903596001");
        lists.add("191401400050,101558336222245001");
        lists.add("191401500002,101558337509772001");
        lists.add("191401500003,101558337839093001");
        lists.add("191402100001,101558358111348001");
        lists.add("191401500006,101558338201424001");
        lists.add("191401500007,101558338692073001");
        lists.add("191401600003,101558341056569001");
        lists.add("191401600004,101558341357523001");
        lists.add("191401700001,101558343912396001");
        lists.add("191401700004,101558345638355001");
        lists.add("191401600006,101558342955257001");
        lists.add("191401600007,101558343253027001");
        lists.add("191401700002,101558343898366001");
        lists.add("191402300004,101558368200180001");
        lists.add("191401700006,101558347086466001");
        lists.add("191401800001,101558347119518001");
        lists.add("191401800002,101558348005735001");

        for (String str:lists) {
            BorrowMqConfirmDto borrowMqConfirmDto = new BorrowMqConfirmDto();
            borrowMqConfirmDto.setUserId(str.split(",")[0]);
            borrowMqConfirmDto.setOrderNo(str.split(",")[1]);
            borrowMqConfirmDto.setProductCode("xxd20181010003");
            logger.info("mq重新跑repost" + JSON.toJSONString(borrowMqConfirmDto));
            ResponseData responseData = orderInfoService.mqRepost(borrowMqConfirmDto);
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/mq/repost/all2", method = RequestMethod.POST, produces = "application/json")
    public ResponseData repostAll2(@RequestBody BorrowMqConfirmDto dto) throws Exception{
        List<String> lists = new ArrayList<>();
        lists.add("191541900011,101559577698269001") ;
        lists.add("191542300013,101559578033325001") ;
        lists.add("191550000001,101559579767354001") ;
        lists.add("191550000002,101559579978759001") ;
        lists.add("191550000003,101559580451799001") ;
        lists.add("191550100001,101559583967510001") ;
        lists.add("191550300001,101559591169137001") ;
        lists.add("191550500008,101559600316234001") ;
        lists.add("191541600060,101559605592530001") ;
        lists.add("191550700008,101559605719856001") ;
        lists.add("191550700010,101559606057155001") ;
        lists.add("191550700009,101559606420435001") ;
        lists.add("191550700013,101559607195288001") ;
        lists.add("191550800002,101559607434141001") ;
        lists.add("191550800007,101559608765087001") ;
        lists.add("191501900022,101559609000604001") ;
        lists.add("191550900001,101559610474357001") ;
        lists.add("191550900009,101559611155424001") ;
        lists.add("191550900010,101559612172968001") ;
        lists.add("191550900013,101559612420176001") ;
        lists.add("191531400016,101559612514777001") ;
        lists.add("191550900020,101559613083338001") ;
        lists.add("191550900024,101559613371458001") ;
        lists.add("191550900025,101559613625812001") ;
        lists.add("191531800059,101559614047833001") ;
        lists.add("191521100029,101559614402889001") ;
        lists.add("191531700376,101559614846056001") ;
        lists.add("191530600005,101559617051986001") ;
        lists.add("191531300229,101559617115731001") ;
        lists.add("191551100001,101559618189049001") ;
        lists.add("191551100010,101559618364320001") ;
        lists.add("191551100014,101559618900825001") ;
        lists.add("191551100024,101559619490109001") ;
        lists.add("191551100026,101559619744431001") ;
        lists.add("191521900017,101559619916015001") ;
        lists.add("191551100021,101559620099511001") ;
        lists.add("191531500214,101559620987398001") ;
        lists.add("191532000018,101559621688523001") ;
        lists.add("191541400008,101559621988068001") ;
        lists.add("191551200015,101559623106646001") ;
        lists.add("191551200017,101559623279802001") ;
        lists.add("191551200034,101559623524608001") ;
        lists.add("191551200016,101559623602835001") ;
        lists.add("191551200040,101559623705323001") ;
        lists.add("191551200027,101559623842465001") ;
        lists.add("191551100031,101559624141516001") ;
        lists.add("191551200101,101559624166774001") ;
        lists.add("191551200046,101559624297164001") ;
        lists.add("191551200075,101559624329365001") ;
        lists.add("191551200104,101559624337565001") ;
        lists.add("191551200103,101559624365275001") ;
        lists.add("191551200091,101559624407360001") ;
        lists.add("191551200069,101559624654261001") ;
        lists.add("191551200107,101559624968826001") ;
        lists.add("191551300009,101559625033339001") ;
        lists.add("191551300050,101559625650379001") ;
        lists.add("191551300030,101559625764993001") ;
        lists.add("191551300020,101559625866114001") ;
        lists.add("191551300054,101559625945531001") ;
        lists.add("191551300058,101559626013972001") ;
        lists.add("191551300055,101559626203008001") ;
        lists.add("191551300068,101559626252864001") ;
        lists.add("191551300073,101559626275385001") ;
        lists.add("191551300117,101559626320934001") ;
        lists.add("191551300071,101559626346726001") ;
        lists.add("191551300120,101559626371541001") ;
        lists.add("191551300075,101559626417822001") ;
        lists.add("191551300111,101559626454642001") ;
        lists.add("191551300079,101559626497979001") ;
        lists.add("191551300102,101559626600948001") ;
        lists.add("191551300115,101559626639969001") ;
        lists.add("191551300066,101559626695365001") ;
        lists.add("191551300143,101559626837644001") ;
        lists.add("191551300001,101559627079775001") ;
        lists.add("191551300148,101559627099272001") ;
        lists.add("191551300154,101559627155650001") ;
        lists.add("191551300166,101559627467770001") ;
        lists.add("191551300171,101559627509182001") ;
        lists.add("191551300177,101559627539569001") ;
        lists.add("191551300134,101559627555605001") ;
        lists.add("191551300106,101559627792658001") ;
        lists.add("191551300193,101559628038534001") ;
        lists.add("191522200079,101559628038739001") ;
        lists.add("191551300192,101559628235140001") ;
        lists.add("191551400003,101559628377011001") ;
        lists.add("191551300118,101559628543477001") ;
        lists.add("191551300189,101559628576310001") ;
        lists.add("191521200021,101559628665212001") ;
        lists.add("191551400005,101559629219847001") ;
        lists.add("191551400010,101559629846572001") ;
        lists.add("191551400017,101559630033491001") ;
        lists.add("191551400014,101559630583359001") ;
        lists.add("191551400029,101559630594875001") ;
        lists.add("191551400030,101559630887679001") ;
        lists.add("191551400032,101559631117019001") ;
        lists.add("191551400039,101559631652107001") ;
        lists.add("191551400047,101559632154963001") ;
        lists.add("191522100028,101559632584057001") ;
        lists.add("191551500001,101559632597357001") ;
        lists.add("191551500004,101559633120301001") ;
        lists.add("191551500013,101559633594499001") ;
        lists.add("191531500192,101559634427296001") ;
        lists.add("191541200004,101559634665900001") ;
        lists.add("191551400035,101559634669656001") ;
        lists.add("191541200004,101559634670048001") ;
        lists.add("191551400035,101559634670037001") ;
        lists.add("191551400035,101559634684756001") ;
        lists.add("191541400021,101559638152973001") ;
        lists.add("191541400021,101559638177321001") ;
        lists.add("191551500046,101559638889797001") ;
        lists.add("191551300013,101559639491907001") ;
        lists.add("191551300013,101559639510093001") ;
        lists.add("191551600033,101559639548361001") ;
        lists.add("191551700001,101559640343510001") ;
        lists.add("191551600037,101559640414963001") ;
        lists.add("191551600037,101559640432168001") ;
        lists.add("191551600009,101559641572486001") ;
        lists.add("191551600009,101559641596462001") ;
        lists.add("191551600046,101559641638566001") ;
        lists.add("191551200047,101559642316747001") ;
        lists.add("191551200047,101559642338388001") ;
        lists.add("191532300109,101559642701276001") ;
        lists.add("191551700042,101559642924615001") ;
        lists.add("191551700042,101559642943299001") ;
        lists.add("191551700030,101559643606191001") ;
        lists.add("191551700030,101559643628142001") ;
        lists.add("191551700041,101559644163006001") ;
        lists.add("191551300170,101559644383974001") ;
        lists.add("191551300170,101559644402374001") ;
        lists.add("191551800023,101559648458147001") ;
        lists.add("191551800023,101559648475651001") ;
        lists.add("191551900018,101559650323234001") ;
        lists.add("191552000005,101559651649598001") ;
        lists.add("191552000005,101559651667385001") ;
        lists.add("191551700003,101559653404101001") ;
        lists.add("191551900017,101559653526899001") ;
        lists.add("191551900017,101559653548093001") ;
        lists.add("191552100003,101559654714042001") ;
        lists.add("191521600172,101559655283446001") ;
        lists.add("191521600172,101559655301364001") ;
        lists.add("191552100017,101559656867066001") ;
        lists.add("191552100017,101559656890183001") ;
        lists.add("191552100026,101559657119277001") ;
        lists.add("191552200003,101559659270493001") ;
        lists.add("191551700018,101559659736657001") ;

        for (String str:lists) {
            BorrowMqConfirmDto borrowMqConfirmDto = new BorrowMqConfirmDto();
            borrowMqConfirmDto.setUserId(str.split(",")[0]);
            borrowMqConfirmDto.setOrderNo(str.split(",")[1]);
            borrowMqConfirmDto.setProductCode("xxd20181010003");
            logger.info("mq重新跑repost" + JSON.toJSONString(borrowMqConfirmDto));
            ResponseData responseData = orderInfoService.mqRepost(borrowMqConfirmDto);
        }
        return ResponseData.success();
    }

    /**
     * 银行卡列表(代扣版)
     *
     * @param borrowConfirmDto
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/banks/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowBanks(@RequestBody BorrowConfirmDto borrowConfirmDto) throws Exception{
            return orderInfoService.borrowBanks(borrowConfirmDto);
    }

    @RequestMapping(value = "/lend/banks/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowBanksNew(@RequestBody BorrowConfirmDto borrowConfirmDto) throws Exception{
        return orderInfoService.borrowBanks(borrowConfirmDto);
    }

    /**
     * 借款结果
     *
     * @param baseInfo
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/result/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowResult(@RequestBody BaseInfo baseInfo) throws Throwable {
        ResponseData responseData = orderInfoService.getBorrowResult(baseInfo.getUserId());
        logger.info("流程result" + JSON.toJSONString(responseData));
        return responseData;
    }

    /**
     * 借款详情
     *
     * @param baseInfo
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/detail/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowDetail(@RequestBody BaseInfo baseInfo) throws Throwable {
        ResponseData responseData = orderInfoService.getBorrowDetail(baseInfo.getOrderNo());
        return responseData;
    }

    @RequestMapping(value = "/lend/detail/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowDetailNew(@RequestBody BaseInfo baseInfo) throws Throwable {
        ResponseData responseData = orderInfoService.getBorrowDetail(baseInfo.getOrderNo());
        return responseData;
    }

    /**
     * 根据userId订单详情
     *
     * @param baseInfo
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/detail/withfundcode", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowDetailByUserId(@RequestBody BaseInfo baseInfo) throws Throwable {
    	logger.info("查询订单请求参数：{}", JSON.toJSONString(baseInfo));
    	ResponseData responseData = orderInfoService.getBorrowDetailByUserId(baseInfo.getUserId());
    	return responseData;
    }
    /**
     * 根据订单号更新是否已通知用户状态
     *
     * @param info
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/detail/updateNotice", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateOrderNoticeStatus(@RequestBody OrderUpdatInfo info) throws Throwable {
    	logger.info("更新订单请求参数：{}", JSON.toJSONString(info));
    	ResponseData responseData = orderInfoService.updateOrderNoticeStatus(info);
    	return responseData;
    }



    /**
     * 所有借款记录
     *
     * @param baseInfo
     * @return ResponseData
     */
    @RequestMapping(value = "/borrow/record/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowAll(@RequestBody BaseInfo baseInfo) throws Throwable {
        ResponseData responseData = orderInfoService.getBorrowAll(baseInfo.getUserId());
        //ResponseData responseData = orderInfoService.getBorrowAllWithAppName(baseInfo.getUserId(),baseInfo.getAppName());
        return responseData;
    }

    @RequestMapping(value = "/lend/record/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowAllNew(@RequestBody BaseInfo baseInfo) throws Throwable {
        ResponseData responseData = orderInfoService.getBorrowAll(baseInfo.getUserId());
        //ResponseData responseData = orderInfoService.getBorrowAllWithAppName(baseInfo.getUserId(),baseInfo.getAppName());
        return responseData;
    }

    /**
     * 获取新报告内容
     * @param base
     * @return
     * @throws Throwable
     */
    @RequestMapping(value="/dc/getNewReport/auth")
    public ResponseData getNewReport(@RequestBody BaseInfo base)  throws Throwable{
    	logger.info("获取新报告内容前端传入参数:{}",JSON.toJSONString(base));
    	ResponseData data = orderInfoService.getNewReport(base);
    	return data;
    }

    @RequestMapping(value="/report/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getNewReportNew(@RequestBody BaseInfo base)  throws Throwable{
        logger.info("获取新报告内容前端传入参数:{}",JSON.toJSONString(base));
        ResponseData data = orderInfoService.getNewReport(base);
        return data;
    }

    /**
	 * 
	 * queryResult:(代扣借款结果查询). <br/>
	 * @author wangzhch
	 * @param baseInfo
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping(value="/dc/queryResult/auth")
	public ResponseData queryResult(@RequestBody BaseInfo baseInfo)  throws Throwable{
		logger.info("认证借款结果查询:{}",JSON.toJSONString(baseInfo));
		ResponseData data = orderInfoService.queryResult(baseInfo);
		return data;
	}

    @RequestMapping(value="/audit/query")
    public ResponseData queryResultNew(@RequestBody BaseInfo baseInfo)  throws Throwable{
        logger.info("认证结果查询:{}",JSON.toJSONString(baseInfo));
        ResponseData data = orderInfoService.queryResult(baseInfo);
        return data;
    }

	/**
	 * 手动划扣重发
	 */
	@RequestMapping(value="/withhold/resend")
	public ResponseData withholdResend()  throws Throwable{
		logger.info("代扣手动跑批开始");
		new Thread(() -> {
			ResponseData data = withholdOrderContract.resendWithholdOrder();
        }).start();
		logger.info("代扣手动跑批结束");
		return ResponseData.success();
	}

    /**
     * 代扣成功回调
     *
     * @param message
     * @return ResponseData
     */
    @RequestMapping(value = "/withhold/callback", method = RequestMethod.POST, produces = "application/json")
    public ResponseData withholdCallBack(@RequestBody OrderMessage message)  {
        ResponseData responseData = orderInfoService.withholdCallBack(message);
        return responseData;
    }

    /**
     * 代扣回调发送短信
     * @return
     */
    @RequestMapping(value = "/withhold/callback/withhold", method = RequestMethod.POST, produces = "application/json")
    public ResponseData withholdCallBackMsg(@RequestBody WithholdCallBackInfo withholdCallBackInfo) {
        ResponseData responseData = orderInfoService.withholdCallBackMsg(withholdCallBackInfo);
        return responseData;
    }

    /**
     * 根据订单号查询代扣信息；
     * @return
     */
    @RequestMapping(value = "/queryPayOrderByBusinessOrderNo", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryPayOrderByBusinessOrderNo(@RequestBody QueryWithholdOrderByBusinessOrderNoReqDTO queryWithholdOrderByBusinessOrderNoReqDTO) {
        logger.info("根据订单号查询代扣信息:{}",JSON.toJSONString(queryWithholdOrderByBusinessOrderNoReqDTO));
        ResponseData responseData = orderInfoService.queryPayOrderByBusinessOrderNo(queryWithholdOrderByBusinessOrderNoReqDTO.getBusinessOrderNo());
        logger.info("根据订单号查询代扣信息:{}",JSON.toJSONString(responseData));
        return responseData;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/comfimWithrowMoney", method = RequestMethod.POST, produces = "application/json")
    public ResponseData comfimWithrowMoney(){
        ResponseData responseData = new ResponseData();
        logger.info("提现确认请求参数:{}");
        return responseData;
    }

   @RequestMapping(value = "/getBorrowInfoByOrderNo", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowInfo(@RequestBody BaseInfo baseInfo) throws Throwable {
        ResponseData responseData = orderInfoService.getBorrowInfoByOrderNo(baseInfo.getOrderNo());
        return responseData;
    }

    @RequestMapping(value = "/getBorrowInfoByO", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowInfoByO(@RequestBody BaseInfo baseInfo) throws Throwable {
        logger.info("查询订单请求参数：{}", JSON.toJSONString(baseInfo));
        ResponseData responseData = orderInfoService.getBorrowInfoByO(baseInfo.getOrderNo());
        return responseData;
    }
    @RequestMapping(value = "/getBorrowInfoAll", method = RequestMethod.POST, produces = "application/json")
    public ResponseData borrowInfoAll(@RequestBody BaseInfo baseInfo) throws Throwable {
		logger.info("查询我的订单请求参数：{}", JSON.toJSONString(baseInfo));
        ResponseData responseData = orderInfoService.getBorrowInfoAll(baseInfo.getUserId());
        return responseData;
    }
    @RequestMapping(value = "/query/checkOrderSuccess", method = RequestMethod.POST, produces = "application/json")
    public ResponseData checkOrderSuccess(@RequestBody User user) {
    	logger.info("查询我的订单请求参数：{}", JSON.toJSONString(user));
    	ResponseData responseData = orderInfoService.checkOrderSuccess(user);
    	logger.info("查询我的订单请求参数：{}", JSON.toJSONString(responseData));
		return responseData;
    	
    }
}













