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

 Date: 27/09/2019 10:12:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sign_sys_template_field
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_template_field`;
CREATE TABLE `sign_sys_template_field` (
  `id` varchar(64) NOT NULL COMMENT 'snowflake',
  `template_id` varchar(64) NOT NULL COMMENT '模板Id（sign_sys_template表中Id）',
  `sign_name` varchar(64) NOT NULL COMMENT '名称',
  `sign_type` char(2) NOT NULL DEFAULT '01' COMMENT '类型(01:印章；02:签名；)',
  `signature_method` char(2) NOT NULL COMMENT '签章方式(01：单页签章；02：多页签章；03：骑缝签章；04:连页签章)',
  `signature_start_page` int(11) NOT NULL COMMENT '签章页或签章初始页',
  `signature_end_page` int(11) DEFAULT NULL COMMENT '签章结束页',
  `cover_page_num` int(10) DEFAULT NULL COMMENT '骑缝签时每枚章的覆盖页数',
  `signature_coordinate_x` decimal(10,4) DEFAULT NULL COMMENT '签章坐标X轴',
  `signature_coordinate_y` decimal(10,4) DEFAULT NULL COMMENT '签章坐标Y轴',
  `signature_direction` tinyint(4) DEFAULT NULL COMMENT '签章方向0：表示左 1：表示右',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除（0：未删除；1：删除；）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creat_user` varchar(25) DEFAULT NULL COMMENT '创建者',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `modify_user` varchar(25) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板控件表'


SET FOREIGN_KEY_CHECKS = 1;
