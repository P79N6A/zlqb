package com.nyd.zeus.service.lock;

import org.springframework.data.redis.core.RedisTemplate;

public class RowLock {

	
	 private RedisTemplate redisTemplate;
	 
	 public RowLock(RedisTemplate redisTemplate){
		 redisTemplate = redisTemplate;
	 }
	
	public synchronized void lock(String id) throws InterruptedException {
		
		while (redisTemplate.hasKey(id)) {
			wait();
		}
		redisTemplate.opsForValue().set(id, id);
	}

	public synchronized void unLock(String id) {
		if (redisTemplate.hasKey(id)) {
			redisTemplate.delete(id);
			notifyAll();
		}
	}
}
