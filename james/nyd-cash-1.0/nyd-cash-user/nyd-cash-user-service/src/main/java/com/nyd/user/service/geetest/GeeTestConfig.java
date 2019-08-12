package com.nyd.user.service.geetest;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liuqiu
 */
@Component
@Getter
public class GeeTestConfig {

	@Value("${geetest.failback}")
	private String newfailback;
}
