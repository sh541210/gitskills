USE `asign_system`;
ALTER TABLE `asign_system`.`sign_sys_template` ADD COLUMN `create_user` VARCHAR(100) NULL COMMENT '添加用户' AFTER `gmt_modified`, ADD COLUMN `update_user` VARCHAR(100) NULL COMMENT '修改用户' AFTER `create_user`;
ALTER TABLE `asign_system`.`sign_sys_template` ADD COLUMN `temp_type` VARCHAR(50) NULL COMMENT '模板类型' AFTER `temp_html`;