/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpsender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

/**
 *
 * @author Emil
 */
public class TCPSender 
{
    Socket sock;
    String ipAddress;
    int port;
    
    TCPSender(String ipAddr, int targetPort)
    {
        ipAddress = ipAddr;
        port = targetPort;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException 
    {
        String ip = "127.0.0.1";
        int port = 4032;
        if (args.length > 0)
            ip = args[0];
        if (args.length > 1)
            port = Integer.parseInt(args[1]);
        TCPSender sender = new TCPSender(ip, port);
        if (!sender.Connect())
            return;
        int sleepTime = 500;
        System.out.println("Writing every "+sleepTime+" ms");
        while (true)
        {
            sender.Write("Testing testing 1-2-3\n");
            sender.Read();
            
            Thread.sleep(sleepTime);
            if (sender.sock.isClosed() || !sender.sock.isConnected())
            {
                System.out.println("Socket closed");
                return;
            }
        }
    }
    boolean Connect()
    {
        try {
            System.out.println("Connecting to "+ipAddress+":"+port);
            sock = new Socket(ipAddress, port);
        }
        catch (java.net.ConnectException e)
        {
            System.out.println(e.toString());
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        System.out.println("Connected");
        return true;
    }
    void Write(String text) throws IOException
    {
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        out.println(text);
        out.flush();
    }
    void Read() throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
   //   System.out.println("Making new buffered readerrr");
        if (!in.ready())
            return;
        String line = in.readLine();
        System.out.println("recv: "+line);
    }
    
}
