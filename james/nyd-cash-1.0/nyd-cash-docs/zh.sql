CREATE SCHEMA IF NOT EXISTS `zhfw`;


CREATE TABLE IF NOT EXISTS `zhfw`.`role` (
  `id`     BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name`   VARCHAR(16),
  `status` INT(5),
  `descr`  VARCHAR(16),
  KEY `id` (`id`)
);

CREATE TABLE IF NOT EXISTS `zhfw`.`resources` (
  `id`     BIGINT(11) NOT NULL AUTO_INCREMENT,
  `pid`    BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name`   VARCHAR(16),
  `val`    VARCHAR(16),
  `icon`   VARCHAR(16),
  `target` VARCHAR(16),
  `status` INT(5),
  `sort`   INT(5),
  `descr`  VARCHAR(16),
  KEY `id` (`id`)
);

CREATE TABLE IF NOT EXISTS `zhfw`.`account` (
  `id`      BIGINT(11) NOT NULL AUTO_INCREMENT,
  `rid`     BIGINT(11),
  `account` VARCHAR(16),
  `pwd`     VARCHAR(16),
  `nick`    VARCHAR(16),
  `status`  INT(5),
  `type`    INT(5),
  KEY `id` (`id`)
);

CREATE TABLE IF NOT EXISTS `zhfw`.`account_role` (
  `aid` BIGINT(11) NOT NULL,
  `rid` BIGINT(11) NOT NULL,
  KEY `aid` (`aid`),
  KEY `rid` (`rid`)
);


CREATE TABLE IF NOT EXISTS `zhfw`.`resources` (
  `id`     BIGINT(11) NOT NULL AUTO_INCREMENT,
  `pid`    BIGINT(11),
  `name`   VARCHAR(16),
  `val`    VARCHAR(16),
  `icon`   VARCHAR(16),
  `descr`  VARCHAR(120),
  `status` INT(5),
  `level`  INT(5),
  `sort`   INT(5),
  `typ`    INT(5),
  `target` INT(5),
  KEY `id` (`id`)
);


CREATE TABLE IF NOT EXISTS `zhfw`.`account_role` (
  `role_id`     BIGINT(11) NOT NULL,
  `resource_id` BIGINT(11) NOT NULL,
  KEY `roleId` (`role_id`),
  KEY `resource_id` (`resource_id`)
);


DROP TABLE IF EXISTS `zhfw`.`product`;
CREATE TABLE `zhfw`.`product` (
  `id`     INT(11) NOT NULL AUTO_INCREMENT,
  `bn`     INT(11),
  `brand`  VARCHAR(64),
  `type`   VARCHAR(64),
  `series` VARCHAR(64),
  `psn`    VARCHAR(32),
  `number` VARCHAR(64),
  `sku`    VARCHAR(60),
  `model`  VARCHAR(15),
  `descr`  CHAR(30),
  `ptime`  DATE,
  `gtime`  DATE,
  KEY id (id)
);

DROP TABLE IF EXISTS `zhfw`.`product_warehouse`;
CREATE TABLE `zhfw`.`product_warehouse` (
  `id`     INT(11) NOT NULL AUTO_INCREMENT,
  `wid`    INT(11),
  `counts` INT(11),
  `bn`     INT(11),
  `brand`  VARCHAR(64),
  `type`   CHAR(64),
  `series` VARCHAR(30),
  `model`  VARCHAR(64),
  `stime`  DATE,
  KEY id (id)
);


DROP TABLE IF EXISTS `zhfw`.`user`;
CREATE TABLE `zhfw`.`user` (
  `id`          INT(11) NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(64),
  `age`         TINYINT(1),
  `sex`         TINYINT(1),
  `idcard`      VARCHAR(32),
  `create_time` DATE,
  KEY id (id)
);


DROP TABLE IF EXISTS `zhfw`.`adress`;
CREATE TABLE `zhfw`.`adress` (
  `id`        INT(11) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `rid`       INT(11) COMMENT '关联id',
  `province`  VARCHAR(18) COMMENT '省',
  `city`      VARCHAR(18) COMMENT '市',
  `area`      VARCHAR(18) COMMENT '区',
  `adress`    VARCHAR(32) COMMENT '街道',
  `community` VARCHAR(32) COMMENT '社区',
  `room`      VARCHAR(32) COMMENT '住址',
  KEY id (id)
);


DROP TABLE IF EXISTS `zhfw`.`phone`;
CREATE TABLE `zhfw`.`phone` (
  `id`     INT(11) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `rid`    INT(11) COMMENT '关联id',
  `typ`    TINYINT(1) COMMENT '0电话/1手机',
  `number` VARCHAR(18) COMMENT '号码',
  KEY id (id)
);


DROP TABLE IF EXISTS `zhfw`.`login`;
CREATE TABLE `zhfw`.`login` (
  `id`   INT(11) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `rid`  INT(11) COMMENT '关联id',
  `name` VARCHAR(18) COMMENT '用户名',
  `pwd`  VARCHAR(18) COMMENT '密码',
  KEY id (id)
);

DROP TABLE IF EXISTS `zhfw`.`account`;
CREATE TABLE `zhfw`.`account` (
  `id`      INT(11) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `rid`     INT(11) COMMENT '关联id',
  `account` VARCHAR(18) COMMENT '账号',
  `card`    VARCHAR(32) COMMENT '关联银行卡',
  `bank`    VARCHAR(32) COMMENT '所属银行',
  `pwd`     VARCHAR(18) COMMENT '密码',
  KEY id (id)
);


DROP TABLE IF EXISTS `zhfw`.`company`;
CREATE TABLE `zhfw`.`company` (
  `id`         BIGINT(11) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `rid`        BIGINT(11) COMMENT '关联id',
  `typ`        INT(11) COMMENT '公司性质或类型（个人0/股份1/外企3/生产4/经销5）',
  `name`       VARCHAR(18) COMMENT '名称',
  `legal_name` VARCHAR(18) COMMENT '法人',
  `cellphone`  VARCHAR(18) COMMENT '手机',
  `telephone`  VARCHAR(18) COMMENT '电话',
  `email`      VARCHAR(64) COMMENT '企业邮箱',
  `licence`    VARCHAR(32) COMMENT '营业执照',
  `province`   VARCHAR(32) COMMENT '省',
  `city`       VARCHAR(18) COMMENT '市',
  `district`   VARCHAR(32) COMMENT '区',
  `address`    VARCHAR(18) COMMENT '地址',
  `descr`      VARCHAR(32) COMMENT '描述',
  KEY id (id)
);

DROP TABLE IF EXISTS `zhfw`.`brand`;
CREATE TABLE `zhfw`.`brand` (
  `id`     BIGINT(11) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `rid`    BIGINT(11) COMMENT '关联id',
  `typ`    INT(8) COMMENT '公司性质或类型（个人0/股份1/外企3/生产4/经销5）',
  `name`   VARCHAR(18) COMMENT '名称',
  `logo`   VARCHAR(18) COMMENT 'logo',
  `url`    VARCHAR(18) COMMENT 'url',
  `weight` INT(8) COMMENT '品牌权重',
  `descr`  VARCHAR(32) COMMENT '描述',
  KEY id (id)
);

DROP TABLE IF EXISTS `zhfw`.`production_config`;
CREATE TABLE `zhfw`.`production_config` (
  `id`     BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `batch`    INT(11) COMMENT '生产批次',
  `count`    INT(8) COMMENT '生产数量',
  `brand`   VARCHAR(18) COMMENT '品牌',
  `serial`   VARCHAR(18) COMMENT 'logo',
  `model`    VARCHAR(18) COMMENT '型号',
  `province`    VARCHAR(18) COMMENT '生产省份',
  `city`    VARCHAR(18) COMMENT '城市',
  `area_code`   VARCHAR(18) COMMENT '区域代码',
  `warehouse`   VARCHAR(18) COMMENT '存放仓库',
  `ptime`    DATE COMMENT '生产时间',
  `gtime`   DATE COMMENT '保修时间',
  `weight` INT(8) COMMENT '品牌权重',
  `descr`  VARCHAR(32) COMMENT '描述',
  KEY id (id)
);

















