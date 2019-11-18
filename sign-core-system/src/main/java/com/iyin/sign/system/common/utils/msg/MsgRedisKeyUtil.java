package com.iyin.sign.system.common.utils.msg;

import com.iyin.sign.system.common.enums.VerificationCodeUseTypeEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.model.exception.BusinessException;
import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: MsgRedisKeyUtil
 * @Description: 根据手机号和验证码用途类型拼接redisKey工具类
 * @Author: 刘志
 * @CreateDate: 2018/9/18 18:53
 * @UpdateUser: 刘志
 * @UpdateDate: 2018/9/18 18:53
 * @Version: 0.0.1
 */
public class MsgRedisKeyUtil {

    private static final String MOBILE_PHONE_REGEX = "^1[\\d]{10}";

    private static final String EMAIL_REGEX = ("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    private static final String PASSW0RD_REGEX = ("^[a-zA-Z0-9]+$");

    /**
     * 密码长度
     */
    private static final int PWD_LENGTH = 8;

    private MsgRedisKeyUtil() {
    }

    /**
     * 根据手机号生成检查获取次数的RedisKey
     *
     * @param phoneNo
     * @param codeUseType
     * @return java.lang.String
     * @Author: 刘志
     * @CreateDate: 2018/9/18 19:20
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/18 19:20
     * @Version: 0.0.1
     */
    public static String getCheckTimeKeyByPhone(String phoneNo, String codeUseType) {
        if (!checkMobilePhoneNo(phoneNo)) {
            throw new BusinessException(ErrorCode.PHONE_ERROR);
        }
        String useType = VerificationCodeUseTypeEnum.getNameByCode(codeUseType);
        if (StringUtils.isEmpty(useType)) {
            throw new BusinessException(ErrorCode.SECURITY_CODE_USE_TYPE_ERROR);
        }
        return phoneNo + useType;
    }

    /**
     * 根据手机号生成获取验证码的RedisKey
     *
     * @param phoneNo
     * @param codeUseType
     * @return java.lang.String
     * @Author: 刘志
     * @CreateDate: 2018/9/18 19:20
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/18 19:20
     * @Version: 0.0.1
     */
    public static String getVerificationCodeKeyByPhone(String phoneNo, String codeUseType) {
        if (!checkMobilePhoneNo(phoneNo)) {
            throw new BusinessException(ErrorCode.PHONE_ERROR);
        }
        String useType = VerificationCodeUseTypeEnum.getNameByCode(codeUseType);
        if (StringUtils.isEmpty(useType)) {
            throw new BusinessException(ErrorCode.SECURITY_CODE_USE_TYPE_ERROR);
        }
        return useType + phoneNo;
    }

    /**
     * 根据邮箱生成检查获取次数的RedisKey
     *
     * @param email
     * @param codeUseType
     * @return java.lang.String
     * @Author: 刘志
     * @CreateDate: 2018/9/18 19:20
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/18 19:20
     * @Version: 0.0.1
     */
    public static String getCheckTimeKeyByEmail(String email, String codeUseType) {
        if (!checkEmailAddress(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ERROR);
        }
        String useType = VerificationCodeUseTypeEnum.getNameByCode(codeUseType);
        if (StringUtils.isEmpty(useType)) {
            throw new BusinessException(ErrorCode.SECURITY_CODE_USE_TYPE_ERROR);
        }
        return email + useType;
    }

    /**
     * 根据邮箱生成获取验证码的RedisKey
     *
     * @param email
     * @param codeUseType
     * @return java.lang.String
     * @Author: 刘志
     * @CreateDate: 2018/9/18 19:20
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/18 19:20
     * @Version: 0.0.1
     */
    public static String getVerificationCodeKeyByEmail(String email, String codeUseType) {
        if (!checkEmailAddress(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ERROR);
        }
        String useType = VerificationCodeUseTypeEnum.getNameByCode(codeUseType);
        if (StringUtils.isEmpty(useType)) {
            throw new BusinessException(ErrorCode.SECURITY_CODE_USE_TYPE_ERROR);
        }
        return useType + email;
    }


    /**
     * 校验手机号
     *
     * @param phoneNo
     * @return boolean
     * @Author: 刘志
     * @CreateDate: 2018/9/18 19:20
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/18 19:20
     * @Version: 0.0.1
     */
    public static boolean checkMobilePhoneNo(String phoneNo) {
        boolean empty = StringUtils.isEmpty(phoneNo);
        boolean b1 = !(phoneNo.matches(MOBILE_PHONE_REGEX));
        return !(empty || b1);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return boolean
     * @Author: 刘志
     * @CreateDate: 2018/9/18 19:20
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/18 19:20
     * @Version: 0.0.1
     */
    public static boolean checkEmailAddress(String email) {
        boolean flag = StringUtils.isEmpty(email) || !(email.matches(EMAIL_REGEX));
        return !flag;
    }

    /**
     * 校验密码格式
     *
     * @param pwd
     * @return boolean
     * @Author: 刘志
     * @CreateDate: 2018/9/18 19:20
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/18 19:20
     * @Version: 0.0.1
     */
    public static void checkPasswordStyle(String pwd) {

        if (StringUtils.isEmpty(pwd) || !pwd.matches(PASSW0RD_REGEX)) {
            throw new BusinessException(ErrorCode.PWD_STYLE_ERROR);
        }
        if (pwd.length() < PWD_LENGTH) {
            throw new BusinessException(ErrorCode.PWD_LENGTH_ERROR);
        }
    }

}
