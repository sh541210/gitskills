/*用户增加手机号属性*/
ALTER TABLE `asign_system`.`sign_sys_user_info`
ADD COLUMN `phone` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机号' AFTER `user_name`;