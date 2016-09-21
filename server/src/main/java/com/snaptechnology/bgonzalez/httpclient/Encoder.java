package com.snaptechnology.bgonzalez.httpclient;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by bgonzalez on 06/09/2016.
 */
public class Encoder {

    private String code ;

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
