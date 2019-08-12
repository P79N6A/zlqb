package com.nyd.capital.service;

import com.nyd.capital.service.impl.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.nyd.capital.model.enums.FundSourceEnum;

/**
 * Cong Yuxiang 2017/11/13
 **/
@Component
public class FundFactory implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	public FundService buildChannel(FundSourceEnum code) {
		if (code == FundSourceEnum.WSM) {
			return applicationContext.getBean(WsmFundService.class);
		} else if (code == FundSourceEnum.QCGZ) {
			return applicationContext.getBean(QcgzFundService.class);
		} else if (code == FundSourceEnum.KZJR) {
			return applicationContext.getBean(KzjrFundService.class);
		} else if (code == FundSourceEnum.JX) {
			return applicationContext.getBean(JxFundService.class);
		} else if (code == FundSourceEnum.DLD) {
			return applicationContext.getBean(DldFundService.class);
		} else if (code == FundSourceEnum.KDLC) {
			return applicationContext.getBean(KdlcFundService.class);
		}else {
			return applicationContext.getBean(DldFundService.class);
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
