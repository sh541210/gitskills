/*sign_sys_admin_user表增加 单位ID、级别字段 */
ALTER TABLE sign_sys_admin_user ADD enterprise_id VARCHAR(64) DEFAULT NULL COMMENT '所属单位ID' AFTER is_deleted;
ALTER TABLE sign_sys_admin_user ADD power_level CHAR(10) DEFAULT NULL COMMENT '0:超级管理员 01 单位管理员' AFTER enterprise_id;
ALTER TABLE sign_sys_admin_user ADD user_nick_name VARCHAR(64) DEFAULT NULL COMMENT '用户昵称 如：张三' AFTER enterprise_id;
ALTER TABLE sign_sys_seal_info COMMENT '章模类型：01 正规章模图片，02 手写签名'