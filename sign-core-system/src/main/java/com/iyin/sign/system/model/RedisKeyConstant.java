package com.iyin.sign.system.model;

/**
 * @ClassName: RedisKeyConstant
 * @Description: 返回key常量
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 18:09
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 18:09
 * @Version: 0.0.1
 */
public class RedisKeyConstant {

    private RedisKeyConstant(){}

    /**
     *  登录密码错误次数redis key前缀 + accountId
     */
    public static final String PWORD_INPUT_ERROR_PRE = "password_input_error_";

    public static final String USER_SESSION_TOKEN_PRE="user_sesson_token_";

    /**
     * 管理员和普通用户的ID不可能重复，设置相同的key
     */
    public static final String ADMIN_USER_SESSION_TOKEN_PRE="user_sesson_token_";

    /**
     * 创建单位信息第一步，缓存单位信息
     */
    public static final String CREATE_ENTERPRISE_STEP_ONE_PRE="create_enterprise_step_one_pre_";

    /**
     * 用户登录验证码缓存可以
     */
    public static final String USER_LOGIN_VALICODE_KEY_PRE="user_login_valicode_key_pre_";

    /**
     * 单位调用短信接口次数缓存key
     */
    public static final String ENTERPRISE_SEND_SHORT_MESSAGE_PRE="enterprise_send_short_message_pre_";


}
