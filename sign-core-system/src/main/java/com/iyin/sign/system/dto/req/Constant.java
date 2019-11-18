package com.iyin.sign.system.dto.req;

import org.springframework.beans.factory.annotation.Value;

/**
	* @Description 常量
	* @Author: wdf 
    * @CreateDate: 2019/7/9 15:27
	* @UpdateUser: wdf
    * @UpdateDate: 2019/7/9 15:27
	* @Version: 0.0.1
    * @param 
    * @return 
    */
public class Constant {

    private Constant(){}

    public static final String ACCESS_TOKEN = "accessToken";

    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 默认的 refresh_token 的有效时长: 30天
     */
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 24 * 30;

    /**
     * 默认的 access_token 的有效时长: 12小时
     */
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 60 * 12;

    /**
     * @Description 应用token详细信息
     * @Author: wdf
     * @CreateDate: 2019/1/20 16:11
     * @UpdateUser: wdf
     * @UpdateDate: 2019/1/20 16:11
     * @Version: 0.0.1
     * @param
     * @return
     */
    public static final String SIGN_APP_TOKEN_INFO="sign_app_token_info:";


    public static final String SIGN_APP_INFO="sign_app_info:";

    public static final String SIGN_REFRESH_APP_INFO="sign_refresh_app_info:";

    /**
    	* @Description 应用token
    	* @Author: wdf
        * @CreateDate: 2019/1/20 16:11
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/1/20 16:11
    	* @Version: 0.0.1
        * @param
        * @return
        */
    public static final String SIGN_APP_TOKEN="sign_app_token:";

    public static final String ISO88591="ISO-8859-1";

    public static final String UTF8 ="UTF-8";

    /**
     * 白鹤 配置
     * @Author: wdf
     * @CreateDate: 2019/10/16 16:05
     * @UpdateUser: wdf
     * @UpdateDate: 2019/10/16 16:05
     * @Version: 0.0.1
     * @param
     * @return
     */
    @Value("${private-baihe.appKey}")
    public static String APP_KEY;

    /**
     * 白鹤 配置
     * @Author: wdf
     * @CreateDate: 2019/10/16 16:05
     * @UpdateUser: wdf
     * @UpdateDate: 2019/10/16 16:05
     * @Version: 0.0.1
     * @param
     * @return
     */
    @Value("${private-baihe.secret}")
    public static String SECRET;

}
