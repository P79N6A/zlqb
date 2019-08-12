package com.nyd.capital.ws.controller;

import com.nyd.capital.service.SchedulerService;
import com.nyd.capital.service.quartz.QuartzJob;
import com.nyd.capital.service.quartz.ScheduleJob;
import com.tasfe.framework.support.model.ResponseData;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cong Yuxiang
 * 2017/12/8
 **/
@RestController
@RequestMapping("/nyd/capital")
public class TimeController {
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @RequestMapping("/test")
    public void test() throws SchedulerException {
        ScheduleJob job = new ScheduleJob();
        job.setJobId("10001");
        job.setJobName("data_import");
        job.setJobGroup("dataWork");
        job.setJobStatus("1");
        job.setCronExpression("00 05 14 * * ?");
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        //不存在，创建一个
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                    .withIdentity(job.getJobName(), job.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                    .getCronExpression());

            //按新的cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                    .getCronExpression());

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();

            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

//    @RequestMapping("/start")
//    public String startRabbit(){
//        listenerContainer.start();
//        return "start***********";
//    }
//    @RequestMapping("/stop")
//    public String stopRabbit(){
//        listenerContainer.stop();
//        return "stop***********";
//    }
//    @RequestMapping("/queryFund")
//    public ResponseData qu(){
//        return ResponseData.success(fundSourceService.queryUseSource());
//    }
    @RequestMapping("/reset")
    public ResponseData resetQuartzWsm(){
        try {
            schedulerService.scheduleRabbit();
            return ResponseData.success();
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseData.error();
        }
//        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//        List<Fund> result = fundSourceService.queryUseSource();
//        List<ScheduleJob> jobs = new ArrayList<>();
//        for(Fund fund:result){
//            ScheduleJob jobStart = new ScheduleJob();
//            jobStart.setJobName("start");
//            jobStart.setJobGroup(fund.getFundName()+"_"+fund.getFundCode());
//            jobStart.setCronExpression(QuartzUtils.getCronTime(fund.getRemitStartTime()));
//            jobs.add(jobStart);
//
//            ScheduleJob jobStop = new ScheduleJob();
//            jobStop.setJobName("stop");
//            jobStop.setJobGroup(fund.getFundName()+"_"+fund.getFundCode());
//            jobStop.setCronExpression(QuartzUtils.getCronTime(fund.getRemitEndTime()));
//            jobs.add(jobStop);
//        }
//        for (ScheduleJob job:jobs) {
//
//            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
//
//            //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
//            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//
//            //不存在，创建一个
//            if (null == trigger) {
//                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
//                        .withIdentity(job.getJobName(), job.getJobGroup()).build();
//                jobDetail.getJobDataMap().put("scheduleJob", job);
//
//                //表达式调度构建器
//                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
//                        .getCronExpression());
//
//                //按新的cronExpression表达式构建一个新的trigger
//                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
//
//                scheduler.scheduleJob(jobDetail, trigger);
//            } else {
//                // Trigger已存在，那么更新相应的定时设置
//                //表达式调度构建器
//                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
//                        .getCronExpression());
//
//                //按新的cronExpression表达式重新构建trigger
//                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
//                        .withSchedule(scheduleBuilder).build();
//
//                //按新的trigger重新设置job执行
//                scheduler.rescheduleJob(triggerKey, trigger);
//            }
//        }
//        return "success";
    }
}
