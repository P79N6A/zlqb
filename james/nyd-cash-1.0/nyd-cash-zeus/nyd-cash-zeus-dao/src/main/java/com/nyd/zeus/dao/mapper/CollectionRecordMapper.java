package com.nyd.zeus.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nyd.zeus.model.collection.CollectionRecord;
import com.nyd.zeus.model.collection.CollectionRecordRequest;

@Mapper
public interface CollectionRecordMapper {
	
	void save(CollectionRecord request);
	
	List<CollectionRecord> queryList(CollectionRecordRequest request);
	
	Long queryListCount(CollectionRecordRequest request);
	
	CollectionRecord getCollectionInfo(@Param("userId") String userId, @Param("phone") String phone);

}
