package com.zhiwang.zfm.config.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Druid配置
 * @author chenbo
 * @email 381756915@qq.com
 * @date 2018-01-5 19:22
 */
@Configuration
public class DruidConfig {

    //private Logger logger = LoggerFactory.getLogger(DruidConfig.class);

	/**
	 * 连接数据库的url，不同数据库不一样。例如：
	 * mysql : jdbc:mysql://10.20.153.104:3306/druid2  
	 * oracle : jdbc:oracle:thin:@10.20.149.85:1521:ocnauto
	 */
    @Value("${spring.datasource.url:#{null}}")
    private String dbUrl;
   
    /**
     * 连接数据库的用户名
     */
    @Value("${spring.datasource.username: #{null}}")
    private String username;
    
    /**
     * 连接数据库的密码。如果你不希望密码直接写在配置文件中，可以使用ConfigFilter。
     */
    @Value("${spring.datasource.password:#{null}}")
    private String password;
    
    /**
     * 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName
     */
    @Value("${spring.datasource.driverClassName:#{null}}")
    private String driverClassName;
    
    /**
     * 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
     */
    @Value("${spring.datasource.initialSize:#{null}}")
    private Integer initialSize;
    
    /**
     * 最小连接池数量
     */
    @Value("${spring.datasource.minIdle:#{null}}")
    private Integer minIdle;
    
    /**
     * 最大连接池数量
     */
    @Value("${spring.datasource.maxActive:#{null}}")
    private Integer maxActive;
    
    /**
     * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后， 缺省启用公平锁，并发效率会有所下降， 如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
     */
    @Value("${spring.datasource.maxWait:#{null}}")
    private Integer maxWait;
    
    /**
     * 有两个含义： 
	 * 1) Destroy线程会检测连接的间隔时间，单位是毫秒
	 * 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
     */
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis:#{null}}")
    private Integer timeBetweenEvictionRunsMillis;
    
    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    @Value("${spring.datasource.minEvictableIdleTimeMillis:#{null}}")
    private Integer minEvictableIdleTimeMillis;
    
    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句。 如果validationQuery为null，testOnBorrow、testOnReturn、 testWhileIdle都不会起作用。
     */
    @Value("${spring.datasource.validationQuery:#{null}}")
    private String validationQuery;
    
    /**
     * 建议配置默认为true，不影响性能，并且保证安全性。 申请连接的时候检测，如果空闲时间大于 timeBetweenEvictionRunsMillis， 执行validationQuery检测连接是否有效。
     */
    @Value("${spring.datasource.testWhileIdle:#{null}}")
    private Boolean testWhileIdle;
    
    /**
     * 默认true，申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
     */
    @Value("${spring.datasource.testOnBorrow:#{null}}")
    private Boolean testOnBorrow;
    
    /**
     * 默认ture，归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
     */
    @Value("${spring.datasource.testOnReturn:#{null}}")
    private Boolean testOnReturn;
    
    /**
     * 默认false，是否缓存preparedStatement，也就是PSCache。 PSCache对支持游标的数据库性能提升巨大，比如说oracle。 
	 * 在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。
	 * 作者在5.5版本中使用PSCache，通过监控界面发现PSCache有缓存命中率记录， 该应该是支持PSCache。
     */
    @Value("${spring.datasource.poolPreparedStatements:#{null}}")
    private Boolean poolPreparedStatements;
    
    /**
     * 每个连接上PSCache的大小，只有poolPreparedStatements为true时有用
     */
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize:#{null}}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    
    /**
     * 属性类型是字符串，通过别名的方式配置扩展插件， 常用的插件有： 
	 * 监控统计用的filter:stat  
	 * 日志用的filter:log4j 
	 * 防御sql注入的filter:wall
     */
    @Value("${spring.datasource.filters:#{null}}")
    private String filters;
    
    /**
     * 
     */
    @Value("{spring.datasource.connectionProperties:#{null}}")
    private String connectionProperties;

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        //configuration
        if(initialSize != null) {
            datasource.setInitialSize(initialSize);
        }
        if(minIdle != null) {
            datasource.setMinIdle(minIdle);
        }
        if(maxActive != null) {
            datasource.setMaxActive(maxActive);
        }
        if(maxWait != null) {
            datasource.setMaxWait(maxWait);
        }
        if(timeBetweenEvictionRunsMillis != null) {
            datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
        if(minEvictableIdleTimeMillis != null) {
            datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
        if(validationQuery!=null) {
            datasource.setValidationQuery(validationQuery);
        }
        if(testWhileIdle != null) {
            datasource.setTestWhileIdle(testWhileIdle);
        }
        if(testOnBorrow != null) {
            datasource.setTestOnBorrow(testOnBorrow);
        }
        if(testOnReturn != null) {
            datasource.setTestOnReturn(testOnReturn);
        }
        if(poolPreparedStatements != null) {
            datasource.setPoolPreparedStatements(poolPreparedStatements);
        }
        if(maxPoolPreparedStatementPerConnectionSize != null) {
            datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        }

        if(connectionProperties != null) {
            datasource.setConnectionProperties(connectionProperties);
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(statFilter());
        filters.add(wallFilter());
        datasource.setProxyFilters(filters);

        return datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        return servletRegistrationBean;
    }

    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        statFilter.setSlowSqlMillis(1000);

        return statFilter;
    }

    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();

        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);

        return wallFilter;
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
