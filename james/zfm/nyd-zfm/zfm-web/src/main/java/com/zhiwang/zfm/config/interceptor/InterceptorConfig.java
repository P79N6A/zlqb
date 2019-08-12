package com.zhiwang.zfm.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter{

	@Override  
    public void addInterceptors(InterceptorRegistry registry) {
        //这里可以添加多个拦截器  
        registry.addInterceptor(new AppInterceptor()).addPathPatterns("/api/app/**");  
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/web/pangolin/followInfo/**");  
        super.addInterceptors(registry);  
    }
	
}
