package com.base;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

	@ExceptionHandler(Exception.class)
	public Result result(Exception e) {
		return new Result(false, "error", e.toString());
	}
}
