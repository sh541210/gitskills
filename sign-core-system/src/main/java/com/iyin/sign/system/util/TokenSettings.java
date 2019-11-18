package com.iyin.sign.system.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: JWTtoken生成配置
 * @Author: 肖泛舟
 * @CreateDate: 2018/9/13 11:51
 * @UpdateUser: 肖泛舟
 * @UpdateDate: 2018/9/13 11:51
 * @Version: 0.0.1
 */
@Configuration
@ConfigurationProperties(prefix = "api.token")
public class TokenSettings {

	/**
	 * 过期时间
	 */
	private Integer tokenExpirationTime;

	/**
	 * 签发者
	 */
	private String tokenIssuer;

	/**
	 * 签名私钥
	 */
	private String tokenSigningKey;

	public Integer getTokenExpirationTime() {
		return tokenExpirationTime;
	}

	public void setTokenExpirationTime(Integer tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}

	public String getTokenIssuer() {
		return tokenIssuer;
	}

	public void setTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	public String getTokenSigningKey() {
		return tokenSigningKey;
	}

	public void setTokenSigningKey(String tokenSigningKey) {
		this.tokenSigningKey = tokenSigningKey;
	}

}
