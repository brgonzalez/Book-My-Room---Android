package com.snaptechnology.bgonzalez.httpclient;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by bgonzalez on 06/09/2016.
 */
public class Encoder {

    private String code ;

    public String encode(Account account){
        String authString = account.getAddress() + ":" + account.getPassword();
        System.out.println("auth string: " + authString);
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

    public static void main(String[] args){
        Account a = new Account("bgonzalez@snaptechnology.net", "Brayan", "BrgcBrgc5snap");
        Encoder e = new Encoder();
        System.out.println(e.encode(a));
    }
}
