package com.mahama.quartz.config;

import com.mahama.quartz.model.QuartzJob;
import com.mahama.quartz.utils.QuartzUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class BaseJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		QuartzJob job = (QuartzJob) data.get("job");
		try {
			QuartzUtils.executeJob(job);
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
