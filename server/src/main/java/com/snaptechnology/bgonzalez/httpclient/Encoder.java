package com.snaptechnology.bgonzalez.httpclient;

import com.snaptechnology.bgonzalez.settings.ReaderProperties;
import org.apache.commons.codec.binary.Base64;

/**
 * The class Encoder is used to encode the location account with the according password
 *
 * @author Brayan González
 * @since 11/09/2016
 */
public class Encoder {

    private String code ;

    private String accountX ;

    private String password ;

    public Encoder(){
        ReaderProperties readerProperties = new ReaderProperties();
        accountX = readerProperties.getValue("office365.account.email");
        password= readerProperties.getValue("office365.account.password");
    }

    /**
     * This method encode the location account with the password to a Authorization Basic code
     * @return String Authorization Basic code
     */
    public String encode(){
        String authString =  accountX+":" + password;
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
