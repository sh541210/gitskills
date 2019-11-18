/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : 192.168.51.218:6059
 Source Schema         : asign_system

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 16/08/2019 16:27:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 签署位置表增加类型字段
-- ----------------------------
ALTER TABLE `asign_system`.`sign_sys_compact_field`
ADD COLUMN `sign_type` char(2) NOT NULL DEFAULT '01' COMMENT '位置类型(01:印章；02:签名；03:日期)' AFTER `sign_name`;

-- ----------------------------
-- 修改字段
-- ----------------------------
ALTER TABLE `asign_system`.`sign_sys_compact_info`
CHANGE COLUMN `remake` `remark` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `sign_deadline`;
ALTER TABLE `asign_system`.`sign_sys_compact_copy`
MODIFY COLUMN `sign_contact` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签署人的联系方式(手机/邮箱)' AFTER `user_id`;
ALTER TABLE `asign_system`.`sign_sys_compact_signatory`
MODIFY COLUMN `sign_contact` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签署人的联系方式(手机/邮箱)' AFTER `sign_status`;
ALTER TABLE `asign_system`.`sign_sys_compact_info`
MODIFY COLUMN `revocation_remake` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '撤销原因' AFTER `package_path`;

-- ----------------------------
-- 增加文件名字段
-- ----------------------------
ALTER TABLE `asign_system`.`sign_sys_compact_file`
ADD COLUMN `file_name` varchar(64) NULL COMMENT '文件名' AFTER `file_code`;

ALTER TABLE `asign_system`.`sign_sys_compact_info`
ADD COLUMN `print_num` varchar(100) NULL COMMENT '分配的打印次数' AFTER `folder_id`;

-- ----------------------------
-- 增加文件编码字段
-- ----------------------------
ALTER TABLE `asign_system`.`sign_sys_compact_file`
ADD COLUMN `file_code_origin` varchar(128) NULL COMMENT '资源文件编码（sign_sys_file_resource表中file_code）拒签恢复用' AFTER `file_code`;

-- ----------------------------
-- Table structure for sign_sys_compact_log
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_compact_log`;
CREATE TABLE `sign_sys_compact_log`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作用户ID（sign_sys_user_info表主键）',
  `compact_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同Id（sign_sys_compact_info表中Id）',
  `file_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源文件编码（sign_sys_file_resource表中file_code）',
  `type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型 （0查看 1签署 2打印 3下载 4拒签）',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同文件操作记录' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
