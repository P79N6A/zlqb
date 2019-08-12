package com.nyd.zeus.dao.mapper;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.nyd.zeus.model.attendance.AttendanceRequest;
import com.nyd.zeus.model.attendance.TimeAttendance;

@Repository
public interface TimeAttendanceMapper {
	
	void save(TimeAttendance request);
	
	void update(TimeAttendance request);
	
	List<TimeAttendance> queryList(AttendanceRequest request);
	
	Long queryListCount(AttendanceRequest request);

}
