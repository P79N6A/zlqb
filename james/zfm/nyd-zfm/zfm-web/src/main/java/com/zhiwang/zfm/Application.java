package com.zhiwang.zfm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableConfigurationProperties
@PropertySource({"classpath:dubbo.properties"})
@ImportResource(locations = { "classpath:zfm-dubbo-client.xml" })
@EnableAutoConfiguration(exclude = { JpaRepositoriesAutoConfiguration.class })
@ComponentScan(basePackages = {"com.zhiwang.zfm.*"})
@MapperScan("com.zhiwang.zfm.dao")
@EnableScheduling
public class Application
{
 
	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "true");
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.setBannerMode(Banner.Mode.CONSOLE);
		springApplication.run(args);
	}
	
}
