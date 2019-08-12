package com.nyd.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.OrderChannelContract;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.dao.mapper.ProportionMapper;
import com.nyd.order.entity.ChannelProportionConfig;
import com.nyd.order.model.ChannelInfo;
import com.nyd.order.model.Gem;
import com.nyd.order.service.util.DateUtil;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
@Service("orderChannelContract")
public class OrderChannelContractImpl implements OrderChannelContract {
    final Logger LOGGER = LoggerFactory.getLogger(OrderChannelContractImpl.class);
    @Autowired
    private ProportionMapper proportionMapper;
    @Autowired
    private OrderMapper orderMapper;


    @Override
    public ResponseData getChannel() {
        LOGGER.info("开始查询所有在使用的资金渠道");
        ResponseData responseData = ResponseData.success();
        try {
            List<ChannelProportionConfig> proportions = proportionMapper.selectChannelList();
            if (proportions.size() <= 0) {
                LOGGER.error("未能查到任何在使用中的资金渠道!");
                return ResponseData.error("未能查到任何在使用中的资金渠道!");
            }
            //筛选本时段可用资金渠道
            queryUserProportions(proportions);
            
            LOGGER.info("最终参与分流渠道：" + JSON.toJSONString(proportions));
            List<Gem> gums = new ArrayList<>();
            int ratio = 0;
            //使用比例进行分流
            for (ChannelProportionConfig proportion : proportions) {
                gums.add(new Gem(proportion.getChannelCode(), proportion.getChannelRatio()));
                ratio += proportion.getChannelRatio();
            }
            //分流逻辑

            int random = new Random().nextInt(ratio);
            // 比例
            int prizeRate = 0;

            String channel = null;
            Iterator<Gem> it = gums.iterator();
            while (it.hasNext()) {
                Gem gem = it.next();
                prizeRate += gem.getPriority();
                if (random < prizeRate) {
                    channel = gem.getName();
                    break;
                }
            }
            if (proportions.get(0).getRatioUse() == 0) {
                return responseData.setData(channel);
                //使用份数进行分流
            } else {
                //根据渠道查询当天订单数
                boolean flag = false;
                while (!flag) {
                    int count = orderMapper.selectTodayOrderCountByFundCode(channel);
                    ChannelProportionConfig proportionConfig = proportionMapper.selectChannelConfigByCode(channel);
                    if (count < proportionConfig.getChannelLimit()) {
                        flag = true;
                        responseData.setData(channel);
                        break;
                    } else {
                        if (proportions.size()==0){
                            return responseData.setData(channel);
                        }
                        for(int i = 0; i < proportions.size() ; i ++){
                            if (proportions.get(i).equals(proportionConfig)){
                                proportions.remove(i);
                                break;
                            }
                        }
                        ratio = 0;
                        List<Gem> gums1 = new ArrayList<>();
                        for (ChannelProportionConfig proportion : proportions) {
                            gums1.add(new Gem(proportion.getChannelCode(), proportion.getChannelRatio()));
                            ratio += proportion.getChannelRatio();
                        }
                        random = new Random().nextInt(ratio);
                        // 比例
                        int prizeRate1 = 0;
                        Iterator<Gem> it1 = gums1.iterator();
                        while (it1.hasNext()) {
                            Gem gem = it1.next();
                            prizeRate1 += gem.getPriority();
                            if (random < prizeRate1) {
                                channel = gem.getName();
                                break;
                            }
                        }
                    }
                }
                return responseData;
            }
        } catch (Exception e) {
            LOGGER.error("查询所有在使用的资金渠道发生异常", e);
            return ResponseData.error("查询所有在使用的资金渠道发生异常");
        }
    }
    @Override
    public ResponseData getChannelWithAmount(BigDecimal currentAmount) {
    	LOGGER.info("开始查询所有在使用的资金渠道");
    	ResponseData responseData = ResponseData.success();
    	try {
    		List<ChannelProportionConfig> proportions = proportionMapper.selectChannelList();
    		if (proportions.size() <= 0) {
    			LOGGER.error("未能查到任何在使用中的资金渠道!");
    			return ResponseData.error("未能查到任何在使用中的资金渠道!");
    		}
    		//筛选本时段可用资金渠道
    		queryUserProportions(proportions,currentAmount);
    		
    		LOGGER.info("最终参与分流渠道：" + JSON.toJSONString(proportions));
    		List<Gem> gums = new ArrayList<>();
    		int ratio = 0;
    		//使用比例进行分流
    		for (ChannelProportionConfig proportion : proportions) {
    			gums.add(new Gem(proportion.getChannelCode(), proportion.getChannelRatio()));
    			ratio += proportion.getChannelRatio();
    		}
    		//分流逻辑
    		
    		int random = new Random().nextInt(ratio);
    		// 比例
    		int prizeRate = 0;
    		
    		String channel = null;
    		Iterator<Gem> it = gums.iterator();
    		while (it.hasNext()) {
    			Gem gem = it.next();
    			prizeRate += gem.getPriority();
    			if (random < prizeRate) {
    				channel = gem.getName();
    				break;
    			}
    		}
    		if (proportions.get(0).getRatioUse() == 0) {
    			return responseData.setData(channel);
    			//使用份数进行分流
    		} else {
    			//根据渠道查询当天订单数
    			boolean flag = false;
    			while (!flag) {
    				int count = orderMapper.selectTodayOrderCountByFundCode(channel);
    				ChannelProportionConfig proportionConfig = proportionMapper.selectChannelConfigByCode(channel);
    				if (count < proportionConfig.getChannelLimit()) {
    					flag = true;
    					responseData.setData(channel);
    					break;
    				} else {
    					if (proportions.size()==0){
    						return responseData.setData(channel);
    					}
    					for(int i = 0; i < proportions.size() ; i ++){
    						if (proportions.get(i).equals(proportionConfig)){
    							proportions.remove(i);
    							break;
    						}
    					}
    					ratio = 0;
    					List<Gem> gums1 = new ArrayList<>();
    					for (ChannelProportionConfig proportion : proportions) {
    						gums1.add(new Gem(proportion.getChannelCode(), proportion.getChannelRatio()));
    						ratio += proportion.getChannelRatio();
    					}
    					random = new Random().nextInt(ratio);
    					// 比例
    					int prizeRate1 = 0;
    					Iterator<Gem> it1 = gums1.iterator();
    					while (it1.hasNext()) {
    						Gem gem = it1.next();
    						prizeRate1 += gem.getPriority();
    						if (random < prizeRate1) {
    							channel = gem.getName();
    							break;
    						}
    					}
    				}
    			}
    			return responseData;
    		}
    	} catch (Exception e) {
    		LOGGER.error("查询所有在使用的资金渠道发生异常", e);
    		return ResponseData.error("查询所有在使用的资金渠道发生异常");
    	}
    }


    /**
     * 根据资金渠道关闭时间段筛选有效渠道
     * @param proportions
     */
	private void queryUserProportions(List<ChannelProportionConfig> proportions) {
		String closeTimes = null;
		LOGGER.info("使用中的渠道：" + JSON.toJSONString(proportions));
		List<ChannelProportionConfig> ret = new ArrayList<ChannelProportionConfig>();
		for(ChannelProportionConfig proportion:proportions) {
			closeTimes = proportion.getCloseTimes();
			if(StringUtils.isBlank(closeTimes)) {
				continue;
			}
			String[] split1 = closeTimes.split(";");
			if(split1 == null || split1.length < 1) {
				continue;
			}
			for(String colseTime:split1) {
				String[] split2 = colseTime.split("-");
				String start = null;
				String end = null;
				List<String> times = Arrays.asList(split2);
				start = times.get(0);
				end = times.get(1);
				if(DateUtil.isEffectiveDate(DateUtil.dateToHms(new Date()), start, end)) {
					//proportions.remove(proportion);
					ret.add(proportion);
					continue;
				}
			}
			BigDecimal max = BigDecimal.ZERO;
			try {
				max = orderMapper.getSuccessRemitAmount(proportion.getChannelCode());
				if(proportion.getMaxAmount() != null && proportion.getMaxAmount().compareTo(max) <= 0) {
					ret.add(proportion);
				}
			}catch(Exception e) {
				LOGGER.error("查询当然放款金额失败：" + e.getMessage());
				continue;
			}
		}
		for(ChannelProportionConfig cinf:ret) {
			proportions.remove(cinf);
		}
		LOGGER.info("时间过滤和最大限额过滤后渠道：" + JSON.toJSONString(proportions));
	}
	/**
	 * 根据资金渠道关闭时间段筛选有效渠道
	 * @param proportions
	 */
	private void queryUserProportions(List<ChannelProportionConfig> proportions,BigDecimal currentAmount) {
		String closeTimes = null;
		LOGGER.info("使用中的渠道：" + JSON.toJSONString(proportions));
		List<ChannelProportionConfig> ret = new ArrayList<ChannelProportionConfig>();
		for(ChannelProportionConfig proportion:proportions) {
			closeTimes = proportion.getCloseTimes();
			if(StringUtils.isBlank(closeTimes)) {
				continue;
			}
			String[] split1 = closeTimes.split(";");
			if(split1 == null || split1.length < 1) {
				continue;
			}
			for(String colseTime:split1) {
				String[] split2 = colseTime.split("-");
				String start = null;
				String end = null;
				List<String> times = Arrays.asList(split2);
				start = times.get(0);
				end = times.get(1);
				if(DateUtil.isEffectiveDate(DateUtil.dateToHms(new Date()), start, end)) {
					//proportions.remove(proportion);
					ret.add(proportion);
					continue;
				}
			}
//			BigDecimal max = BigDecimal.ZERO;
//			try {
//				max = orderMapper.getSuccessRemitAmount(proportion.getChannelCode());
//				LOGGER.info("渠道编码：" + proportion.getChannelCode() + "最大限额：" + proportion.getMaxAmount() + "今日成功金额：" + max.add(currentAmount));
//				if(proportion.getMaxAmount() != null && proportion.getMaxAmount().compareTo(max.add(currentAmount)) <= 0) {
//					ret.add(proportion);
//				}
//			}catch(Exception e) {
//				LOGGER.error("查询当然放款金额失败：" + e.getMessage());
//				continue;
//			}
		}
		for(ChannelProportionConfig cinf:ret) {
			proportions.remove(cinf);
		}
		LOGGER.info("时间过滤和最大限额过滤后渠道：" + JSON.toJSONString(proportions));
	}

	@Override
	public boolean ifReachMaxAmount(String code,BigDecimal currentAmount) {
		ChannelProportionConfig channel = proportionMapper.selectChannelConfigByCode(code);
		BigDecimal max = BigDecimal.ZERO;
		try {
			max = orderMapper.getSuccessRemitAmount(channel.getChannelCode());
			LOGGER.info("渠道编码：" + code + "最大限额：" + channel.getMaxAmount() + "今日成功金额：" + max.add(currentAmount));
			if(channel.getMaxAmount() != null && channel.getMaxAmount().compareTo(max.add(currentAmount)) <= 0) {
				return true;
			}
		}catch(Exception e) {
			LOGGER.error("查询放款金额失败：" + e.getMessage());
		}
		return false;
	}
	

	@Override
	public ResponseData getChannelByCode(String code) {
		ChannelProportionConfig channel = proportionMapper.selectChannelConfigByCode(code);
		return ResponseData.success(channel);
	}
	
	/**
     * 获取所有资金渠道
     * @return
     */
	@Override
    public List<ChannelInfo> getAllChannelConfig(ChannelInfo info){
    	List<ChannelInfo> proportions = proportionMapper.selectAllChannelList(info);
    	return proportions;
    }


	@Override
	public void save(ChannelInfo channelInfo) {
		proportionMapper.save(channelInfo);
		
	}


	@Override
	public void update(ChannelInfo channelInfo) {
		proportionMapper.update(channelInfo);
		
	}
}
