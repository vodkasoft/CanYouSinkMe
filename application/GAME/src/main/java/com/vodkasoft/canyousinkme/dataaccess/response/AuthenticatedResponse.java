package com.vodkasoft.canyousinkme.dataaccess.response;

import com.google.gson.annotations.SerializedName;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public abstract class AuthenticatedResponse {

    private static final String MAC_ALGORITHM = "HmacSHA256";

    private static final char[] hexCharacters = "0123456789abcdef".toCharArray();

    @SerializedName("MAC")
    private String mMessageAuthenticationCode;

    protected AuthenticatedResponse(String messageAuthenticationCode) {
        mMessageAuthenticationCode = messageAuthenticationCode;
    }

    private static String encodeHex(byte[] data) {
        char[] characters = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            int character = data[i] & 0xFF;
            characters[i * 2] = hexCharacters[character >>> 4];
            characters[i * 2 + 1] = hexCharacters[character & 0x0F];
        }
        return new String(characters);
    }

    private static boolean verifyMac(String message, String expectedMac, String key) {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), MAC_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            if (mac == null) {
                return false;
            }
            mac.init(signingKey);
            String resultingMac = encodeHex(mac.doFinal(message.getBytes()));
            return expectedMac.equals(resultingMac);
        } catch (InvalidKeyException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public boolean messageIsValid(String serverResponseKey) {
        return verifyMac(getMessage(), mMessageAuthenticationCode, serverResponseKey);
    }

    protected abstract String getMessage();
}
