package com.snaptechnology.bgonzalez.bookmyroomandroid.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by bgonzalez on 29/08/2016.
 */
public class ListenerSocket {

    private static Socket socket;

    public ListenerSocket(){

    }

    public void listen(){

        try
        {
            //tablet

            int port = 25000;
            InetAddress addr = InetAddress.getByName("localhost");

            SocketAddress sockaddr = new InetSocketAddress(addr, port);
            ServerSocket serverSocket = new ServerSocket(25000,0,addr);
            //ServerSocket serverSocket = new ServerSocket(25000);

            System.out.println("Device Started and listening to the port 25000");

            while(true)
            {
                socket = serverSocket.accept();

                InputStream is = socket.getInputStream();

                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String number = br.readLine();
                System.out.println("Message received from server is "+number);
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
                socket.close();
            }
            catch(Exception e){}
        }
    }
}
