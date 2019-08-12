package com.zhiwang.zfm.controller.webapp.order;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.user.api.zzl.UserForGYTServise;
import com.nyd.user.model.vo.EmergencyContactVo;
import com.nyd.zeus.api.zzl.CollectionRecordService;
import com.nyd.zeus.model.collection.CollectionRecord;
import com.nyd.zeus.model.collection.CollectionRecordRequest;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.config.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/collectionRecord")
@Api(description="催收记录")
public class CollectionRecordController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionRecordController.class);
	
	@Autowired
	private CollectionRecordService collectionRecordService;
	
	@Autowired
	private UserForGYTServise userForGYTServise;
	
	@ApiOperation(value = "催收记录列表查询")
	@RequestMapping(value = "/collectionList", method = RequestMethod.POST)
    public PagedResponse<List<CollectionRecord>> collectionList(@RequestBody CollectionRecordRequest request) {
    	LOGGER.info("催收记录列表查询请求开始入参：{}", JSON.toJSONString(request));
    	PagedResponse<List<CollectionRecord>> response = collectionRecordService.queryList(request);
        return response;
    }
	
	@ApiOperation(value = "添加催记")
	@RequestMapping(value = "/saveCollection", method = RequestMethod.POST)
    public CommonResponse<JSONObject> saveCollection(@RequestBody CollectionRecord request){
    	LOGGER.info("添加催收记录开始入参：{}",JSON.toJSONString(request));
    	CommonResponse<JSONObject> response = new CommonResponse<>();
    	try {
    		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
    		request.setSysUserId(userVO.getId());
    		request.setSysUserName(userVO.getLoginName());
    		response = collectionRecordService.save(request);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("添加催记系统错误：{}",e,e.getMessage());
		}
        return response;
    }
	
	@ApiOperation(value = "紧急联系人")
	@RequestMapping(value = "/emergencyContact", method = RequestMethod.POST)
    public com.nyd.user.model.common.CommonResponse<List<EmergencyContactVo>> emergencyContact(@RequestBody CollectionRecord request){
    	LOGGER.info("紧急联系人查询开始入参：{}",JSON.toJSONString(request));
    	com.nyd.user.model.common.CommonResponse<List<EmergencyContactVo>> response = new com.nyd.user.model.common.CommonResponse<>();
    	try {
    		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
    		request.setSysUserId(userVO.getId());
    		request.setSysUserName(userVO.getLoginName());
    		response = userForGYTServise.emergencyContact(request.getUserId());
    		List<EmergencyContactVo> list = response.getData();
    		if(!ChkUtil.isEmpty(list)){
    			for(EmergencyContactVo vo : list){
    				CollectionRecord info = collectionRecordService.getCollectionInfo(vo.getUserId(), vo.getPhone());
    				if(!ChkUtil.isEmpty(info)){
    					vo.setUpdateTime(info.getUpdateTime());
        				vo.setRemark(info.getRemark());
    				}
    			}
    		}
    		response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("紧急联系人查询系统错误：{}",e,e.getMessage());
		}
        return response;
    }

}
