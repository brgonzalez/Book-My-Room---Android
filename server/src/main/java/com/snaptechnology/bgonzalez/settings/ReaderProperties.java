package com.snaptechnology.bgonzalez.settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
/**
 * @autor bgonzalez
 * @since 16/11/2016.
 */
public class ReaderProperties {
    String result = "";
    InputStream inputStream;

    public String getValue(String value)  {

        try {
            Properties prop = new Properties();
            String propFileName = "application.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            Date time = new Date(System.currentTimeMillis());
            // get the property value and print it out
            result = prop.getProperty(value);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}