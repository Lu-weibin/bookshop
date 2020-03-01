package com.web;

import com.web.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author luwb
 * @date 2020/02/25
 */
@SpringBootApplication
public class BookShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookShopApplication.class, args);
	}

	@Bean
	public JwtUtil getJwtUtil() {
		return new JwtUtil();
	}
}
