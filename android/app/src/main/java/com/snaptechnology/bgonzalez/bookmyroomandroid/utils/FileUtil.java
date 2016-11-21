package com.snaptechnology.bgonzalez.bookmyroomandroid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();
    private static String locationFile = "location_file";

    public static String readLocation(Context context) {
        String ret = null;

        try {
            InputStream inputStream = context.openFileInput(locationFile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
            return ret;
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        } catch (NullPointerException e){
            Log.e(TAG, "NullPointerException reading file");
        }
        return "Device is not configured";
    }

    public static void writeLocation(Context context, String data){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(locationFile, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }
}