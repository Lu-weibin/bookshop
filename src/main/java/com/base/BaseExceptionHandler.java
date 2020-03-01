package com.base;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author luwb
 * @date 2019/11/22
 * 公共异常处理类
 */
@RestControllerAdvice
public class BaseExceptionHandler {

	@ExceptionHandler(Exception.class)
	public Result result(Exception e) {
		return new Result(false, StatusCode.ERROR, "error", e.toString());
	}
}
