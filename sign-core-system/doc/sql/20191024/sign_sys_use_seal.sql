/*
 Navicat Premium Data Transfer

 Source Server         : 合同平台开发
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : 192.168.51.218:6059
 Source Schema         : asign_system

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 24/10/2019 09:37:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sign_sys_use_seal
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_use_seal`;
CREATE TABLE `sign_sys_use_seal`  (
  `apply_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '申请单据id',
  `apply_user` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '申请人ID',
  `file_code` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件code列表，多个逗号分隔',
  `seal_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '印章id',
  `apply_count` int(10) NOT NULL COMMENT '申请次数',
  `expire_time` datetime(0) NOT NULL COMMENT '失效时间',
  `source` tinyint(4) NOT NULL COMMENT '来源1合同2文件3用印申请',
  `business_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务ID,如合同id',
  `file_number` int(10) NOT NULL DEFAULT 0 COMMENT '文件份数',
  `file_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '文件类型',
  `apply_pdf` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请文件，限制为PDF，和imgList 参数二选一',
  `img_list` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请文件拍照图片，和applyPdf 参数二选一',
  `seal_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '印章名称',
  `apply_cause` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请事由',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除（0：未删除；1：删除）',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creat_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `modify_user` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`apply_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用印申请表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
