/*企业表增加node_id字段*/
ALTER TABLE sign_sys_enterprise_info add node_id VARCHAR(64) DEFAULT null COMMENT '节点ID' AFTER ext_define

/* 角色表 */
CREATE TABLE `sign_sys_role` (
  `id` varchar(64) NOT NULL COMMENT '自增主键',
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `role_flag` varchar(64) NOT NULL COMMENT '角色标识',
  `role_desc` varchar(64) NOT NULL COMMENT '角色描述',
  `is_deleted` tinyint(4) unsigned DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `uk_role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';


/*用户角色表*/
CREATE TABLE `sign_sys_user_role` (
  `id` varchar(64) NOT NULL COMMENT '自增主键',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `is_deleted` tinyint(4) unsigned DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

/*用户数据权限范围*/
CREATE TABLE `sign_sys_user_data_limit` (
  `id` varchar(64) NOT NULL COMMENT '自增主键',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `limit_type` varchar(6) NOT NULL COMMENT '0:全部 1:本级 2:本级及子级 3:自定义',
  `node_ids` varchar(64) DEFAULT NULL COMMENT '当为自定义时，显示选取的节点ID, 格式如：232323,2323233,33333',
  `is_deleted` tinyint(4) unsigned DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据权限范围';


/*角色菜单权限表*/
CREATE TABLE `sign_sys_role_menu` (
  `id` varchar(64) NOT NULL COMMENT '自增主键',
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单ID',
  `is_deleted` tinyint(4) unsigned DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单权限表表';

/*ukey中的证书信息表*/


CREATE TABLE `sign_sys_uk_cert_info` (
  `id` VARCHAR(64) NOT NULL COMMENT 'snowflake',
  `oid` varchar(160) DEFAULT NULL COMMENT '数字证书唯一标识',
  `trust_id` varchar(40) DEFAULT NULL COMMENT '数字证书信任服务号',
  `enterprise_id` varchar(64) DEFAULT NULL COMMENT '企业用户ID（用户中心库企业信息表代理主键）',
  `issuer` varchar(40) DEFAULT NULL COMMENT '数字证书颁发机构（CertificateFirmsEnum.java）对应国密的VID电子印章厂商ID',
  `valid_start` datetime DEFAULT NULL COMMENT '数字证书有效期开始时间',
  `valid_end` datetime DEFAULT NULL COMMENT '数字证书有效期结束时间',
  `current_status` char(2) DEFAULT '0' COMMENT '数字证书当前状态（CurrentStatusEnum,java）',
  `remark` longtext COMMENT '备注',
  `is_deleted` tinyint(4) unsigned DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4241372 DEFAULT CHARSET=utf8 COMMENT='UKEY印章证书信息';








