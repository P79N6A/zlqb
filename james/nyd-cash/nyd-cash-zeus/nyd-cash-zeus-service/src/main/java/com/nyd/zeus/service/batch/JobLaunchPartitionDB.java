/**
 * 
 */
package com.nyd.zeus.service.batch;

import org.springframework.batch.core.JobParametersBuilder;

import java.util.Date;

/**
 *
 */
public class JobLaunchPartitionDB {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JobLaunchBase.executeJob("com/nyd/zeus/configs/service/job/job-partition-db.xml", "partitionJob",
				new JobParametersBuilder().addDate("date", new Date()));
	}
}
