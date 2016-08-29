package com.snaptechnology.bgonzalez.socket;

/**
 * Created by bgonzalez on 09/08/2016.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class SocketClient {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        String serverAddress = "localhost";
        Socket s = new Socket(serverAddress, 9090);
        while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer=input.readLine();
            System.out.println(answer);
        }

    }
}