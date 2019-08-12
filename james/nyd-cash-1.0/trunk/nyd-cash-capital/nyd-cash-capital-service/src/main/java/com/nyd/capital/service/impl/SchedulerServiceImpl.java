package com.nyd.capital.service.impl;

import com.nyd.capital.entity.Fund;
import com.nyd.capital.service.FundSourceService;
import com.nyd.capital.service.SchedulerService;
import com.nyd.capital.service.quartz.QuartzJob;
import com.nyd.capital.service.quartz.QuartzUtils;
import com.nyd.capital.service.quartz.WsmRabbitConsumerTask;
import com.nyd.capital.service.quartz.ScheduleJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/8
 **/
@Service
public class SchedulerServiceImpl implements SchedulerService{
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @Autowired
    private FundSourceService fundSourceService;

    @Autowired
    private WsmRabbitConsumerTask rabbitConsumerTask;




    @Override
    public void scheduleRabbit() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<Fund> result = fundSourceService.queryUseSource();
        List<ScheduleJob> jobs = new ArrayList<>();
        for(Fund fund:result){
            ScheduleJob jobStart = new ScheduleJob();
            jobStart.setJobName("start");
            jobStart.setJobGroup(fund.getFundName()+"_"+fund.getFundCode());
            jobStart.setCronExpression(QuartzUtils.getCronTime(fund.getRemitStartTime()));
            jobs.add(jobStart);

            ScheduleJob jobStop = new ScheduleJob();
            jobStop.setJobName("stop");
            jobStop.setJobGroup(fund.getFundName()+"_"+fund.getFundCode());
            jobStop.setCronExpression(QuartzUtils.getCronTime(fund.getRemitEndTime()));
            jobs.add(jobStop);
        }
        for (ScheduleJob job:jobs) {

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
    }

    @Override
    public void scheduleKzjr() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        ScheduleJob job = new ScheduleJob();
        job.setJobName("kzjr");
        job.setJobGroup("kzjr");
        job.setCronExpression("0 0-59/30 0-23 * * ?");
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
}
