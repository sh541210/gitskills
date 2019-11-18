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

 Date: 22/07/2019 17:22:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sign_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sign_sys_menu`;
CREATE TABLE `sign_sys_menu`  (
  `menu_id` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点ID',
  `parent_id` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级节点ID',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `permission` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点权限标识',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端地址',
  `icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `component` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端组件',
  `sort` int(10) NULL DEFAULT 1 COMMENT '排序值',
  `keep_alive` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '路由缓冲 0-开启，1- 关闭',
  `type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点类型 （0菜单 1按钮）',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '节点权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sign_sys_menu
-- ----------------------------
INSERT INTO `sign_sys_menu` VALUES ('602905797478842368', '-1', '后台管理', NULL, NULL, 'iconfont icon-houtaiguanli', NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602906386031968256', '602905797478842368', '单位管理', NULL, '/backManage/unitManage/unitManage', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602906517426929664', '602905797478842368', '我的账号', NULL, '/backManage/myAccount', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 16:58:47', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602907525649530880', '-1', '文档中心', NULL, NULL, 'iconfont icon-wendangzhongxin1', NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602907646067998720', '602907525649530880', '文档管理', NULL, '/fileCenter/fileManage', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 17:00:14', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602911687036633088', '-1', '系统对接', NULL, NULL, 'iconfont icon-xitongduijie1', NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602911879735541760', '602911687036633088', '应用管理', NULL, '/electronContract/myContract', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602912062531698688', '-1', '系统设置', NULL, NULL, 'iconfont icon-xitongpeizhi1', NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602912206350188544', '602912062531698688', '菜单管理', NULL, '/sysSet/menuMgt', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602912283215003648', '602912062531698688', '角色管理', NULL, '/sysSet/roleMgt', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602912355973595136', '602912062531698688', '签章设置', NULL, '/sysSet/signMgt', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602912620541902848', '-1', '签章验证', NULL, NULL, 'iconfont icon-xitongduijie1', NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602912780856590336', '602912620541902848', '电子文档验证', NULL, '/signVertify/elecFileVertify', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');
INSERT INTO `sign_sys_menu` VALUES ('602912863958335488', '602912620541902848', '验证码验证', NULL, '/signVertify/codeVertify', NULL, NULL, 0, '1', '0', '2019-07-22 08:47:20', 'admin', '2019-07-22 08:47:20', 'admin', '0');

SET FOREIGN_KEY_CHECKS = 1;
