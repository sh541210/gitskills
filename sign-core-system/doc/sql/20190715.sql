USE `asign_system`;
/* 文件中心表添加字段 验证码 ，验证有效时间 */
ALTER TABLE `asign_system`.`sign_sys_file_resource` ADD COLUMN `verification_code` VARCHAR(100) NULL COMMENT '验证码' AFTER `down_count`, ADD COLUMN `gmt_verification` DATE NULL COMMENT '验证有效时间' AFTER `verification_code`;
ALTER TABLE `asign_system`.`sign_sys_file_resource` CHANGE `gmt_modified` `gmt_modified` DATETIME DEFAULT NULL COMMENT '签署时间',
ADD COLUMN `gmt_mod` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间' AFTER `gmt_modified`;

DROP TABLE `asign_system`.`sign_sys_template`;
/* 添加模板表 */
CREATE TABLE `sign_sys_template` (
  `id` varchar(100) NOT NULL COMMENT '模板编号',
  `temp_name` varchar(100) DEFAULT NULL COMMENT '模板名称',
  `temp_purpose` varchar(255) DEFAULT NULL COMMENT '模板用途',
  `temp_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '模板状态',
  `temp_html` longtext COMMENT '模板内容',
  `relation_id` varchar(100) DEFAULT NULL COMMENT '外键关联ID',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板表';

/* 添加签章配置设置表 */
CREATE TABLE `sign_sys_sign_config` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `pre_sign` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用预盖章0否1是',
  `verification_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用验证码0否1是',
  `qr_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用二维码0否1是',
  `gmt_verification` int(10) NOT NULL DEFAULT '0' COMMENT '验证失效为0永久有效',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签章配置设置表'

/* 添加签章配置设置表 初始化数据 */
INSERT INTO `asign_system`.`sign_sys_sign_config` (`id`, `pre_sign`, `verification_code`, `qr_code`) VALUES ('1', '1', '1', '1');

/* 修改 验证有效时间 0为永久有效 */
ALTER TABLE `asign_system`.`sign_sys_file_resource` CHANGE `gmt_verification` `gmt_verification` VARCHAR(50) NULL COMMENT '验证有效时间 0为永久有效';
/* add 是否启用二维码0否1是 */
ALTER TABLE `asign_system`.`sign_sys_file_resource` ADD COLUMN `qr_code` TINYINT NULL COMMENT '是否启用二维码0否1是' AFTER `gmt_verification`;