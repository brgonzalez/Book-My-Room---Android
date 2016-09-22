package com.snaptechnology.bgonzalez.httpclient;

import org.apache.commons.codec.binary.Base64;

/**
 * The class Encoder is used to encode the location account with the according password
 *
 * @author Brayan González
 * @since 11/09/2016
 */

public class Encoder {

    private String code ;

    /**
     * This method encode the location account with the password to a Authorization Basic code
     * @param displayNameLocation location account
     * @param password
     * @return String Authorization Basic code
     */
    public String encode(String displayNameLocation, String password){
        String authString = displayNameLocation + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        return authStringEnc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
