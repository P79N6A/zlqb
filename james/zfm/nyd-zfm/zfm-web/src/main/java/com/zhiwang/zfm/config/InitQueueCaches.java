package com.zhiwang.zfm.config;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import com.zhiwang.zfm.common.util.cache.QueueCaches;

@Repository
public class InitQueueCaches implements ApplicationListener<ApplicationEvent> {

	public void onApplicationEvent(ApplicationEvent arg0) {
		// 初始化队列池
		 QueueCaches.getInstance();
		
		// 设置邀请好友发放奖励金队列
		QueueCaches.addQueue("inviteReward");
		
		// 设置邀请好友发放奖励金提现队列
		QueueCaches.addQueue("rewardWithdraw");
	}
}
