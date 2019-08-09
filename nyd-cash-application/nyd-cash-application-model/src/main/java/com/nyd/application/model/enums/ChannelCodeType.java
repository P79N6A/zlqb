package com.nyd.application.model.enums;

/**
 * Created by lk on 2019/08/07
 */
public enum ChannelCodeType {
	//假设常捷对应的大庆市语诗铭科技有限公司
    CHANNEL_CHANGJIE("changjie","大庆市语诗铭科技有限公司"),
    //假设讯联对应的B新网签公司
    CHANNEL_XUNLIAN("xunlian","B新网签公司"),
    CHANNEL_THREE("three","新渠道3"),
    CHANNEL_FOUR("four","新渠道4"),
    CHANNEL_FIVE("five","新渠道5");

    private String type;

    private String companyName;

    private ChannelCodeType(String type, String companyName){
        this.type = type;
        this.companyName = companyName;
    }

    public String getType(){
        return this.type;
    }

    public String getCompanyName(){
        return this.companyName;
    }
    
    /**
	 * 自己定义一个静态方法,通过code返回枚举常量对象
	 * @param code
	 * @return
	 */
	public static ChannelCodeType getCompany(String code){
		
		for (ChannelCodeType channel : values()) {
			if(channel.getType().equals(code)){
				return channel;
			}
		}
		return null;
	}
}
