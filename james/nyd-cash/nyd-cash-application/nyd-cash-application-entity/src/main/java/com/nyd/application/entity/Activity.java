package com.nyd.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

import javax.persistence.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_activity")
public class Activity {
	
	    @Id
	    private Long id;
	    //图片地址
	    private String imgUrl;
	    //活动跳转地址
	    private String linkUrl;
	    //活动标题
	    private String title;
	    //按钮名称
	    private String btnName;
	    //app名称
	    private String appName;
	    //是否展示
	    private Integer display;
	    //是否已删除
	    private Integer deleteFlag;
	    //添加时间
	    private Date createTime;
	    //更新时间
	    private Date updateTime;
	    //修改人
	    private String updateBy;

}
