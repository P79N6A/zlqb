/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : local_wsm

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2017-09-01 11:55:54
*/

CREATE DATABASE local_wsm; 
USE local_wsm 

-- SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `wsm_test_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `wsm_test_userinfo`;
CREATE TABLE `wsm_test_userinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `xm` varchar(20) DEFAULT NULL COMMENT '姓名',
  `sfzh` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `sfzzpdz` varchar(50) DEFAULT NULL COMMENT '身份证照片地址',
  `sfzzpbddz` varchar(50) DEFAULT NULL COMMENT '身份证照片本地地址',
  `sflx` tinyint(3) unsigned NOT NULL COMMENT '身份类型(1在职 2在读 3待业)',
  `sjh` varchar(20) NOT NULL COMMENT '手机号',
  `dzxx` varchar(200) DEFAULT NULL COMMENT '地址信息',
  `hyzk` tinyint(3) unsigned NOT NULL COMMENT '婚姻状况(1未婚 2已婚 3丧偶 4离婚)',
  `jkzk` tinyint(3) unsigned NOT NULL COMMENT '健康状况(1健康 2良好 3一般 4较弱)',
  `zgxl` tinyint(3) NOT NULL COMMENT '最高学历(1博士研究生 2硕士研究生 3大学本科 4大学专科和专科学校 5中等专业学校或中等技术学校 6技术学院 7高中 8初中 9小学 10未知)',
  `zy` varchar(200) DEFAULT NULL COMMENT '专业',
  `yx` varchar(200) DEFAULT NULL COMMENT '院校',
  `gsmc` varchar(200) DEFAULT NULL COMMENT '公司名称',
  `gsdh` varchar(20) DEFAULT NULL COMMENT '公司电话',
  `yhklx` tinyint(3) unsigned DEFAULT NULL COMMENT '银行卡类型(1借记卡 2信用卡)',
  `kh` varchar(50) DEFAULT NULL COMMENT '卡号',
  `khh` int(4) DEFAULT NULL COMMENT '银行列表(配置文件)',
  `ylsjh` varchar(15) DEFAULT NULL COMMENT '预留手机号',
  `sqje` int(11) DEFAULT NULL COMMENT '申请金额(单位分)',
  `dkyt` tinyint(3) unsigned DEFAULT NULL COMMENT '贷款用途(1消费 2汽车 3医美 4旅游 5教育 63C 7家装 8租房 9租赁)',
  `cpmx` varchar(200) DEFAULT NULL COMMENT '产品明细',
  `qx` tinyint(3) unsigned DEFAULT NULL COMMENT '期数--还款次数',
  `jjfwxyqysj` datetime DEFAULT NULL COMMENT '居间服务协议签约时间',
  `jjfwxyckdz` varchar(200) DEFAULT NULL COMMENT '居间服务协议查看地址',
  `casqxyqysj` datetime DEFAULT NULL COMMENT 'CA授权协议签约时间',
  `casqxyckdz` varchar(200) DEFAULT NULL COMMENT 'CA授权协议查看地址',
  `dkxyqysj` datetime DEFAULT NULL COMMENT '代扣协议签约时间',
  `dkxyckdz` varchar(100) DEFAULT NULL COMMENT '代扣协议查看地址',
  `lxrxm1` varchar(30) DEFAULT NULL COMMENT '联系人1姓名',
  `lxrdh1` varchar(20) DEFAULT NULL COMMENT '联系人1电话',
  `lxrgx1` tinyint(3) unsigned DEFAULT NULL COMMENT '联系人1关系(1.父母 2.配偶 3.亲属 4.朋友 5.同事 6.老师 7.同学 8.子女)',
  `lxrxm2` varchar(30) DEFAULT NULL COMMENT '联系人2姓名',
  `lxrdh2` varchar(20) DEFAULT NULL COMMENT '联系人2电话',
  `lxrgx2` tinyint(3) unsigned DEFAULT NULL COMMENT '联系人2关系(1.父母 2.配偶 3.亲属 4.朋友 5.同事 6.老师 7.同学 8.子女)',
  `qyszsheng` varchar(20) DEFAULT NULL COMMENT '签约所在省',
  `qyszshi` varchar(100) DEFAULT NULL COMMENT '签约所在市',
  `sfdf` tinyint(3) unsigned DEFAULT NULL COMMENT '是否代付(0.不代付 1.代付)',
  `dgkhh` varchar(50) DEFAULT NULL COMMENT '对公开户行',
  `dggsmc` varchar(50) DEFAULT NULL COMMENT '对公公司名称',
  `dgkhhbh` varchar(50) DEFAULT NULL COMMENT '对公开户行编号',
  `dgkhhkh` varchar(30) DEFAULT NULL COMMENT '对公开户行卡号',
  `dgkhhsheng` varchar(20) DEFAULT NULL COMMENT '对公开户行省',
  `dgkhhshi` varchar(20) DEFAULT NULL COMMENT '对公开户行市',
  `ts` int(11) DEFAULT NULL,
  `on_line` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户信息';

-- ----------------------------
-- Records of wsm_test_userinfo
-- ----------------------------
INSERT INTO `wsm_test_userinfo` VALUES ('1', '测试数据', '210211198703177415', 'http://weiwei.com/index.php?name=weiwei', null, '1', '13898478285', '重庆重庆市九龙坡区杨家坪团结路11号6-1', '1', '1', '1', null, null, '微神马（大连）有限公司', '0411-86603893', '1', '6228480402564890018', '1004', '13898478282', '60000', '1', null, '1', null, null, null, null, null, null, '测试', '17184033038', '1', '周', '15998616691', '2', '辽宁', '大连', null, null, null, null, null, null, null, '30', '1');
