
USE `asign_system`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sign_sys_compact_field
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_compact_field`;
CREATE TABLE `sign_sys_compact_field`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'snowflake',
  `compact_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同Id（sign_sys_compact_info表中Id）',
  `compact_file_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同文件Id（sign_sys_file_resource表中file_code）',
  `signatory_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签署人Id（sign_sys_compact_signatory表中Id）',
  `sign_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签署人名称',
  `signature_method` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签章方式(01：单页签章；02：多页签章；03：骑缝签章；)',
  `signature_start_page` int(11) NOT NULL COMMENT '签章页或签章初始页',
  `signature_end_page` int(11) NULL DEFAULT NULL COMMENT '签章结束页',
  `cover_page_num` int(10) NULL DEFAULT NULL COMMENT '骑缝签时每枚章的覆盖页数',
  `signature_coordinate_x` decimal(10, 4) NULL DEFAULT NULL COMMENT '签章坐标X轴',
  `signature_coordinate_y` decimal(10, 4) NULL DEFAULT NULL COMMENT '签章坐标Y轴',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creat_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `modify_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同签名域表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sign_sys_compact_file
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_compact_file`;
CREATE TABLE `sign_sys_compact_file`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'snowflake',
  `compact_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同Id（sign_sys_compact_info表中Id）',
  `file_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源文件编码（sign_sys_file_resource表中file_code）',
  `file_type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同文档类型（01：合同文件；02：合同附件；）',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除（0：未删除；1：删除）',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creat_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `modify_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `page_total` int(11) NULL DEFAULT NULL COMMENT '页总数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id`) USING BTREE,
  INDEX `idx_contract_id`(`compact_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同关联文件资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sign_sys_compact_info
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_compact_info`;
CREATE TABLE `sign_sys_compact_info`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'snowflake',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户Id（sign_sys_user_info表中Id）',
  `template_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同模板Id（sign_sys_compact_template表中Id）',
  `compact_theme` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同主题',
  `validity_start_date` datetime(0) NULL DEFAULT NULL COMMENT '合同有效期开始时间',
  `validity_end_date` datetime(0) NULL DEFAULT NULL COMMENT '合同有效期结束时间',
  `sign_deadline` datetime(0) NULL DEFAULT NULL COMMENT '签署截止日期',
  `remake` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `compact_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '01' COMMENT '合同状态（01：草稿；02：已撤销；03：已拒签；04：签署中；05：签署完成；06:已过期）',
  `compact_start_date` datetime(0) NULL DEFAULT NULL COMMENT '合同发起时间',
  `compact_end_date` datetime(0) NULL DEFAULT NULL COMMENT '合同完成时间',
  `compact_refuse_date` datetime(0) NULL DEFAULT NULL COMMENT '合同拒签时间',
  `compact_revocation_date` datetime(0) NULL DEFAULT NULL COMMENT '合同撤销时间',
  `sign_way` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签署方式（01：无序签；02：顺序签署；03：每人单独签署；）',
  `signatory_way` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签署人方式（01：我需要签署；02：需要其他人签署）',
  `is_site_sign` tinyint(4) NULL DEFAULT NULL COMMENT '是否指定签署位置（0：未指定签署位置；1：指定签署位置；）',
  `package_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同包路径',
  `revocation_remake` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '撤销原因',
  `refuse_sign_remake` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拒签原因',
  `is_deleted` tinyint(4) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除（0：未删除；1：删除）',
  `org_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属组织Id',
  `folder_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归档文件夹Id',
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型（01:文件管理，02:合同）',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creat_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `modify_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sign_sys_compact_signatory
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_compact_signatory`;
CREATE TABLE `sign_sys_compact_signatory`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'snowflake',
  `compact_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同Id（sign_sys_compact_info表中Id）',
  `signatory_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签署人Id（sign_sys_user_info表中Id）',
  `next_signatory_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下一个签署人Id（sign_sys_user_info表中Id）',
  `sign_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签署状态（01：待我签署；02：待他人签；03：签署通过；04签署不通过）',
  `sign_contact` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签署人的联系方式(手机/邮箱)',
  `sign_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签署人名称',
  `serial_number` int(11) NOT NULL DEFAULT 0 COMMENT '序号',
  `is_key_signature` tinyint(4) NULL DEFAULT 0 COMMENT '签署人是否进行UKey签章（0：否；1：是）',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除（0：未删除；1：删除；）',
  `turn_sign_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否已转签（0：未转签，1：已转签)',
  `turn_to_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转签到谁（当前表的id）',
  `turn_date` datetime(0) NULL DEFAULT NULL COMMENT '转签时间',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creat_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `modify_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `sign_date` datetime(0) NULL DEFAULT NULL COMMENT '签署时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id`) USING BTREE,
  INDEX `idx_compact_id`(`compact_id`) USING BTREE,
  INDEX `idx_signtory_id`(`signatory_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同签署人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sign_sys_compact_copy
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_compact_copy`;
CREATE TABLE `sign_sys_compact_copy`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'snowflake',
  `compact_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同Id（sign_sys_compact_info表中Id）',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抄送人用户Id（sign_sys_user_info表中id）',
  `sign_contact` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签署人的联系方式(手机/邮箱)',
  `sign_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签署人名称',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creat_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `modify_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id`) USING BTREE,
  INDEX `index_compact_id`(`compact_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同抄送人表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
