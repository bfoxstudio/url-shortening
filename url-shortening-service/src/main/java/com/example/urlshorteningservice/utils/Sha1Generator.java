package com.example.urlshorteningservice.utils;

import org.apache.logging.log4j.util.Strings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Generator {

    public static String getHash(String in) {
        if (Strings.isEmpty(in)){
            throw new RuntimeException("empty input for hashing");
        }
        try {
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            byte[] sha1Hash = sha1Digest.digest(in.getBytes());

            StringBuilder hexBuilder = new StringBuilder();
            for (byte b : sha1Hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexBuilder.append("0");
                }
                hexBuilder.append(hex);
            }
            return hexBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("failed to get encryption algorithm", e);
        }
    }
}
