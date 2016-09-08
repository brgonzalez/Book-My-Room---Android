package com.snaptechnology.bgonzalez.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by bgonzalez on 05/09/2016.
 */
public class ReceiverSocket {


    public void listen(){
        Socket mSocket = null;
        String output = "null";
        try
        {
            //tablet
            Socket deviceSocket;
            ServerSocket serverSocket = new ServerSocket(6500);
            while(true) {
                deviceSocket = serverSocket.accept();
                InputStream is = deviceSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                output = br.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
