//package com.zhiwang.zfm.service.impl.redis;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.JedisCluster;
//
///** 
// * 生成JedisCluster对象 
// * @author chenbo-QQ381756915
// * @date 创建时间：2018年4月20日 下午3:06:09 
// * @version 1.0.0 
// * @parameter  
// * @throws 
// * @return  
// */
//@Configuration
//public class JedisClusterConfig {
//	 	
//	@Autowired  
//    private RedisParam redisParam;  
//
//    /**  
//     * 注意：  
//     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用  
//     * @return  
//     */  
//    @Bean  
//    public JedisCluster getJedisCluster() {  
//        String[] serverArray = redisParam.getNodes().split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)  
//        Set<HostAndPort> nodes = new HashSet<>();  
//  
//        for (String ipPort : serverArray) {  
//            String[] ipPortPair = ipPort.split(":");  
//            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));  
//        } 
//        
//        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig(); 
//        poolConfig.setMaxWaitMillis(redisParam.getMaxAttempts());
//        poolConfig.setMinIdle(redisParam.getMinIdle());
//        poolConfig.setMaxTotal(redisParam.getMaxTotal());
//        poolConfig.setMaxIdle(redisParam.getMaxIdle());
//
//        return new JedisCluster(nodes, redisParam.getConnectionTimeout(), redisParam.getSoTimeout(), redisParam.getMaxAttempts(), redisParam.getPassword(), poolConfig);
//    }  
//}
