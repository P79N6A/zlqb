package com.creativearts.nyd.pay.service.zzl.helibao;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.nyd.order.entity.zzl.HelibaoFilesConfig;
import com.nyd.pay.api.zzl.HelibaoFilesConfigService;
import com.nyd.pay.dao.mapper.HelibaoFilesConfigMapper;



@Service
public class HelibaoFilesConfigServiceImpl implements HelibaoFilesConfigService {

	@Autowired
    private HelibaoFilesConfigMapper mapper;

	@Override
	@Cacheable(value = ConfigCacheManager.HLB_NAMES_DEFAULT, key = "'DICT-'+#dic")
	public HelibaoFilesConfig queryHelibaoFilesConfigInfo() {
		System.out.println("===============初始合利宝配置文件============");
		HelibaoFilesConfig config = gethelibaoConfig();
		String pubPath = getClass().getClassLoader().getResource("").getPath();
		config.setCertPath(pubPath + config.getCertPath().replace("/", File.separator));
		config.setPfxPath(pubPath + config.getPfxPath().replace("/", File.separator));

		
		//List<HelibaoFilesConfig> list  = mapper.queryHelibaoFilesConfigInfo();
//		if(null != list && list.size()>0){
//			config = list.get(0);
//			config.setCertPath(pubPath + config.getCertPath().replace("/", File.separator));
//			config.setPfxPath(pubPath + config.getPfxPath().replace("/", File.separator));
//		}
		return config;
	}
	
	public static HelibaoFilesConfig gethelibaoConfig(){
		HelibaoFilesConfig helibaoFilesConfig = new HelibaoFilesConfig();
		helibaoFilesConfig.setCertPath("helibaoFiles/sit/helipay.cer");
		helibaoFilesConfig.setCustomerNumber("C1801121010");
		helibaoFilesConfig.setPfxPath("helibaoFiles/sit/C1801121010.pfx");
		helibaoFilesConfig.setPfxPwd("ZhuLeQianBao168");
		helibaoFilesConfig.setPayIp("47.103.128.134");
		helibaoFilesConfig.setPayUrl("http://pay.trx.helipay.com/trx/quickPayApi/interface.action");
		helibaoFilesConfig.setPayCallbackUrl("http://47.103.128.134:8095/zfm/api/notify/quickPayConfirmPay");;
		helibaoFilesConfig.setEntrustedCallbackUrl("http://47.103.128.134:8085/zfm/api/notify/entrustedLoanPay");
		helibaoFilesConfig.setEntrustedLoanUrl("http://transfer.trx.helipay.com/trx/entrustedLoan/interface.action");
		helibaoFilesConfig.setEntrustedUploanUrl("http://transfer.trx.helipay.com/trx/entrustedLoan/upload.action");
		helibaoFilesConfig.setSignkey("5nTKU7bxpUMIIFCHeSDo9IvZKfgHhE3J");
		helibaoFilesConfig.setSignkeyPrivate("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALVdfUuDsEzYY3BeDzBF/b3re5fyFDUjx//uGJiFHTVxdSV/KUwwhJhZN0J5HcQdrQ04E3Iec5bSGC+JfWlbVdvy9IlpdEYbxi0qZcsH+cy3Ne39GWWGorkAoU9iEHwWBMLqeNilr2AbmNZcQpt7DinL2w5jNxS+1pXuL4b/RVPPAgMBAAECgYBRT5bK8kFqUmFSkXjxxY9bRGXm22tf36jc/xwb1SIhQbqpkvcq512rB+SUcsR7lmptUMS3FxvvdEXPmi6Vu5MkBe0ZMypLtPxNx5dwqsOJd/ZFT7b9qAi722o45BfV9rd9YjeWYNn4cEnt9ZLrfgaqHBaKSS21/1UKfqG3vjLqsQJBANtmuivQ9ftEisbtQ3z/dAqTMpYbo5KZP64vkdORFcY+xD5TtpSQDHLJLlRtEdyr18DvTdYpC7o4oimu9eCJk6UCQQDTnnney2tRNgAawqxciW0edlIHlVBYvIF+Tc5en4O9G0rcIHUzZUMV24najf79UUthJ5sfiOSkQvl394u8e19jAkEAt8ZJZRBIKmsUT2JH6HjVS1JWyhNmpRIGnDGuVWeutHq4yHg4dCJguvk2/HLLxmqOc0Y/jYaeEyMC+iVaQPUcHQJASKFCmKneamALRyP7fkMYdXUMkFe53MrN8uiHZMiAsX3VgpmNQBeIH89aj+1eT9j/8xdh0T/toUbvUjJe/lClmwJABZnRVFZXJGfm8Wp3RuZkzz5Ey8tnRdwAgCqZrmf0VEt1TkUny1I40VUMKmMDLAhzZgHScS3R7lESeOU7GyjwWg==");
		helibaoFilesConfig.setDeskeyKey("xglHJa0H1QKQi1Z9aaqnL0l2");
		helibaoFilesConfig.setGoodsName("XXD");
		helibaoFilesConfig.setRemark("helibaoconfig");
		return helibaoFilesConfig;
	}
}
