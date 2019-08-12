package com.zhiwang.zfm.common.util.cache;

import java.util.concurrent.ConcurrentLinkedDeque;

/**   
 * @ClassName:  QueueCache  
 * @Description:  队列
 * @Author: 
 * @CreateDate: 2018年12月10日 下午2:13:04 
 * @Version: v1.0  
 */
public class QueueCache {

	private static ConcurrentLinkedDeque<Object> queue = new ConcurrentLinkedDeque<Object>() ;
	
	public QueueCache() {
		
	}

	public static boolean pubObj(Object obj){
		boolean flag = queue.offer(obj);
		return flag;
	}
	
	public static ConcurrentLinkedDeque<Object> get(){
		return queue;
	}
	
	public static Object getHead(){
		return queue.peek();
	}
	
	public static Object getAndRemove(){
		return queue.poll();
	}
	
	public static Object getSize(){
		return queue.size();
	}
	
	/**
	 * @Tiele：pubObj
	 * @Describe：带参数调用添加队列
	 * @param queueParams
	 * @param obj
	 * @return
	 */
	public static boolean pubObj(Object obj, ConcurrentLinkedDeque<Object> paramQueue){
		boolean flag = paramQueue.offer(obj);
		return flag;
	}
	
	/**
	 * @Tiele：pubObj
	 * @Describe：带参数调用获取队列第一个值
	 *  获取但不移除此队列的头；如果此队列为空，则返回 null
	 * @param queueParams
	 * @param obj
	 * @return
	 */
	public static Object getHead(ConcurrentLinkedDeque<Object> queueParams){
		return queueParams.peek();
	}
	
	/**
	 * @Tiele：pubObj
	 * @Describe：带参数调用获取队列第一个值并移除、
	 * 获取并移除此队列的头，如果此队列为空，则返回 null。
	 * @param queueParams
	 * @param obj
	 * @return
	 */
	public static Object getAndRemove(ConcurrentLinkedDeque<Object> queueParams){
		return queueParams.poll();
	}
	
	/**
	 * @Tiele： get
	 * @Describe：带参数获取
	 * @param paramQueue
	 * @return
	 */
	public static ConcurrentLinkedDeque<Object> get(ConcurrentLinkedDeque<Object> paramQueue){
		return paramQueue;
	}
	
}
