package com.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luwb
 * @date 2020/03/02
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	private final JwtInterceptor jwtInterceptor;

	@Autowired
	public WebMvcConfig(JwtInterceptor jwtInterceptor) {
		this.jwtInterceptor = jwtInterceptor;
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		// 配置不拦截的路径
		List<String> patterns = new ArrayList<>();
		patterns.add("/user/login");
		patterns.add("/user/register");
		registry.addInterceptor(jwtInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(patterns);
	}

}
