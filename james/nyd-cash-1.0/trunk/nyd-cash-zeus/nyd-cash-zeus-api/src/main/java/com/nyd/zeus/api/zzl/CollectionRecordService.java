package com.nyd.zeus.api.zzl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.model.collection.CollectionRecord;
import com.nyd.zeus.model.collection.CollectionRecordRequest;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;

public interface CollectionRecordService {
	
	//列表查询
	public PagedResponse<List<CollectionRecord>> queryList(CollectionRecordRequest request);
	
	CommonResponse<JSONObject> save(CollectionRecord request);
	
	//根据手机号查询催收记录信息
	CollectionRecord getCollectionInfo(String userId, String phone);

}
