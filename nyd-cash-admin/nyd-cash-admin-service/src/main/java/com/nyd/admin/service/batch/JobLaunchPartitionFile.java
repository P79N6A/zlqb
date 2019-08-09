/**
 * 
 */
package com.nyd.admin.service.batch;

import org.springframework.batch.core.JobParametersBuilder;

import java.util.Date;

/**
 *
 */
public class JobLaunchPartitionFile {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JobLaunchBase.executeJob("com/nyd/zeus/configs/service/job/job-partition-file.xml", "partitionJob",
				new JobParametersBuilder().addDate("date", new Date()));
	}
}
