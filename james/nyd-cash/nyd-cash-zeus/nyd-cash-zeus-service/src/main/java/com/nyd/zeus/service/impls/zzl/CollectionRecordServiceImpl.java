package com.nyd.zeus.service.impls.zzl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.zeus.api.zzl.CollectionRecordService;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.dao.mapper.CollectionRecordMapper;
import com.nyd.zeus.model.collection.CollectionRecord;
import com.nyd.zeus.model.collection.CollectionRecordRequest;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.helibao.util.Uuid;

@Service("collectionRecordService")
public class CollectionRecordServiceImpl implements CollectionRecordService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionRecordServiceImpl.class);
	
	@Autowired
    private CollectionRecordMapper collectionRecordMapper;
	
	@Autowired
	private ZeusSqlService<?> zeusSqlService;

	@Override
	public PagedResponse<List<CollectionRecord>> queryList(CollectionRecordRequest request) {
		LOGGER.info("催收记录--列表查询开始入参：{}",JSON.toJSONString(request));
		PagedResponse<List<CollectionRecord>> response = new PagedResponse<List<CollectionRecord>>();
		try {
			if(ChkUtil.isEmpty(request.getOrderNo())){
	    		response.setData(null);
				response.setCode("0");
				response.setMsg("必填参数缺失！");
				response.setSuccess(false);
	    	}
			/*LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("create_time", "desc");
			request.setOrderby(orderby);*/
			List<CollectionRecord> list = collectionRecordMapper.queryList(request);
			Long listCount = collectionRecordMapper.queryListCount(request);
			response.setData(list);
			response.setTotal(listCount);
			response.setPageNo(request.getPageNo());
			response.setPageSize(request.getPageSize());
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("催收记录--列表查询系统错误：{}",e.getMessage());
		}
		return response;
	}

	@Override
	public CommonResponse<JSONObject> save(CollectionRecord request) {
		LOGGER.info("添加催记开始入参request：{}",JSON.toJSONString(request));
		CommonResponse<JSONObject> response = new CommonResponse<>();
		try {
			String sql = "select urge_status as urgeStatus from t_bill where order_no = '"+request.getOrderNo()+"'";
			JSONObject obj = zeusSqlService.queryOne(sql);
			if(ChkUtil.isEmpty(obj) || !obj.containsKey("urgeStatus")){
				response.setData(null);
				response.setCode("0");
				response.setMsg("订单不存在！");
				response.setSuccess(false);
				return response;
			}
			//获取订单催收状态
			Integer urgeStatus = Integer.parseInt(obj.getString("urgeStatus"));
			//是否承诺还款：0-是，1-否
			if(request.getIsPromiseRepay()==0 && urgeStatus < 3){//是
				urgeStatus = 3;
				zeusSqlService.updateSql("update t_bill set urge_status = '"+urgeStatus+"' where order_no = '"+request.getOrderNo()+"'");
			}else if(request.getIsPromiseRepay()==1 && urgeStatus < 2){//否
				urgeStatus = 2;
				zeusSqlService.updateSql("update t_bill set urge_status = '"+urgeStatus+"' where order_no = '"+request.getOrderNo()+"'");
			}
			request.setId(UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24());
			collectionRecordMapper.save(request);
			response.setData(null);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("添加催记系统错误：{}",e,e.getMessage());
		}
		return response;
	}

	@Override
	public CollectionRecord getCollectionInfo(String userId, String phone) {
		CollectionRecord info = new CollectionRecord();
		try {
			info = collectionRecordMapper.getCollectionInfo(userId, phone);
		} catch (Exception e) {
			LOGGER.error("根据手机号查询催收记录信息系统错误：{}",e,e.getMessage());
		}
		return info;
	}

}
