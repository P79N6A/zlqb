package com.nyd.admin.service.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/25
 **/
public class JobLaunchDb2File {
    Logger logger = LoggerFactory.getLogger(JobLaunchDb2File.class);
    public static void main(String[] args){
        run("readDB2FileJob");
    }
    public static void run (String jobId) {
        //通过应用程序上下文获得bean
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "com/nyd/admin/configs/service/xml/job/job-db2file.xml");
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        //任务

        Map<String,JobParameter> parameters = new HashMap<>();
        long flag = System.currentTimeMillis();
       parameters.put("timede",new JobParameter("E:/tmp/"+flag));
        parameters.put("billWsmTable",new JobParameter("bill_wsm_20171012"));
        parameters.put("billNydTable",new JobParameter("t_remit"));
        parameters.put("resultDe",new JobParameter("E:/tmp/"+flag+"/result"));

        Job job = (Job) context.getBean(jobId);

        long start = System.currentTimeMillis() ;
        try {
            //执行任务
            JobExecution execution = jobLauncher.run(job, new JobParameters(parameters));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            /** 如果jobLauncher的taskExecutor设置为异步多线程，这里应该注释掉，否则由于job还在执行，而context关闭
             * ，就会导致所有bean（包括数据源dataSource）关闭，从而导致job出错：Data source is closed等错误

             **/
            if(context != null){
                context.close();
            }
        }
        long end = System.currentTimeMillis() ;
    }
}
