package com.snaptechnology.bgonzalez.com.snaptechnology.bgonzalez.testSocket;

/**
 * Created by bgonzalez on 09/08/2016.
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;



public class SocketServerTest
{

    private static final int SERVERPORT = 6000;

    private static final String SERVER_IP = "10.110.10.149";


    private static Socket socket;

    public static void main(String args[]) throws IOException {

        String str = "Test sending crud from server";

        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

        socket = new Socket(serverAddr, SERVERPORT);



        PrintWriter out = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),
        true);

        out.println(str);


    }
}