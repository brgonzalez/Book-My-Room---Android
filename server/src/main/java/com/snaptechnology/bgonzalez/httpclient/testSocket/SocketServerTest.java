package com.snaptechnology.bgonzalez.httpclient.testSocket;

/**
 * Created by bgonzalez on 09/08/2016.
 */
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SocketServerTest
{

    private static Socket socket;

    public static void main(String args[])
    {
        try
        {
            //Socket

            while(true) {
                Thread.sleep(1000);
                String host = "10.110.10.149";
                int port = 25000;
                InetAddress address = InetAddress.getByName(host);
                socket = new Socket(address,port);


                //Send the message to the server
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                String number = "Message from socketServer";

                String sendMessage = number + "\n";
                bw.write(sendMessage);
                bw.flush();
                System.out.println("Message sent to the device : " + sendMessage);

                /*
                //Get the return message from the server
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
                System.out.println("Message received from the server : " + message);
                System.out.println("PAsa");*/
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}