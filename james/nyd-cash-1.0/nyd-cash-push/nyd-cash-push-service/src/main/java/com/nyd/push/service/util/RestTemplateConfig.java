package com.nyd.push.service.util;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

    @Bean(name="defaultRestTemplate")
	public RestTemplate commonRestTemplate(ClientHttpRequestFactory defatulClientHttpRequestFactory) {
		return new RestTemplate(defatulClientHttpRequestFactory);
	}

    @Bean(name="defatulClientHttpRequestFactory")
    public ClientHttpRequestFactory defatulClientHttpRequestFactory() {
        return RequestFactoryUtil.getClientHttpRequestFactory(30,   201, 0, 5000, 3600000, 5000);
    }

    private static class RequestFactoryUtil
    {
        public static ClientHttpRequestFactory getClientHttpRequestFactory(int keepAlive, int maxTotal, int retryCount, int connectTimeout,int readTimeout,int  waitingForConnectionTimeOut)
        {
            PoolingHttpClientConnectionManager pollingConnectionManager =
                    new PoolingHttpClientConnectionManager(keepAlive, TimeUnit.SECONDS); // 长连接保持30

            pollingConnectionManager.setMaxTotal(maxTotal); // 总连接数,默认在服务器满负荷的情况下200（线程池全部用完），每个线程池用1个连接
            pollingConnectionManager.setDefaultMaxPerRoute(maxTotal); // 同路由的并发数

            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setRetryHandler(new DefaultMJHttpRequestRetryHandler(retryCount)); // 超时重试次数2次，就是调用3次
            httpClientBuilder.setConnectionManager(pollingConnectionManager);
            httpClientBuilder.setServiceUnavailableRetryStrategy(new ServiceErrorRetryStrategy());

            HttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                    new HttpComponentsClientHttpRequestFactory(httpClient);
            clientHttpRequestFactory.setConnectTimeout(connectTimeout); // 连接超时5秒
            clientHttpRequestFactory.setReadTimeout(readTimeout); // 数据读取超时时间6000秒，即SocketTimeout
            clientHttpRequestFactory.setConnectionRequestTimeout(waitingForConnectionTimeOut); // 连接不够用的等待时间6秒，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
            return clientHttpRequestFactory;
        }
    }

    private static class DefaultMJHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {
        private final int retryCount;
        DefaultMJHttpRequestRetryHandler(int retryCount) {
            this.retryCount = retryCount;
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount,
                                    HttpContext context) {
            if (executionCount > this.retryCount) {
                return false;
            }
            //所有异常都重试
            return true;
        }
    }
}
