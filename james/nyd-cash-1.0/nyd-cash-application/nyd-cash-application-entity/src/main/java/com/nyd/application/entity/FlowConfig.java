package com.nyd.application.entity;

import java.util.Date;

import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_flow_config")
public class FlowConfig {
	@Id
	private int id;
	
    private String channelId;
    
    private String partnerName;
    
    private String appId;
    
    private Integer publicValidate;
    
    private String publicKey;
    
    private Integer deleteFlag;
    
    private  Date createTime;
    
    private  Date updateTime;
    
    private String updateBy;

}
