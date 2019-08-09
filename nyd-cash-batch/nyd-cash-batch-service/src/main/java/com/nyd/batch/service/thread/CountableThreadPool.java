package com.nyd.batch.service.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cong Yuxiang
 * 2018/1/15
 **/
public class CountableThreadPool {
    private int threadNum;

    private AtomicInteger threadAlive = new AtomicInteger();

//    private ReentrantLock reentrantLock = new ReentrantLock();

//    private Condition condition = reentrantLock.newCondition();

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

//    public CountableThreadPool(int threadNum) {
//        this.threadNum = threadNum;
//        this.executorService = Executors.newFixedThreadPool(threadNum);
//    }
//
//    public CountableThreadPool(int threadNum, ExecutorService executorService) {
//        this.threadNum = threadNum;
//        this.executorService = executorService;
//    }

//    public void setExecutorService(ExecutorService executorService) {
//        this.executorService = executorService;
//    }


    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public int getThreadAlive() {
        return threadAlive.get();
    }

    public int getThreadNum() {
        return threadNum;
    }

//    private ExecutorService executorService;

    public void execute(final Runnable runnable) {


//        if (threadAlive.get() >= threadNum) {
//            try {
//                reentrantLock.lock();
//                while (threadAlive.get() >= threadNum) {
//                    try {
//                        condition.await();
//                    } catch (InterruptedException e) {
//                    }
//                }
//            } finally {
//                reentrantLock.unlock();
//            }
//        }
        threadAlive.incrementAndGet();
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {
                    try {
//                        reentrantLock.lock();
                        threadAlive.decrementAndGet();
//                        condition.signal();
                    } finally {
//                        reentrantLock.unlock();
                    }
                }
            }
        });
    }



    public void shutdown() {
        threadPoolTaskExecutor.shutdown();
    }
}
