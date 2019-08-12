CREATE TABLE `t_collection_record` (
  `id` varchar(100) NOT NULL,
  `order_no` varchar(20) NOT NULL COMMENT '订单号',
  `user_id` varchar(20) NOT NULL COMMENT '用户Id',
  `phone` varchar(20) DEFAULT NULL COMMENT '催收手机号',
  `name` varchar(50) DEFAULT NULL COMMENT '催收姓名',
  `is_promise_repay` tinyint(4) DEFAULT NULL COMMENT '是否承诺还款：0-是，1-否',
  `sys_user_id` varchar(40) DEFAULT NULL COMMENT '添加催记催收人员id',
  `sys_user_name` varchar(40) DEFAULT NULL COMMENT '添加催记催收人员姓名',
  `relation_code` tinyint(10) DEFAULT NULL COMMENT '联系人关系[0:本人,1:重要联系人,2:公司电话,3:通讯录联系人]',
  `relation_msg` varchar(40) DEFAULT NULL COMMENT '联系人关系说明',
  `remark` varchar(500) DEFAULT NULL COMMENT '催收备注',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态 0：启用；1：停用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `index_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='催记流水表';

CREATE TABLE `t_bill_repay_param` (
  `id` varchar(50) NOT NULL COMMENT '标识',
  `reference_hour` int(10) NOT NULL COMMENT '还款时间标准',
  `reference_money` decimal(10,2) NOT NULL COMMENT '剩余应还金额',
  `latefee_max` decimal(10,2) NOT NULL COMMENT '滞纳金最大值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='跑批还款配置表';

INSERT INTO `t_bill_repay_param` VALUES ('1', 16, 1000.00, 100.00);

alter table t_bill_distribution_record add pay_status varchar(10) DEFAULT NULL COMMENT '1贷中 2贷后';

alter table t_bill add urge_status int(5) DEFAULT '0' COMMENT '0默认值 1:待催收 2催收中 3 承若还款';


CREATE TABLE `t_bill_sms_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `bill_no` varchar(255) DEFAULT NULL COMMENT 'bill表主键',
  `status` varchar(255) DEFAULT NULL COMMENT '发送状态1已发送 0未发送',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `send_content` varchar(255) DEFAULT NULL COMMENT '发送内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `order_no` varchar(255) DEFAULT NULL COMMENT '订单号',
  `channel` varchar(255) DEFAULT NULL COMMENT '短信渠道',
  `create_user` varchar(255) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `t_bill_sms_reply` (
  `id` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `mobile` varchar(11) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '手机号',
  `content` varchar(1000) DEFAULT NULL COMMENT '回复内容',
  `deliver_time` datetime DEFAULT NULL COMMENT '接收时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
