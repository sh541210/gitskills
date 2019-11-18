USE `asign_system`;
/* 保留数据 `sign_sys_sign_config` */
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE `sign_sys_application`;
TRUNCATE `sign_sys_file_down`;
TRUNCATE `sign_sys_file_print`;
TRUNCATE `sign_sys_file_resource`;
TRUNCATE `sign_sys_template`;
TRUNCATE `sign_sys_print_auth_user`;

TRUNCATE `sign_sys_seal_info`;
TRUNCATE `sign_sys_seal_picture_data_tmp`;
TRUNCATE `sign_sys_seal_user`;
TRUNCATE `sign_sys_cert_info`;

TRUNCATE `sign_sys_enterprise_info`;
TRUNCATE `sign_sys_menu`;
TRUNCATE `sign_sys_node_info`;
TRUNCATE `sign_sys_personal_info`;
TRUNCATE `sign_sys_role`;
TRUNCATE `sign_sys_role_menu`;
TRUNCATE `sign_sys_uk_cert_info`;
TRUNCATE `sign_sys_user_data_limit`;
TRUNCATE `sign_sys_user_info`;
TRUNCATE `sign_sys_user_role`;
TRUNCATE `sign_sys_admin_user`;

TRUNCATE `sign_sys_compact_copy`;
TRUNCATE `sign_sys_compact_field`;
TRUNCATE `sign_sys_compact_file`;
TRUNCATE `sign_sys_compact_info`;
TRUNCATE `sign_sys_compact_log`;
TRUNCATE `sign_sys_compact_signatory`;

TRUNCATE `sign_sys_signature_log`;
TRUNCATE `sign_sys_template_field`;

INSERT INTO `asign_system`.`sign_sys_admin_user` (`id`, `user_name`, `password`, `create_date`, `create_user`, `update_date`, `remark`, `update_user`, `is_deleted`, `enterprise_id`, `user_nick_name`, `power_level`) VALUES ('1', 'admin', '$2a$10$7NNuL1erKpX9.Hq1e09kQ.Jl3AKBMBI/h6JjqTLzxpbGWqpS1fRR.', '2019-06-20 15:16:50', 'admin', '2019-08-09 15:17:32', '15361874015', 'admin', '0', '599265091245834240', '超级管理员', '0');
INSERT INTO `asign_system`.`sign_sys_enterprise_info` (`id`, `chinese_name`, `minority_name`, `english_name`, `credit_code`, `license_code`, `organization_code`, `undeclared_code`, `area_code`, `status`, `type`, `nationality`, `province`, `city`, `area`, `phone`, `address`, `startup_date`, `legal_name`, `legal_telephone`, `legal_certificate_type`, `legal_certificate_no`, `legal_address`, `details_info`, `organization_logo_path`, `remark`, `is_deleted`, `gmt_create`, `gmt_modified`, `certification_levels`, `unit_type`, `business_end_time`, `business_scope`, `industry`, `company_size`, `publish_certification_time`, `registration_company`, `business_start_time`, `capital_type`, `capital`, `company_website_url`, `company_email`, `fax`, `real_name_authentication_flag`, `real_name_authentication_time`, `real_name_authentication_from_biz_system_code`, `business_license_pic`, `legal_person_identity_card_front_pic`, `legal_person_identity_card_back_pic`, `ext_define`, `node_id`) VALUES ('599265091245834240', '深圳市安印科技有限公司', NULL, NULL, '421083198805414268', NULL, NULL, NULL, NULL, NULL, NULL, 'CN', NULL, NULL, NULL, NULL, NULL, NULL, '杨瑞芳', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '2019-07-12 15:50:48', '2019-07-24 11:27:44', NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, '待定', '599266417723834368');
INSERT INTO `asign_system`.`sign_sys_node_info` (`id`, `parent_node_id`, `node_name`, `remark`, `is_deleted`, `gmt_create`, `gmt_modified`) VALUES ('599266417723834368', '0', '深圳市安印科技有限公司', NULL, '0', '2019-07-12 15:50:48', NULL);

SET FOREIGN_KEY_CHECKS = 1;


