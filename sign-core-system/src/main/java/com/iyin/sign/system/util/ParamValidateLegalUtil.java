package com.iyin.sign.system.util;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://www.cnblogs.com/go4mi/p/6426215.html
 * @ClassName: PassWordValidateLegalUtil
 * @Description: 密码合法性验证工具类
 * @Author: luwenxiong
 * @CreateDate: 2018/8/9 0009 上午 10:43
 * @UpdateUser: BORUI
 * @UpdateDate: 2018/8/9 0009 上午 10:43
 * @Version: 0.0.1
 */
public class ParamValidateLegalUtil {

    /**
     * 密码允许的特殊字符
     */
    public static final String PASS_SPECIALCHAR = "@#$%^&*()_+-={}[]!^&*=+\\|{};:'\",<>/?";


    /**
     * 正则表达式：验证邮箱
     * /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
     */
    //public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String REGEX_EMAIL = "^(\\w)+@(\\w+.)+(\\w+)";

    /**
     * 账户名允许的特殊字符
     */
    public static final String NAME_SPECIALCHAR  = "@-_.";

    /**
     * 11位数字，以1开头
     */
    public static final String PATTERN = "^1[\\d]{10}";


    public static void main(String[] args){

        String str ="wangkang@q.com.cn";
        //validatePassWord(str);
       // validateLinkPhone("12333344-");
        if(isEmail(str)){
            System.out.println("ok");
        }else {
            System.out.println("no ok");
        }
    }

    /**
     * 验证账户密码的合法性
     * @Author:      陆文雄
     * @CreateDate:  2018/8/9 0009 上午 10:56
     * @UpdateUser:  陆文雄
     * @UpdateDate:  2018/8/9 0009 上午 10:56
     * @Version:     0.0.1
     * @param       pasword
     */
    public static void validatePassWord(String pasword){
        //判定密码长度
        if(StringUtils.isEmpty(pasword) || pasword.length()<6 || pasword.length()>16){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1021);
        }

        boolean flag=true;
        for(int i=0;i<pasword.length();i++){
            if(!isWord(pasword.charAt(i)) && !isSpecialWord(pasword.charAt(i))){
                flag =false;
                break;
            }
        }
        if(!flag){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1022);
        }
    }

    /**
     * 校验某个字符是否是a-z、A-Z、_、0-9
     *
     * @param c
     *  被校验的字符
     * @return true代表符合条件
     */
    public static boolean isWord(char c){
        String regEx = "[\\w]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher("" + c);
        return m.matches();
    }


    /**
     * 校验某个字符是否是0-9数字
     *
     * @param c
     *  被校验的字符
     * @return true代表符合条件
     */
    public static boolean isNumber(char c){
        String regEx = "^\\d+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher("" + c);
        return m.matches();
    }

    /**
     * 校验某个字符是否允许的特殊字符
     * @param c
     *  被校验的字符
     * @return true代表符合条件
     */
    public static boolean isSpecialWord(char c){
        boolean flag= false;
        for(int i=0;i<PASS_SPECIALCHAR.length();i++){
            if(c == PASS_SPECIALCHAR.charAt(i)){
                flag= true;
                break;
            }
        }
        return flag;
    }

    /**
     * 账户名允许的特殊字符
     * @param c
     *  被校验的字符
     * @return true代表符合条件
     */
    public static boolean isAccountNameSpecialWord(char c){
        boolean flag= false;
        for(int i=0;i<NAME_SPECIALCHAR.length();i++){
            if(c == NAME_SPECIALCHAR.charAt(i)){
                flag= true;
                break;
            }
        }
        return flag;
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(PATTERN, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
}
