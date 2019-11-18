package com.iyin.sign.system.filter;

import com.alibaba.fastjson.JSON;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.RedisKeyConstant;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BaseException;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.AccessLimitService;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.service.VerifyLicense;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.TokenSettings;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @ClassName: AccessFilter
 * @Description: 过滤器类
 * @Author: luwenxiong
 * @CreateDate: 2019/6/22 11:12
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/22 11:12
 * @Version: 0.0.1
 */
@Component
@Slf4j
public class AccessFilter implements Filter {

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    RedisService redisService;

    @Autowired
    TokenSettings settings;

    @Autowired
    IAuthTokenService authTokenService;

    @Autowired
    private VerifyLicense verifyLicense;

    private static String licName = "license.lic";

    @Autowired
    private AccessLimitService accessLimitService;

    /**
     * 不需要验证
     */

    @Value("${fiter.button}")
    private Boolean button;

    @Value("${fiter.skipUrls}")
    private String[] skipUrls;

    /**
     * lic
     */
    @Value("${localFilePath.auth}")
    public String authUrl;

    /**
     * signKey
     */
    @Value("${localFilePath.signKey}")
    private String signKey;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        log.info("#########过滤器初始化成功########");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        String requestUrl = req.getRequestURL().toString();
        log.info("=========={} request to {}", req.getMethod(), req.getRequestURL().toString());
        //设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        rep.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的访问方法
        rep.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        rep.setHeader("Access-Control-Max-Age", "3600");
        rep.setHeader("Access-Control-Allow-Headers", "token,Origin, X-Requested-With, Content-Type, Accept");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        IyinResult filterResult = null;
        /**
         * 是否关闭了session验证
         */
        if (!button) {
            filterChain.doFilter(request, response);
            return;
        }
        for (String skipUrl : skipUrls) {
            if (requestUrl.indexOf(skipUrl) != -1) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        //ck license 如果不需要校验license 注释掉下面这行代码
//        filterResult = ckLicense(req, requestUrl, filterResult);
        //ck token 如果不需要校验license 放开这行代码，注释ck license
        filterResult = ckToken(req, requestUrl, filterResult);
        getFilterResult(request, response, filterChain, filterResult);
    }

    /**
    　　* @description: 返回结果集
    　　* @param request
     　 * @param response
     　 * @param filterChain
     　 * @param filterResult
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/22 11:36
    　　*/
    private void getFilterResult(ServletRequest request, ServletResponse response,
                                 FilterChain filterChain, IyinResult filterResult)throws IOException, ServletException{
        if (filterResult != null) {
            try (OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
                 PrintWriter writer = new PrintWriter(osw, true)){
                String jsonStr = JSON.toJSONString(filterResult);
                writer.write(jsonStr);
                writer.flush();
            }catch (IOException e) {
                log.error("过滤器返回信息失败:" + e.getMessage(), e);
            }
        } else {
            log.info("token filter过滤ok");
            filterChain.doFilter(request, response);
        }
    }

    /**
    	* @Description 校验License
    	* @Author: wdf 
        * @CreateDate: 2019/8/20 17:32
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/20 17:32
    	* @Version: 0.0.1
        * @param req, requestUrl, filterResult
        * @return com.iyin.sign.system.model.IyinResult
        */
    private IyinResult ckLicense(HttpServletRequest req, String requestUrl, IyinResult filterResult) {
        try {
            File file=new File(authUrl+licName);
            if(file.exists()) {
                verifyLicense.ckLicence(authUrl + licName);
                filterResult = ckToken(req, requestUrl, filterResult);
            }else {
                filterResult = IyinResult.getIyinResult(ErrorCode.SERVER_50300);
            }
        }catch (Exception e){
            log.error("检测证书失败"+e.getMessage());
            filterResult = IyinResult.getIyinResult(ErrorCode.SERVER_50300);
        }
        return filterResult;
    }

    private IyinResult ckToken(HttpServletRequest req, String requestUrl, IyinResult filterResult) {
        String sessionToken = req.getHeader("session_token");
        log.info("###{}开始sessionToken验证###", requestUrl);
        log.info("=======sessionToken:{}=========", sessionToken);
        if (!StringUtils.isEmpty(sessionToken)) {
            filterResult = validateSessionToken(sessionToken);
        } else {
            //如果session_token为空，那接口是第三方系统调用，则验证 api_token
            String apiToken = req.getHeader("api_token");
            log.info("=======api_token:{}=========", apiToken);
            if (!StringUtils.isEmpty(apiToken)) {
                try {
                    authTokenService.verifyAuthToken(apiToken);
                } catch (BaseException e) {
                    filterResult = IyinResult.getIyinResult(e.getMessageCode(), e.getMessage());
                }
            } else {
                filterResult = IyinResult.getIyinResult(SignSysResponseCode.SIGN_SYS_ERRORE_1008);
            }

        }


        log.info("###{}sessionToken验证结果:{}###", requestUrl, filterResult == null ? "检验通过" : JSON.toJSONString(filterResult));
        return filterResult;
    }

    @Override
    public void destroy() {
        log.info("#########过滤器销毁###");
    }


    public IyinResult validateSessionToken(String sessionToken) {
        IyinResult iyinResult = null;
        try {
            Claims claims = jwtUtils.parseJWT(sessionToken);
            String userId = String.valueOf(claims.get("userId"));
            String value = redisService.getAndUpdateTime(RedisKeyConstant.USER_SESSION_TOKEN_PRE + userId, settings.getTokenExpirationTime());
            if (null == value) {
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1009);
            }

            if (!sessionToken.equals(value)) {
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1010);
            }
        } catch (BaseException e) {
            iyinResult = IyinResult.getIyinResult(e.getMessageCode(), e.getMessage());

        }
        return iyinResult;

    }
}
