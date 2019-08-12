package com.nyd.capital.service.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/

public class QuartzJob implements Job{

    Logger logger = LoggerFactory.getLogger(QuartzJob.class);
    @Autowired
    private KzjrSubbitAssetTask kzjrSubbitAssetTask;

//    private ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("任务成功运行");

        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        logger.info("任务名称 = [" + scheduleJob.getJobName() + "]");
//        System.out.println(InitComponent.applicationContext);
//        KzjrSubbitAssetTask task = (KzjrSubbitAssetTask) InitComponent.applicationContext.getBean("kzjrSubbitAssetTask");

//        System.out.println(task);

        if("kzjr".equals(scheduleJob.getJobName())){
            kzjrSubbitAssetTask.run();
        }
        //        if("start".equals(scheduleJob.getJobName())){
//           task.start();
//        }else if("stop".equals(scheduleJob.getJobName())){
//            task.stop();
//        }
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            this.applicationContext = applicationContext;
//    }
}
