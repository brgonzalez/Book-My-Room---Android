package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * @autor Brayan González
 * @since 16/11/2016.
 */

public class ReaderPropertiesService {

    public String getValue(String value)  {

        Properties props=new Properties();
        InputStream inputStream =
                this.getClass().getClassLoader().getResourceAsStream("application.properties");
        try {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = props.getProperty(value);
        return result;
    }
}
