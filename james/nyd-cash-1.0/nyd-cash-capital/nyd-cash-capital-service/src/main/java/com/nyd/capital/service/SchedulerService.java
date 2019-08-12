package com.nyd.capital.service;

import org.quartz.SchedulerException;

/**
 * Cong Yuxiang
 * 2017/12/8
 **/
public interface SchedulerService {
    void scheduleRabbit() throws SchedulerException;
    void scheduleKzjr() throws SchedulerException;
}
