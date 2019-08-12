//package com.zhiwang.zfm.service.impl.redis;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///** 
// * redis集群配置文件 
// * @author chenbo-QQ381756915
// * @date 创建时间：2018年4月20日 下午3:02:51 
// * @version 1.0.0 
// * @parameter  
// * @throws 
// * @return  
// */
//@Component  
//public class RedisParam {  
//      
//    /** redis集群节点 */ 
//	@Value("${redis.nodes:#{null}}")
//    private String nodes; 
//    
//	/** 连接超时时间 */  
//	@Value("${redis.password:#{null}}")
//    private String password; 
//
//	/** 连接超时时间 */  
//	@Value("${redis.connectionTimeout:#{3000}}")
//    private int connectionTimeout;  
//    
//    /** 重连次数 */   
//	@Value("${redis.maxAttempts:#{5}}")
//    private int maxAttempts;  
//    
//	/** 获取数据超时时间*/
//	@Value("${redis.soTimeout:#{3000}}")
//	private int soTimeout;
//	
//	/** 获取*/
//	@Value("${redis.pool.maxIdle:#{200}}")
//	private int maxIdle;
//	
//	/** 获取*/
//	@Value("${redis.pool.maxTotal:#{1024}}")
//	private int maxTotal;
//	
//	/** */
//	@Value("${redis.pool.maxWaitMillis:#{1000}}")
//	private int maxWaitMillis;
//	
//	/** 获取*/
//	@Value("${redis.pool.minIdle:#{0}}")
//	private int minIdle;
//
//	public int getMaxIdle() {
//		return maxIdle;
//	}
//
//	public void setMaxIdle(int maxIdle) {
//		this.maxIdle = maxIdle;
//	}
//
//	public int getMaxTotal() {
//		return maxTotal;
//	}
//
//	public void setMaxTotal(int maxTotal) {
//		this.maxTotal = maxTotal;
//	}
//
//	public int getMaxWaitMillis() {
//		return maxWaitMillis;
//	}
//
//	public void setMaxWaitMillis(int maxWaitMillis) {
//		this.maxWaitMillis = maxWaitMillis;
//	}
//
//	public int getMinIdle() {
//		return minIdle;
//	}
//
//	public void setMinIdle(int minIdle) {
//		this.minIdle = minIdle;
//	}
//    public int getSoTimeout() {
//		return soTimeout;
//	}
//
//	public void setSoTimeout(int soTimeout) {
//		this.soTimeout = soTimeout;
//	}
//
//	public String getNodes() {  
//        return nodes;  
//    }  
//   
//    public void setNodes(String nodes) {  
//        this.nodes = nodes;  
//    }  
//   
//    public int getConnectionTimeout() {
//		return connectionTimeout;
//	}
//
//	public void setConnectionTimeout(int connectionTimeout) {
//		this.connectionTimeout = connectionTimeout;
//	}
//
//	public int getMaxAttempts() {  
//        return maxAttempts;  
//    }  
//   
//    public void setMaxAttempts(int maxAttempts) {  
//        this.maxAttempts = maxAttempts;  
//    }  
//      
//    public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//} 