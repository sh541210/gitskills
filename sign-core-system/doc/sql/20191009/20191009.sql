USE `asign_system`;

CREATE TABLE `sign_sys_use_seal` (
  `apply_id` varchar(100) NOT NULL COMMENT '申请单据id',
  `apply_code` varchar(100) NOT NULL COMMENT '申请单据编号',
  `file_code` varchar(5000) DEFAULT NULL COMMENT '文件code列表，多个逗号分隔',
  `mac` varchar(100) NOT NULL COMMENT '印章设备地址',
  `apply_count` int(10) NOT NULL COMMENT '申请次数',
  `expire_time` datetime NOT NULL COMMENT '失效时间',
  `source` tinyint(4) NOT NULL COMMENT '来源1合同2文件3用印申请',
  `file_title` varchar(255) DEFAULT NULL COMMENT '文件标题',
  `file_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `postbackaddress` varchar(1000) DEFAULT NULL COMMENT '盖章数据回传地址',
  `seal_name` varchar(100) DEFAULT NULL COMMENT '印章名称',
  `apply_matter` varchar(1000) DEFAULT NULL COMMENT '申请事由',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creat_user` varchar(25) DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `modify_user` varchar(25) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

ALTER TABLE `asign_system`.`sign_sys_file_resource` ADD COLUMN `page_total` BIGINT(20) NULL COMMENT '文件页数' AFTER `print_dis_num`;


