package com.github.thomasfischl.eurydome.backend.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Copy from http://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java
 */
public class HashUtil {

  public static String generateMD5(String message) {
    return hashString(message, "MD5");
  }

  public static String generateSHA1(String message) {
    return hashString(message, "SHA-1");
  }

  public static String generateSHA256(String message) {
    return hashString(message, "SHA-256");
  }

  private static String hashString(String message, String algorithm) {
    try {
      MessageDigest digest = MessageDigest.getInstance(algorithm);
      byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

      return convertByteArrayToHexString(hashedBytes);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
      throw new RuntimeException("Could not generate hash from String", ex);
    }
  }

  private static String convertByteArrayToHexString(byte[] arrayBytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < arrayBytes.length; i++) {
      stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    return stringBuffer.toString();
  }
}
