package com.snaptechnology.bgonzalez.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by bgonzalez on 05/09/2016.
 */
public class SenderSocket {

    private static final int RECEIVERPORT = 6000;
    
    
    private static Socket socket;

    private List<Device> devices ;


    public void sendData(String data){
        InetAddress deviceAddress = null;
        PrintWriter printer ;

        for(Device device : devices){
            try {
                deviceAddress = InetAddress.getByName(device.getIp());
                socket = new Socket(deviceAddress,RECEIVERPORT);
                printer = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())), true);
                printer.println(data);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException {

    }
}
