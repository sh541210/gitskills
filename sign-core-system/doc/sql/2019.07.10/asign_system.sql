/*
SQLyog Enterprise v12.08 (64 bit)
MySQL - 5.7.19-log : Database - asign_system
*********************************************************************
*/

CREATE DATABASE `asign_system`;

USE `asign_system`;

/*Table structure for table `sign_sys_admin_user` */

DROP TABLE IF EXISTS `sign_sys_admin_user`;

CREATE TABLE `sign_sys_admin_user` (
  `id` VARCHAR(64) NOT NULL COMMENT 'snowflake',
  `user_name` VARCHAR(64) NOT NULL COMMENT '用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '密码',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` VARCHAR(64) NOT NULL COMMENT '创建人',
  `update_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(64) NOT NULL COMMENT '备注',
  `update_user` VARCHAR(64) NOT NULL COMMENT '更新人',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否，1:是)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `用户名` (`user_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='签章系统后台管理员用户';

/*Data for the table `sign_sys_admin_user` */

INSERT  INTO `sign_sys_admin_user`(`id`,`user_name`,`password`,`create_date`,`create_user`,`update_date`,`remark`,`update_user`,`is_deleted`) VALUES ('1','admin','$2a$10$zrqtDVQu9MTt5vgMPCa4NOgAWNBgiteIheiRH/dB77JEI8xoKHRki','2019-06-20 15:16:50','admin','2019-06-20 15:16:58','脚本初始化','admin',0),('2','liangqiang','$2a$10$wnyBl9Hp4X3io2p4hVV8FeVWC7vt4Mdd3t7aD8Cmn80qpTNnrHFI6','2019-06-27 09:38:28','admin','2019-06-27 09:39:55','admin创建','admin',0),('3','luwenxiong','$2a$10$y2XAc9pvPbXxoCsO/smiwuiv0soj2C0PfKOdANFh0r2Pb8/jagsCa','2019-06-27 12:09:04','admin','2019-07-10 10:54:00','admin创建','admin',0),('4','wdf','$2a$10$e68U/jzyrahDqh551uZIRe0IqW9gI5mIpqQTLGpWbHKOJ3Nxd4AF6','2019-07-06 10:58:57','admin','2019-07-10 10:54:04','admin','admin',0),('5','yml','$2a$10$/chQJsvuHAmYgp1ejjgW2.Skd4vvwgpbWadZET4sDPaUIj01E2kue','2019-06-27 10:08:52','admin','2019-07-10 10:54:07','admin创建','admin',0);

/*Table structure for table `sign_sys_application` */

DROP TABLE IF EXISTS `sign_sys_application`;

CREATE TABLE `sign_sys_application` (
  `id` VARCHAR(64) NOT NULL COMMENT '应用ID,snowflake',
  `user_app_id` VARCHAR(255) DEFAULT NULL COMMENT '分配用户应用key',
  `user_app_sceret` VARCHAR(255) NOT NULL COMMENT '分配用户应用秘钥',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `application_abled` INT(11) DEFAULT NULL COMMENT '0 启用 ；1 禁用',
  `application_delete` INT(11) DEFAULT NULL COMMENT '0 未删除 ;1 删除',
  `application_desc` VARCHAR(255) DEFAULT NULL COMMENT '应用描述',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `modify_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `filed1` VARCHAR(255) DEFAULT NULL COMMENT '扩展字段1',
  `filed2` VARCHAR(255) DEFAULT NULL COMMENT '扩展字段2',
  `application_name` VARCHAR(255) NOT NULL COMMENT '应用名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='应用表';

/*Data for the table `sign_sys_application` */

INSERT  INTO `sign_sys_application`(`id`,`user_app_id`,`user_app_sceret`,`user_id`,`application_abled`,`application_delete`,`application_desc`,`create_time`,`modify_time`,`filed1`,`filed2`,`application_name`) VALUES ('2','395CB6F10C13461FBD683EF51E62D5C5','4372C1C1DADE4FBAA97274810C1248D5',1,0,0,NULL,'2019-07-05 17:07:34','2019-07-05 17:07:34',NULL,NULL,'111');

/*Table structure for table `sign_sys_cert_info` */

DROP TABLE IF EXISTS `sign_sys_cert_info`;

CREATE TABLE `sign_sys_cert_info` (
  `cert_id` VARCHAR(64) NOT NULL,
  `org_id` VARCHAR(64) DEFAULT NULL COMMENT '机构ID',
  `cert_code` VARCHAR(100) NOT NULL COMMENT '证书路径',
  `cert_password` VARCHAR(20) NOT NULL COMMENT '证书密码',
  `cert_name` VARCHAR(50) NOT NULL COMMENT '证书名称',
  `issuer` VARCHAR(50) NOT NULL COMMENT '颁发者',
  `subject` VARCHAR(50) NOT NULL COMMENT '使用者',
  `serial_number` VARCHAR(100) NOT NULL COMMENT '序列号',
  `validity_from` DATETIME(6) NOT NULL COMMENT '有效期开始',
  `validity_exp` DATETIME(6) NOT NULL COMMENT '有效期到',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否，1:是)',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` VARCHAR(50) NOT NULL COMMENT '创建者',
  `update_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` VARCHAR(50) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`cert_id`) USING BTREE,
  KEY `org_id` (`org_id`) USING BTREE,
  CONSTRAINT `org_id` FOREIGN KEY (`org_id`) REFERENCES `sign_sys_enterprise_info` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='证书';

/*Data for the table `sign_sys_cert_info` */

/*Table structure for table `sign_sys_enterprise_info` */

DROP TABLE IF EXISTS `sign_sys_enterprise_info`;

CREATE TABLE `sign_sys_enterprise_info` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `chinese_name` VARCHAR(500) NOT NULL COMMENT '企业中文名称',
  `minority_name` VARCHAR(500) DEFAULT NULL COMMENT '企业少数民族名称',
  `english_name` VARCHAR(500) DEFAULT NULL COMMENT '企业英文名称',
  `credit_code` CHAR(50) NOT NULL COMMENT '统一社会信用代码（税号）',
  `license_code` CHAR(50) DEFAULT NULL COMMENT '工商注册号，执照号码',
  `organization_code` CHAR(50) DEFAULT NULL COMMENT '组织机构代码（统一社会信用代码第9位到17位）',
  `undeclared_code` VARCHAR(20) DEFAULT NULL COMMENT '未定义的企业某码（适用于无企业证照合一一系列码的场景）',
  `area_code` CHAR(6) DEFAULT NULL COMMENT '辖区代码（统一社会信用代码第3位到8位）',
  `status` VARCHAR(100) DEFAULT NULL COMMENT '企业状态',
  `type` VARCHAR(100) DEFAULT NULL COMMENT '企业类型',
  `nationality` CHAR(2) DEFAULT 'CN' COMMENT '国籍（C、ZH、CN）',
  `province` VARCHAR(20) DEFAULT NULL COMMENT '省份（S）',
  `city` VARCHAR(20) DEFAULT NULL COMMENT '市（L）',
  `area` VARCHAR(20) DEFAULT NULL COMMENT '区',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '企业电话（区号+手机号）',
  `address` VARCHAR(500) DEFAULT NULL COMMENT '企业详细地址（包括国籍省市区）',
  `startup_date` DATE DEFAULT NULL COMMENT '企业成立日期',
  `legal_name` VARCHAR(60) DEFAULT NULL COMMENT '法定代表人',
  `legal_telephone` CHAR(15) DEFAULT NULL COMMENT '法定代表人手机号码',
  `legal_certificate_type` CHAR(2) DEFAULT NULL COMMENT '法定代表人证件类型CertificateTypeEnum.java',
  `legal_certificate_no` VARCHAR(50) DEFAULT NULL COMMENT '法定代表人证件号码',
  `legal_address` VARCHAR(100) DEFAULT NULL COMMENT '法定代表人住址',
  `details_info` VARCHAR(200) DEFAULT NULL COMMENT '企业相信信息（企业服务保存的企业信息文件地址）',
  `organization_logo_path` VARCHAR(500) DEFAULT NULL COMMENT '企业logo',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` TINYINT(4) UNSIGNED DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  `certification_levels` VARCHAR(50) DEFAULT NULL COMMENT '认证等级',
  `unit_type` TINYINT(1) DEFAULT '0' COMMENT '单位类型(0 企业 1国家机关、事业单位等)',
  `business_end_time` DATE DEFAULT NULL COMMENT '营业期限有效结束时间',
  `business_scope` VARCHAR(100) DEFAULT NULL COMMENT '经营范围',
  `industry` VARCHAR(50) DEFAULT NULL COMMENT '所属行业',
  `company_size` VARCHAR(30) DEFAULT NULL COMMENT '公司规模',
  `publish_certification_time` DATE DEFAULT NULL COMMENT '发照日期',
  `registration_company` VARCHAR(20) DEFAULT NULL COMMENT '工商登记机关',
  `business_start_time` DATE DEFAULT NULL COMMENT '营业期限有效开始时间',
  `capital_type` VARCHAR(10) DEFAULT NULL COMMENT '注册资金币种',
  `capital` VARCHAR(50) DEFAULT NULL COMMENT '注册资本',
  `company_website_url` VARCHAR(250) DEFAULT NULL COMMENT '官网url地址',
  `company_email` VARCHAR(20) DEFAULT NULL COMMENT '公司邮箱',
  `fax` VARCHAR(20) DEFAULT NULL COMMENT '传真',
  `real_name_authentication_flag` TINYINT(1) DEFAULT '0' COMMENT '是否实名认证（0：未认证，1：认证）',
  `real_name_authentication_time` DATETIME DEFAULT NULL COMMENT '实名认证通过时间',
  `real_name_authentication_from_biz_system_code` VARCHAR(50) DEFAULT NULL COMMENT '从哪个业务系统实名认证的',
  `business_license_pic` VARCHAR(255) DEFAULT NULL COMMENT '营业执照图片路径',
  `legal_person_identity_card_front_pic` VARCHAR(255) DEFAULT NULL COMMENT '法人身份证正面图片路径',
  `legal_person_identity_card_back_pic` VARCHAR(255) DEFAULT NULL COMMENT '法人身份证反面图片路径',
  `ext_define` VARCHAR(255) DEFAULT NULL COMMENT '自定义字段',
  `node_id` VARCHAR(64) DEFAULT NULL COMMENT '节点ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_credit_code` (`credit_code`) USING BTREE COMMENT '唯一索引统一社会信用代码（税号）',
  UNIQUE KEY `uk_organization_code` (`organization_code`) USING BTREE COMMENT '唯一索引组织机构代码（统一社会信用代码第9位到17位）',
  UNIQUE KEY `uk_license_code` (`license_code`) USING BTREE COMMENT '唯一索引营业执照号（统一社会信用代码第3位到17位）',
  KEY `idx_chinese_name` (`chinese_name`) USING BTREE COMMENT '普通索引企业中文名称',
  KEY `idx_legal_name` (`legal_name`) USING BTREE COMMENT '普通索引法定代表人'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='签章系统企业账户信息';

/*Data for the table `sign_sys_enterprise_info` */

INSERT  INTO `sign_sys_enterprise_info`(`id`,`chinese_name`,`minority_name`,`english_name`,`credit_code`,`license_code`,`organization_code`,`undeclared_code`,`area_code`,`status`,`type`,`nationality`,`province`,`city`,`area`,`phone`,`address`,`startup_date`,`legal_name`,`legal_telephone`,`legal_certificate_type`,`legal_certificate_no`,`legal_address`,`details_info`,`organization_logo_path`,`remark`,`is_deleted`,`gmt_create`,`gmt_modified`,`certification_levels`,`unit_type`,`business_end_time`,`business_scope`,`industry`,`company_size`,`publish_certification_time`,`registration_company`,`business_start_time`,`capital_type`,`capital`,`company_website_url`,`company_email`,`fax`,`real_name_authentication_flag`,`real_name_authentication_time`,`real_name_authentication_from_biz_system_code`,`business_license_pic`,`legal_person_identity_card_front_pic`,`legal_person_identity_card_back_pic`,`ext_define`,`node_id`) VALUES ('591635506115117056','深圳安印科技',NULL,NULL,'421083198805414267',NULL,NULL,NULL,NULL,'01',NULL,'CN',NULL,NULL,NULL,NULL,NULL,NULL,'fsdff',NULL,NULL,NULL,NULL,NULL,NULL,'的范德萨发',0,'2019-06-20 14:28:18','2019-07-10 12:42:34',NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'水电费',NULL,'发的','50第三方第三方',NULL,0,NULL,NULL,NULL,NULL,NULL,'深圳市南山区深南大道','591606514456723458');

/*Table structure for table `sign_sys_file_resource` */

DROP TABLE IF EXISTS `sign_sys_file_resource`;

CREATE TABLE `sign_sys_file_resource` (
  `id` VARCHAR(64) NOT NULL COMMENT 'snowflake',
  `user_id` VARCHAR(64) NOT NULL COMMENT '上传资源用户Id（sys_user表中user_id）',
  `file_code` VARCHAR(128) NOT NULL COMMENT '文件资源编码UUID',
  `file_name` VARCHAR(1024) NOT NULL COMMENT '文件资源名称',
  `file_path` VARCHAR(1024) NOT NULL COMMENT '文件资源路径',
  `print_num` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '打印次数',
  `file_size` VARCHAR(100) DEFAULT '0' COMMENT '文件大小(字节)',
  `file_hash` VARCHAR(100) DEFAULT NULL COMMENT '文件HASH',
  `file_type` VARCHAR(100) DEFAULT NULL COMMENT '文件类型',
  `is_deleted` TINYINT(4) DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除）',
  `sign_count` BIGINT(20) DEFAULT '0' COMMENT '签署次数',
  `down_count` BIGINT(20) DEFAULT '0' COMMENT '下载次数',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '签署时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk_id` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='文件微服务中文件资源表';

/*Data for the table `sign_sys_file_resource` */

/*Table structure for table `sign_sys_node_info` */

DROP TABLE IF EXISTS `sign_sys_node_info`;

CREATE TABLE `sign_sys_node_info` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `parent_node_id` varchar(64) NOT NULL COMMENT '父节点ID',
  `node_name` varchar(255) NOT NULL COMMENT '组织名称',
  `remark` varchar(150) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_node_id` (`parent_node_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织架构信息表';

/*Data for the table `sign_sys_node_info` */

INSERT INTO `asign_system`.`sign_sys_node_info` (`id`, `parent_node_id`, `node_name`, `remark`, `is_deleted`, `gmt_create`, `gmt_modified`) VALUES ('591606514456723458', '0', '财务部', NULL, '0', '2019-06-21 12:23:41', NULL);

/*Table structure for table `sign_sys_personal_info` */

DROP TABLE IF EXISTS `sign_sys_personal_info`;

CREATE TABLE `sign_sys_personal_info` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `personal_name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `personal_phone` CHAR(11) DEFAULT NULL COMMENT '手机号',
  `personal_logo_path` VARCHAR(100) DEFAULT NULL COMMENT '个人头像路径',
  `personal_email` VARCHAR(50) DEFAULT NULL COMMENT '个人邮箱',
  `personal_certificate_type` CHAR(2) NOT NULL COMMENT '证件类型',
  `personal_certificate_no` VARCHAR(40) NOT NULL COMMENT '证件号码',
  `personal_address` VARCHAR(100) DEFAULT NULL COMMENT '住址',
  `personal_contact_address` VARCHAR(100) DEFAULT NULL COMMENT '联系地址',
  `nationality` CHAR(2) DEFAULT 'CN' COMMENT '国籍（C、ZH、CN）',
  `province` VARCHAR(20) DEFAULT NULL COMMENT '省份（S）',
  `city` VARCHAR(20) DEFAULT NULL COMMENT '市（L）',
  `area` VARCHAR(20) DEFAULT NULL COMMENT '区',
  `area_code` CHAR(6) DEFAULT NULL COMMENT '辖区代码（统一社会信用代码第3位到8位）',
  `gender` CHAR(1) DEFAULT '0' COMMENT '性别 0-未知 1-男 2-女',
  `birth` DATE DEFAULT NULL COMMENT '出生日期',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` TINYINT(4) UNSIGNED DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除）',
  `identity_card_front_pic` VARCHAR(255) DEFAULT NULL COMMENT '身份证正面图片路径',
  `identity_card_back_pic` VARCHAR(255) DEFAULT NULL COMMENT '身份证反面图片路径',
  `real_name_authentication_flag` TINYINT(1) DEFAULT '0' COMMENT '是否实名认证（0：未认证，1：认证）',
  `real_name_authentication_time` DATETIME DEFAULT NULL COMMENT '实名认证通过时间',
  `real_name_authentication_from_biz_system_code` VARCHAR(50) DEFAULT NULL COMMENT '从哪个业务系统实名认证的',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  `certification_levels` VARCHAR(50) DEFAULT NULL COMMENT '认证等级',
  `qq` VARCHAR(20) DEFAULT NULL COMMENT 'qq号',
  `bank_info_json` json DEFAULT NULL COMMENT '收款人信息，json格式',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_personal_certificate_no` (`personal_certificate_no`) USING BTREE COMMENT '唯一索引证件号码',
  KEY `idx_personal_name` (`personal_name`) USING BTREE COMMENT '普通索引姓名',
  KEY `idx_personal_phone` (`personal_phone`) USING BTREE COMMENT '普通索引手机号'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='签章系统个人信息表';

/*Data for the table `sign_sys_personal_info` */

/*Table structure for table `sign_sys_seal_info` */

DROP TABLE IF EXISTS `sign_sys_seal_info`;

CREATE TABLE `sign_sys_seal_info` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `picture_user_type` CHAR(6) NOT NULL COMMENT '所属用户类型：01 单位，02 个人',
  `enterprise_or_personal_id` BIGINT(20) DEFAULT NULL COMMENT '企业用户或个人用户ID',
  `picture_type` CHAR(6) DEFAULT '01' COMMENT '章模类型：01 ukey印章，02云签名，03 云印章，04ukey签名',
  `picture_business_type` CHAR(6) DEFAULT '01' COMMENT '章模业务类型：01 行政章、02 财务章等 03 业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章',
  `picture_name` VARCHAR(200) NOT NULL COMMENT '章模名称（如：电子行政章、电子行政章钢印）',
  `picture_code` VARCHAR(20) NOT NULL COMMENT '章模编码（电子印章唯一标识）',
  `picture_pattern` CHAR(10) DEFAULT NULL COMMENT '章模图片类型',
  `picture_width` VARCHAR(20) DEFAULT NULL COMMENT '章模图片宽度（mm）',
  `picture_height` VARCHAR(20) DEFAULT NULL COMMENT '章模图片高度（mm）',
  `picture_data` LONGTEXT COMMENT '章模图片数据（ODC-数字信封 IYIN-二压码Base64）',
  `picture_data64` LONGTEXT COMMENT '章模图片数据（Base64）',
  `picture_digest` VARCHAR(300) DEFAULT NULL COMMENT '章模图片摘要',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` TINYINT(4) UNSIGNED DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  `picture_origin` VARCHAR(20) DEFAULT NULL COMMENT '章模来源',
  `picture_flag` VARCHAR(20) DEFAULT NULL COMMENT '章模来源标记',
  `picture_path` VARCHAR(500) DEFAULT NULL COMMENT '章模图片路径',
  `picture_status` CHAR(2) DEFAULT '01' COMMENT '章模状态：01正常，02禁用',
  `certificate_source` CHAR(6) DEFAULT NULL COMMENT '证书来源（ 01 单位证书库关联  02 在线软证书 03 本地生成证书）',
  `certificate_id` VARCHAR(64) DEFAULT NULL COMMENT '证书ID（证书信息表主键）',
  `medium_type` CHAR(6) DEFAULT NULL COMMENT '介质类型 01：云印章 02 ukey印章',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_picture_code` (`picture_code`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='电子印章章模基础信息表';

/*Data for the table `sign_sys_seal_info` */

/*Table structure for table `sign_sys_seal_picture_data_tmp` */

DROP TABLE IF EXISTS `sign_sys_seal_picture_data_tmp`;

CREATE TABLE `sign_sys_seal_picture_data_tmp` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `picture_data` LONGTEXT NOT NULL COMMENT '章模图片数据（ODC-数字信封 IYIN-二压码Base64）',
  `picture_data64` LONGTEXT COMMENT '章模图片数据（Base64）',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  `is_deleted` TINYINT(4) UNSIGNED DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='电子印章章模图片数据临时表';

/*Data for the table `sign_sys_seal_picture_data_tmp` */

/*Table structure for table `sign_sys_seal_user` */

DROP TABLE IF EXISTS `sign_sys_seal_user`;

CREATE TABLE `sign_sys_seal_user` (
  `id` VARCHAR(64) NOT NULL COMMENT '自增主键',
  `seal_id` VARCHAR(64) NOT NULL COMMENT '印章ID(印章表主键)',
  `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID(用户表主键)',
  `is_deleted` TINYINT(4) UNSIGNED DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `uk_seal_id` (`seal_id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='印章权限分配表';

/*Data for the table `sign_sys_seal_user` */


/*Table structure for table `sign_sys_signature_log` */

DROP TABLE IF EXISTS `sign_sys_signature_log`;

CREATE TABLE `sign_sys_signature_log` (
  `id` VARCHAR(64) NOT NULL COMMENT 'snowflake',
  `app_id` VARCHAR(64) DEFAULT NULL COMMENT '应用ID',
  `seal_name` VARCHAR(50) DEFAULT NULL COMMENT '印章名称',
  `seal_code` VARCHAR(50) DEFAULT NULL COMMENT '印章编码',
  `seal_type` CHAR(2) DEFAULT NULL COMMENT '章模类型：行政章，人事章',
  `medium_type` VARCHAR(50) DEFAULT NULL COMMENT '介质类型：01 云印章，02 ukey印章',
  `file_name` VARCHAR(50) DEFAULT NULL COMMENT '文件名称',
  `file_code` VARCHAR(64) DEFAULT NULL COMMENT '文件编码',
  `signature_type` CHAR(2) NOT NULL COMMENT '签章方案(01:平台签，02:快捷签)',
  `signature_model` CHAR(2) NOT NULL COMMENT '签章模式(01:关键字签章，02:坐标签章)',
  `signature_name` CHAR(2) NOT NULL COMMENT '签章方式(01:单页签章，02:多页签章，03:骑缝签章)',
  `signature_result` CHAR(2) NOT NULL DEFAULT '1' COMMENT '签章结果(01:成功,02:失败)',
  `user_id` VARCHAR(64) NOT NULL COMMENT '用户信息',
  `sign_file_hash` VARCHAR(64) DEFAULT NULL COMMENT '签后文档HASH',
  `sign_file_code` VARCHAR(64) DEFAULT NULL COMMENT '签后文档编码',
  `page` BIGINT(10) NOT NULL DEFAULT '0' COMMENT '文件页数',
  `multi_param` LONGTEXT NOT NULL COMMENT '签章参数',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否，1:是)',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` VARCHAR(50) NOT NULL COMMENT '创建者',
  `update_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` VARCHAR(50) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='签章记录表';

/*Data for the table `sign_sys_signature_log` */

/*Table structure for table `sign_sys_user_info` */

DROP TABLE IF EXISTS `sign_sys_user_info`;

CREATE TABLE `sign_sys_user_info` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `login_type` CHAR(2) DEFAULT NULL COMMENT '账户登录类型(01:手机号登录  02：邮箱 )',
  `user_name` VARCHAR(100) NOT NULL COMMENT '用户名(手机号或邮箱)',
  `user_nick_name` VARCHAR(100) DEFAULT NULL COMMENT '账户号(如 张三 、李四)',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `invalid_auth_times` INT(11) UNSIGNED DEFAULT NULL COMMENT '密码校验错误次数',
  `first_create` CHAR(10) DEFAULT '01' COMMENT '是否为企业或个人下第一个创建的账号（01：是；02：否）',
  `user_type` CHAR(2) NOT NULL COMMENT '用户类型（01：企业用户；02：个人用户；）',
  `enterprise_or_personal_id` VARCHAR(64) NOT NULL COMMENT '用户ID（个人用户或企业用户信息表主键）',
  `node_id` VARCHAR(64) DEFAULT NULL COMMENT '组织结构ID（组织结构表主键）',
  `is_locked` TINYINT(4) DEFAULT '0' COMMENT '是否被锁定（0：未锁定；1：锁定，密码错误一定次数会被锁定）',
  `is_forbid` TINYINT(4) UNSIGNED DEFAULT '0' COMMENT '账户号是否被禁用（0：未禁用；1：禁用）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `source` TINYINT(4) DEFAULT NULL COMMENT '来源(0-注册创建  1-管理员创建)',
  `is_deleted` TINYINT(4) UNSIGNED DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除）',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE COMMENT '普通索引用户名'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='签章系统用户表';

/*Data for the table `sign_sys_user_info` */

INSERT  INTO `sign_sys_user_info`(`id`,`login_type`,`user_name`,`user_nick_name`,`password`,`invalid_auth_times`,`first_create`,`user_type`,`enterprise_or_personal_id`,`node_id`,`is_locked`,`is_forbid`,`remark`,`source`,`is_deleted`,`gmt_create`,`gmt_modified`) VALUES ('591606514456723456','01','13798374338','北极熊','$2a$10$mfmi5fWLqf.g41FRBqavIOmzJCdIbs0Pm1UuqGYNa.dIsKcQCmSvu',5,'01','01','591635506115117056','591606514456723458',0,0,NULL,NULL,0,'2019-06-21 12:33:05',NULL);
