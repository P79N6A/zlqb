package com.nyd.user.service.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceErrorRetryStrategy implements ServiceUnavailableRetryStrategy {

    /**
     * Maximum number of allowed retries if the server responds with a HTTP code
     * in our retry code list. Default value is 1.
     */
    private final int maxRetries;

    private static final Logger LOG = LoggerFactory.getLogger(ServiceErrorRetryStrategy.class);
    
    /**
     * Retry interval between subsequent requests, in milliseconds. Default
     * value is 1 second.
     */
    private final long retryInterval;
    
    public ServiceErrorRetryStrategy(final int maxRetries, final int retryInterval) {
        super();
        Args.positive(maxRetries, "Max retries");
        Args.positive(retryInterval, "Retry interval");
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
    }

    public ServiceErrorRetryStrategy() {
        this(1, 1000);
    }

    @Override
    public boolean retryRequest(final HttpResponse response, final int executionCount, final HttpContext context) {
    	boolean isTry = executionCount <= maxRetries &&
                (response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR
                || response.getStatusLine().getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE);
        if (isTry)
        	LOG.info("Service error, try again.");
        return isTry;
    }

    @Override
    public long getRetryInterval() {
        return retryInterval;
    }


}
