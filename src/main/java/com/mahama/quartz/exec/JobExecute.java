package com.mahama.quartz.exec;

import com.alibaba.fastjson.JSONObject;
import com.mahama.quartz.model.QuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public abstract class JobExecute {
    private QuartzJob job;

    public void setJob(QuartzJob job) {
        this.job = job;
    }

    public void execute() {
        JSONObject json = job.getDataJson();
        try {
            execute(json);
            onSuccess(job);
        } catch (Exception e) {
            onError(job);
        }
    }

    /**
     * @param json 数据库配置的参数
     */
    public abstract void execute(JSONObject json);

    public abstract void onSuccess(QuartzJob job);

    public abstract void onError(QuartzJob job);
}
