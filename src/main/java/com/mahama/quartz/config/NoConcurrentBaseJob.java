package com.mahama.quartz.config;

import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class NoConcurrentBaseJob extends BaseJob {
}
