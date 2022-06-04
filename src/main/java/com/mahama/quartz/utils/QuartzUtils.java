package com.mahama.quartz.utils;

import com.mahama.quartz.exec.JobExecute;
import com.mahama.quartz.model.QuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Date;

@Slf4j
public class QuartzUtils {
    /**
     * 通过反射调用job中定义的方法
     */
    public static void executeJob(QuartzJob job) throws Exception {
        JobExecute jobExecute = null;
        Class<?> clazz = null;
        String jobClass = job.getJobClass();
        if (jobClass != null && !jobClass.equals("") && !jobClass.equals("null")) {
            clazz = Class.forName(job.getJobClass());
            jobExecute = (JobExecute) SpringContextHolder.getBean(clazz);
        }
        if (jobExecute == null) {
            throw new RuntimeException("任务名称 = [" + job.getJobName() + "]未启动成功，请检查执行类是否配置正确！！！");
        }
        jobExecute.setJob(job);
        jobExecute.execute();
    }

    /**
     * 判断cron时间表达式正确性
     */
    public static boolean isValidExpression(final String cronExpression) {
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
