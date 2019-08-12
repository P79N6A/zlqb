package com.zhiwang.zfm.common.util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.zhiwang.zfm.common.util.ChkUtil;


/**
 *   队列池
 * @author Alan
 *
 */
public class QueueCaches {

	
	private static Map<String, ConcurrentLinkedDeque<Object>> queues = null ;
	
	/**
	 *  获得对象
	 * @return
	 */
	public static Map<String, ConcurrentLinkedDeque<Object>> getInstance(){
		if(ChkUtil.isEmpty(queues)) {
			
			queues = new HashMap<String, ConcurrentLinkedDeque<Object>>();
		}
		
		return queues;
	}
	
	/**
	 * 获得队列
	 * @param key
	 * @return
	 */
	public static ConcurrentLinkedDeque<Object> getQueue(String key){
		return queues.get(key);
	}
	
	/**
	 *  加入新队列
	 * @param key
	 */
	public static ConcurrentLinkedDeque<Object> addQueue(String key){
		ConcurrentLinkedDeque<Object> deque = new ConcurrentLinkedDeque<Object>();
		queues.put(key, deque);
		return deque;
	}
	
	
	/**
	 * 移除队列
	 * @param key
	 */
	public static void removeQueue(String key){
		queues.remove(key);
	}
	
	
}
