
/*签章日志添加字段*/
ALTER TABLE `asign_system`.`sign_sys_signature_log`
ADD COLUMN `ip_address` varchar(64) NULL COMMENT '签章ip' AFTER `multi_param`,
ADD COLUMN `mac_address` varchar(64) NULL COMMENT '签章mac' AFTER `ip_address`,
ADD COLUMN `device_name` varchar(255) NULL COMMENT '签章设备' AFTER `mac_address`;

/*合同表添加字段*/
ALTER TABLE `asign_system`.`sign_sys_compact_info`
ADD COLUMN `verification_code` varchar(64) NULL COMMENT '验证码' AFTER `type`,
ADD COLUMN `verification_date` varchar(64) NULL COMMENT '验证码有效期' AFTER `verification_code`,
ADD COLUMN `qr_code` varchar(255) NULL COMMENT '二维码' AFTER `verification_date`;