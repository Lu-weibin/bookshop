package com.web.interceptor;

import com.web.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author luwb
 * @date 2020/03/02
 */
//@Component
public class JwtInterceptor extends HandlerInterceptorAdapter{

	private final JwtUtil jwtUtil;

	@Autowired
	public JwtInterceptor(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	private static final String ROLES = "roles";
	private static final String ROLES_ADMIN = "admin";
	private static final String RROLES_USER = "user";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader("bookshop_token");
		if (token != null) {
			token = token.substring(7);
			if (!"undefined".equals(token)) {
				Claims claims = jwtUtil.parseJwt(token);
				if (claims!=null) {
					if (ROLES_ADMIN.equals(claims.get(ROLES))) {
						request.setAttribute("admin_claims", claims);
					}
					if (RROLES_USER.equals(claims.get(ROLES))) {
						request.setAttribute("user_claims", claims);
					}
				}
			}
		}
		return true;
	}

}
