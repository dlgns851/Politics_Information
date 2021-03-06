package com.ssafy.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssafy.vo.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtService {
	
	@Value("${jwt.salt}")
	private String salt;
	
	@Value("${jwt.expmin}")
	private Long expireMin;
	
	
	public String create(final User user)  {
		//log.trace("time: {}", expireMin);
		final JwtBuilder builder = Jwts.builder();
		
		builder.setHeaderParam("typ", "JWT");
		builder.setSubject("로그인 토큰")
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*expireMin))
				.claim("User", user).claim("second","부가정보");
		builder.signWith(SignatureAlgorithm.HS256, salt.getBytes());
		
		final String jwt = builder.compact();
		//log.debug("토큰발행 : {}", jwt);
		return jwt;
	}
	
	public void checkValid(final String jwt) {
		//log.trace("토큰 점검 : {}", jwt);
		
		Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
	}
	
	public Map<String, Object> get(final String jwt) {
		Jws<Claims> claims = null;
		
			claims = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
		
			
		
		
		//log.trace("claims: {}", claims);
		return claims.getBody();
	}
	
}
