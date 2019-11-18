USE `asign_system`;
ALTER TABLE `asign_system`.`sign_sys_user_info` ADD COLUMN `other_id` VARCHAR(100) NULL COMMENT '白鹤第三方用户ID' AFTER `gmt_modified`;
