package com.ss.song.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.SecureRandom;

/**
 * AES加密解密工具类
 *
 * @author wang.sheng
 *
 */
public final class AESUtils
{
	private static final int LENGTH = 128;

	static Log log = LogFactory.getLog(AESUtils.class);

	public static void encrypt(File srcFile, File destFile, String password)
	{
		try
		{
			byte[] bytes = FileUtils.readFileToByteArray(srcFile);
			bytes = encrypt(bytes, password);
			FileUtils.writeByteArrayToFile(destFile, bytes);
		}
		catch (Exception e)
		{
			log.error("encrypt failed! srcFile: " + srcFile.getAbsolutePath() + ", destFile: " + destFile.getAbsolutePath(), e);
		}
	}

	public static void decrypt(File srcFile, File destFile, String password)
	{
		try
		{
			byte[] bytes = FileUtils.readFileToByteArray(srcFile);
			bytes = decrypt(bytes, password);
			FileUtils.writeByteArrayToFile(destFile, bytes);
		}
		catch (Exception e)
		{
			log.error("decrypt failed! srcFile: " + srcFile.getAbsolutePath() + ", destFile: " + destFile.getAbsolutePath(), e);
		}
	}

	public static String encrypt(String content, String password)
	{
		try
		{
			byte[] encryptResult = encrypt(content.getBytes("UTF-8"), password);
			return Base64.encodeBase64String(encryptResult);
		}
		catch (Exception e)
		{
			log.error("encrypt failed!", e);
			return null;
		}
	}

	public static String decrypt(String content, String password)
	{
		try
		{
			byte[] decryptResult = decrypt(Base64.decodeBase64(content), password);
			return new String(decryptResult, "UTF-8");
		}
		catch (Exception e)
		{
			log.error("decrypt failed!", e);
			return null;
		}
	}

	public static byte[] encrypt(byte[] content, String password)
	{
		try
		{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			kgen.init(LENGTH, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			return cipher.doFinal(content);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	public static byte[] decrypt(byte[] content, String password)
	{
		try
		{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			kgen.init(LENGTH, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			return cipher.doFinal(content);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
}
