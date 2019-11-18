ALTER TABLE `asign_system`.`sign_sys_file_resource`
ADD COLUMN `atomization_file_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '雾化处理文件资源路径' AFTER `file_path`;

-- ----------------------------
-- Table structure for sign_sys_file_print
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_file_print`;
CREATE TABLE `sign_sys_file_print`  (
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '打印ID',
  `print_user` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打印用户ID',
  `user_enterprise` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户所属单位/个人ID',
  `user_type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1单位2个人',
  `user_channel` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1后台用户2前台用户',
  `file_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件code',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '打印时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件打印记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;