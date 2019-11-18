USE `asign_system`;
CREATE TABLE `sign_sys_file_down` (
  `id` varchar(100) NOT NULL COMMENT '下载ID',
  `down_user` varchar(100) DEFAULT NULL COMMENT '下载用户ID',
  `user_enterprise` varchar(100) DEFAULT NULL COMMENT '用户所属单位/个人ID',
  `user_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1单位2个人',
  `user_channel` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1后台用户2前台用户',
  `file_code` varchar(100) DEFAULT NULL COMMENT '文件code',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下载时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件下载记录表';

CREATE TABLE `sign_sys_print_auth_user` (
  `id` VARCHAR(100) NOT NULL COMMENT '主键ID',
  `file_code` VARCHAR(100) NOT NULL COMMENT '文件编码',
  `user_id` VARCHAR(100) NOT NULL COMMENT '用户ID',
  `is_foggy` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否雾化 0否 1是',
  `is_grey` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否脱密 0否 1是',
  `print_num` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '打印次数',
  `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` VARCHAR(100) DEFAULT NULL COMMENT '添加用户',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(100) DEFAULT NULL COMMENT '修改用户',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='打印分配表';