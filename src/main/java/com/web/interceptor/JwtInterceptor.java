package com.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author luwb
 * @date 2020/03/02
 */
//@Component
public class JwtInterceptor extends HandlerInterceptorAdapter{

	private static final String ROLES = "roles";
	private static final String ROLES_ADMIN = "admin";
	private static final String RROLES_USER = "user";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		return true;
	}

}
