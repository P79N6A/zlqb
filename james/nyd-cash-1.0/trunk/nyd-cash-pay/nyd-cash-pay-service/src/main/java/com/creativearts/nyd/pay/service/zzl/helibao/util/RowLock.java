package com.creativearts.nyd.pay.service.zzl.helibao.util;

import java.util.concurrent.ConcurrentHashMap;

public class RowLock {
	
	/** 锁前缀 防止KEY重复  */
	/**
	 * 提现申请KEY
	 */
	public final static String LOCK_PREFIX_ORDER_WITHDRAW_APPLAY = "WITHDRAW_APPLAY";
	
	/**
	 * 还款申请KEY
	 */
	public final static String LOCK_PREFIX_ORDER_REPAYMENT_APPLAY = "REPAYMENT_APPLAY";
	
	/**
	 * 借款申请KEY
	 */
	public final static String LOCK_PREFIX_ORDER_APPLAY = "ORDER_APPLAY";
	
	/**
	 * 放款生成明细
	 */
	public final static String LOCK_PREFIX_ORDER_CREATE_PAYCONTROL = "ORDER_CREATE_PAYCONTROL";
	
	/**
	 * 客户基本信息KEY
	 */
	public final static String LOCK_PREFIX_CUST_BASEINFO = "CUST_BASEINFO";
	
	
	/**
	 * 操作流水 KEY
	 */
	public final static String TICKET_KEY = "TICKET_KEY";
	
	public final static String HELIBAO_KEY	= "HELIBAO_KEY";
	
	private static ConcurrentHashMap<String, Object> lockedMap = new ConcurrentHashMap();

	public static ConcurrentHashMap<String, Object> getLockedMap() {
		return lockedMap;
	}

	public static void setLockedMap(ConcurrentHashMap<String, Object> lockedMap) {
		RowLock.lockedMap = lockedMap;
	}

	public synchronized void lock(String id) throws InterruptedException {
		while (lockedMap.containsKey(id)) {
			wait();
		}
		lockedMap.put(id, "");
	}

	public synchronized void unLock(String id) {
		if (lockedMap.containsKey(id)) {
			lockedMap.remove(id);
			notifyAll();
		}
	}

//	public static void main(String[] args) {
//		RowLock lock = new RowLock();
//
//		List<Runnable> arr = new ArrayList<>();
//
//		for (int i = 0; i < 100; i++) {
//			arr.add(new A1(lock, "A" + i));
//		}
//
//		for (int i = 0; i < arr.size(); i++) {
//			new Thread(arr.get(i), "B" + (i+1)).start();
//		}
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
}

//class A1 implements Runnable{
//    private RowLock lock;
//    private String id;
//    
//    public A1(RowLock lock,String id){
//        this.lock = lock;
//        this.id = id;
//    }
// 
//    @Override
//    public void run(){
//        String threadName = Thread.currentThread().getName();
//        try{
//            lock.lock(id);
//            System.out.println(DateUtils.getCurrentTime(DateUtils.STYLE_10) + "线程:"+threadName+" 为["+id+"]加锁");
//            System.out.println(DateUtils.getCurrentTime(DateUtils.STYLE_10) + "线程["+id+"]:"+threadName+" 执行业务代码");
//            Thread.sleep(5000);
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            lock.unLock(id);
//            System.out.println(DateUtils.getCurrentTime(DateUtils.STYLE_10) + "线程:"+threadName+" 释放锁");
//        }
//    }
//}
