package com.web.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author luwb
 * @date 2020/03/02
 * 工具类
 */
public class CommonUtil {

	/**
	 * 生成订单号: 时间 + 随机6位数
	 */
	public static String getOrderNumber(){
		int time = 6;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate = sdf.format(new Date());
		String result = "";
		Random random = new Random();
		for (int i = 0; i < time; i++) {
			result += random.nextInt(10);
		}
		return newDate + result;
	}

	/**
	 * 生成32位字符
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid.toUpperCase();
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || "".equals(string);
	}

	public static String formatDate(Date date) {
		return formatDate(date, "yyyyMMdd");
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

}
