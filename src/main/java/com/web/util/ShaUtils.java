package com.web.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtils {
	public static final String KEY_SHA = "SHA";
	
	public static String encrypt(String password){
		BigInteger sha = null;
		byte[] byteDate = password.getBytes();
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(KEY_SHA);
			md.update(byteDate);
			sha = new BigInteger(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		return sha.toString(32);
	}
}
