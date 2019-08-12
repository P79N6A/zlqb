package com.zhiwang.zfm.config.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

/**
 * Shiro配置
 *
 * @author chenbo
 * @email 381756915@qq.com
 * @date 2018-01-5 19:22
 */
@Configuration
public class ShiroConfig {

	/**
	 * session管理
	 * @return
	 */
    @Bean(name = "sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        // TODO 暂时设置为12小时
        sessionManager.setGlobalSessionTimeout(12*60 * 60 * 1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    // 安全管理
    @Bean(name = "securityManager")
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager) {
    	DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        //shiroFilter.setLoginUrl("/login");
        //shiroFilter.setUnauthorizedUrl("/api/sys/user/login");
        
        //Map<String, String> filterMap = new LinkedHashMap<String, String>();
        
        // 公用文件
        /*filterMap.put("/public/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/api/**", "anon");

        // swagger配置
        filterMap.put("/swagger**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-resources/configuration/ui", "anon");
        
        filterMap.put("/login", "anon");
//        filterMap.put("/mylogout", "anon");
//        filterMap.put("/sys/logout", "anon");
        filterMap.put("/common/**", "anon");
        filterMap.put("/api/upload/*", "anon");
        
        // 验证码
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/**", "authc");*/

        //shiroFilter.setFilterChainDefinitionMap(filterMap);
        
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("shiroLoginFilter", shiroLoginFilter());
        shiroFilter.setFilters(filters);
        
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        /*filterMap.put("/swagger**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-resources/configuration/ui", "anon");
        filterMap.put("/web/**", "anon");
        filterMap.put("/WEB-INF/views/*", "anon");
        filterMap.put("/common/**", "anon");
        filterMap.put("/api/upload/*", "anon");
        
        filterMap.put("/api/app/**", "anon");
        filterMap.put("/api/sys/user/login", "anon");
        
        filterMap.put("/**", "authc");*/
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


    @Bean(name = "shiroLoginFilter")
    public ShiroLoginFilter shiroLoginFilter(){
        ShiroLoginFilter shiroLoginFilter = new ShiroLoginFilter();
        return shiroLoginFilter;
    }
}
