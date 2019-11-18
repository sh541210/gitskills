package com.iyin.sign.system.util;

import com.iyin.sign.system.model.code.BaseResponseCode;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.TokenCheckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.*;

/**
 * @program iyin-base-services
 * @description: JWT工具类
 * @author: feiye
 * @create: 2018/06/06 15:18
 */
@Component
public class JWTUtils implements Serializable {

	public static String SESSION_TOKEN_TYPE="01";

	public static String ACCESS_TOKEN_TYPE="02";

	public static String API_TOKEN_TYPE="03";

	@Autowired
	TokenSettings settings;

	/**
	 * 从数据声明生成令牌
	 *
	 * @param claims
	 *            数据声明
	 * @return 令牌
	 */
	private String generateToken(Claims claims) {
		// 添加构成JWT的参数
		// 签发者 Issuer
		// 签发时间 IssuedAt
		// 生效时间 NotBefore
		// 过期时间 Expiration
		// signWith 签名算法以及密匙
		// Audience 接受者
		LocalDateTime currentTime = LocalDateTime.now();
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims)
				.setIssuer(settings.getTokenIssuer())
				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(currentTime.plusMinutes(settings.getTokenExpirationTime())
						.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey());
		// 生成JWT
		return builder.compact();
	}

	/**
	 * 从令牌中获取数据声明
	 *
	 * @param token
	 *            令牌
	 * @return 数据声明
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	/**
	 * 创建session_token
	 * @Author:      陆文雄
	 * @CreateDate:  2018/12/10 0010 下午 7:48
	 * @UpdateUser:  陆文雄
	 * @UpdateDate:  2018/12/10 0010 下午 7:48
	 * @Version:     0.0.1
	 * @param
	 * @return
	 * @throws
	 */
	public String createSessionToken(String userId) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("userId",userId);
		return generateToken(claims);
	}

	/**
	 *
	 * @Author:      陆文雄
	 * @CreateDate:  2018/12/11 0011 上午 9:59
	 * @UpdateUser:  陆文雄
	 * @UpdateDate:  2018/12/11 0011 上午 9:59
	 * @Version:     0.0.1
	 * @param        jsonWebToken
	 * @return
	 * @throws
	 */
	public  Claims parseJWT(String jsonWebToken) throws TokenCheckException {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(settings.getTokenSigningKey()))
					.parseClaimsJws(jsonWebToken).getBody();
			return claims;
		} catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException  e) {
			// 解析token发现token 无效
			throw new TokenCheckException(SignSysResponseCode.SIGN_SYS_ERRORE_1011);
		} catch (ExpiredJwtException expiredEx) {
			// 解析token 发现token到期
			throw new TokenCheckException(SignSysResponseCode.SIGN_SYS_ERRORE_1009);
		}catch (Exception e){
			throw new TokenCheckException(SignSysResponseCode.SIGN_SYS_ERRORE_1011);
		}
	}

	public static void main(String[] args){

		TokenSettings settings = new TokenSettings();
		settings.setTokenExpirationTime(1440);
		settings.setTokenIssuer("iyin-contract");
		settings.setTokenSigningKey("xm8EV6Hy5RMFK4EEACIDAwQusxm8EV6Hy5RMFK4EEACIDAwQusxm8EV6Hy5RMFK4EEACIDAwQusxm8EV6Hy5RMFK4EEACIDAwQus");
//
//		Claims claims = Jwts.claims().setSubject("123456");
//		claims.put("accountId", "123456");
//		claims.put("userType", "01");
//
//
//		LocalDateTime currentTime = LocalDateTime.now();
//		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims)
//				.setIssuer(settings.getTokenIssuer())
//				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
//				.setExpiration(Date.from(currentTime.plusMinutes(settings.getTokenExpirationTime())
//						.atZone(ZoneId.systemDefault()).toInstant()))
//				// .setNotBefore(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
//				.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey());
//		// 生成JWT
//		String token = builder.compact();
//
//		System.out.println("token="+token);

		String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0NTYiLCJhY2NvdW50SWQiOiIxMjM0NTYiLCJ1c2VyVHlwZSI6IjAxIiwiaXNzIjoiaXlpbi1jb250cmFjdCIsImlhdCI6MTU1MDEzMjMyMSwiZXhwIjoxNTUwMjE4NzIxfQ.UAmkBTOYskUxQCQ-XWYcDlvAlp5ahJOn4yYzlgrQeDkwrAU0CEQ3fCY7CaDrMaTcvDdJg5cpRH0KGQNX3ujllg";

		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(settings.getTokenSigningKey()))
				.parseClaimsJws(token).getBody();
		System.out.println("claims="+claims);
	}
}
