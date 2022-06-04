package com.mahama.quartz.model;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class QuartzJob implements Serializable {
	/**
	 * 任务名
	 */
	private String jobName;
	/**
	 * 任务组
	 */
	private String jobGroup;
	private String jobClass;
	/**
	 * 描述
	 */
	private String description;
	private String cronExpression;
	/**
	 * 是否支持并发执行
	 */
	private boolean concurrent;
	private String jobStatus;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date nextTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date previousTime;
	private boolean disabled;
	private JSONObject dataJson;
}
