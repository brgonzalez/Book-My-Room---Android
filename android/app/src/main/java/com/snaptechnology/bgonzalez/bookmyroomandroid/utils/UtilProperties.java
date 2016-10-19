package com.snaptechnology.bgonzalez.bookmyroomandroid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Editable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;




public class UtilProperties {


    private static String fileProperties = "application.properties";
    private static String locationFile = "location_file";


    public static String getLocationProperty(Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(locationFile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (NullPointerException e){
            Log.e("Error","Can not read location file");
            getLocationProperty(context);
        }

        return ret;
    }



    public static void setProperty(Context context, String data){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(locationFile, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void main(String[] args){

    }

}
