/*系统设置信息*/
CREATE TABLE `sign_sys_default_config` (
  `id` varchar(64) NOT NULL COMMENT '自增主键',
  `sys_name` varchar(64) NOT NULL COMMENT '系统名称',
  `time_stamp` varchar(64) DEFAULT NULL COMMENT '时间戳服务器地址',
  `api_token` varchar(64) DEFAULT NULL COMMENT '安印云签API令牌',
  `logo_url` varchar(200) DEFAULT NULL COMMENT '系统logo绝对路径',
  `is_deleted` tinyint(4) unsigned DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统设置信息';
