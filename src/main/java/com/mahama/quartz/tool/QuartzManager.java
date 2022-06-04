package com.mahama.quartz.tool;

import com.mahama.quartz.config.NoConcurrentBaseJob;
import com.mahama.quartz.config.BaseJob;
import com.mahama.quartz.model.QuartzJob;
import com.mahama.quartz.utils.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

/**
 * 任务服务
 */
@Service
@Slf4j
public class QuartzManager {
    private final Scheduler scheduler;

    public QuartzManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 获取单个任务
     *
     */
    public QuartzJob getJob(String jobName, String jobGroup) throws SchedulerException {
        QuartzJob job = null;
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (null != trigger) {
            job = new QuartzJob();
            job.setJobName(jobName);
            job.setJobGroup(jobGroup);
            job.setDescription("触发器:" + trigger.getKey());
            job.setNextTime(trigger.getNextFireTime());
            job.setPreviousTime(trigger.getPreviousFireTime());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
        }
        return job;
    }

    /**
     * 添加任务
     *
     */

    public void addJob(QuartzJob job) throws SchedulerException {
        if (job.isDisabled()) {
            return;
        }
        if (!QuartzUtils.isValidExpression(job.getCronExpression())) {
            log.error("时间表达式错误（" + job.getJobName() + "," + job.getJobGroup() + "）," + job.getCronExpression());
        } else {
            // 任务名称和任务组设置规则：    // 名称：task_1 ..    // 组 ：group_1 ..
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 不存在，创建一个
            if (null == trigger) {
                //是否允许并发执行
                Class<? extends Job> clazz = job.isConcurrent() ? BaseJob.class : NoConcurrentBaseJob.class;
                JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
                jobDetail.getJobDataMap().put("job", job);
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                // 按新的表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {     // trigger已存在，则更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
    }

    /**
     * 删除任务
     */

    public boolean deleteJob(QuartzJob record) {
        JobKey jobKey = JobKey.jobKey(record.getJobName(), record.getJobGroup());
        try {
            scheduler.deleteJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
