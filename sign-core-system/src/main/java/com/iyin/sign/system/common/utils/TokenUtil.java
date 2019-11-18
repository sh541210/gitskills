package com.iyin.sign.system.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
	* @Description token生成
	* @Author: wdf
    * @CreateDate: 2019/3/8 10:35
	* @UpdateUser: wdf
    * @UpdateDate: 2019/3/8 10:35
	* @Version: 0.0.1
    * @param
    * @return
    */
public class TokenUtil {
    private TokenUtil() {
        throw new IllegalStateException("TokenUtil class");
    }
    private static final String UTFCODE="UTF-8";
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    public static String getOAuthToken(String secretId, String applicationId) throws UnsupportedEncodingException {
        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        map.put("SecretId", secretId);
        map.put("ApplicationId", applicationId);
        map.put("Timestamp", timestamp);
        map.put("Nonce", UUID.randomUUID().toString());
        return getToken(map);
    }

    private static String getToken(Map<String, Object> param) throws UnsupportedEncodingException {
        String paramString = getParamsOrderByKey(param);
        String encode = new String(new Base64().encode(paramString.getBytes(UTFCODE)));
        return URLEncoder.encode(encode, UTFCODE);
    }

    /**
     * 按照key排序得到参数列表字符串
     *
     * @param paramValues 参数map对象
     * @return 参数列表字符串
     */
    private static String getParamsOrderByKey(Map<String, Object> paramValues) {
        StringBuilder params = new StringBuilder();
        List<String> paramNames = new ArrayList<>(paramValues.size());
        paramNames.addAll(paramValues.keySet());
        Collections.sort(paramNames);
        for (String paramName : paramNames) {
            if(params.length()==0){
                params.append(paramName).append("=").append(paramValues.get(paramName));
            } else {
                params.append("&").append(paramName).append("=").append(paramValues.get(paramName));
            }
        }
        return params.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getOAuthToken(UUIDUtil.getFormatUUID(),"AnYinKeJi"));
        System.out.println(getOAuthToken("00-00-00-00-00-00-00-E0","AnYinKeJi"));
    }

}
