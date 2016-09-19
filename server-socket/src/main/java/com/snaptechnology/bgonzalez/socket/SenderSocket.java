package com.snaptechnology.bgonzalez.socket;

import com.snaptechnology.bgonzalez.model.Device;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgonzalez on 05/09/2016.
 */
public class SenderSocket {

    private static final int RECEIVERPORT = 6000;
    
    
    private static Socket socket;

    private List<Device> devices ;


    public void sendData(List<Device> devices,String data){
        InetAddress deviceAddress = null;
        PrintWriter printer ;

        for(Device device : devices){
            try {
                deviceAddress = InetAddress.getByName(device.getIp());
                System.out.println("Sending to" + device.getIp());
                socket = new Socket(device.getIp(),RECEIVERPORT);
                printer = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())), true);
                printer.println(data);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        int c = 0;
        while(true){

            Thread.sleep(1000);
            SenderSocket senderSocket = new SenderSocket();
            List<Device> devices = new ArrayList<Device>();
            devices.add(new Device("AA","10.110.10.88"));
            senderSocket.sendData(devices,Integer.toString(c));
            c++;
        }


    }
}
