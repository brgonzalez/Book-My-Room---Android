package com.snaptechnology.bgonzalez.com.snaptechnology.bgonzalez.testSocket;

/**
 * Created by bgonzalez on 09/08/2016.
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class Device3
{

    private static Socket deviceSocket;

    public static void main(String[] args)
    {
        try
        {
            //tablet
            Socket deviceSocket;
            InetAddress addr = InetAddress.getByName("10.110.10.125");
            ServerSocket serverSocket = new ServerSocket(6000,0,addr);
            while(true) {
                deviceSocket = serverSocket.accept();
                InputStream is = deviceSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String output = br.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                deviceSocket.close();
            }
            catch(Exception e){}
        }
    }
}