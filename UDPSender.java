/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Emil
 */
import java.net.*;// need this for InetAddress, Socket, ServerSocket 
import java.io.*;// need this for I/O stuff

public class UDPSender 
{ 
    static final int BUFSIZE=1024;
    
    DatagramSocket s;
    DatagramPacket dps, dpr; // data packet send/receive
    InetAddress iaddr;
    int sendPort;
    int timeout = 500;
    String msg = "Wuzzaaaa";
            
    static public void main(String args[]) throws SocketException, 
            UnknownHostException, InterruptedException
    { 
        if (args.length != 2) {
            throw new IllegalArgumentException("Must specify an ip and a port!"); 
        }
        UDPSender s = new UDPSender();
        String ip = args[0];
        int sendPort = Integer.parseInt(args[1]);
        int sleepTime = 500;
        try { 
            s.Connect(ip, sendPort);
            while (true) 
            {
                s.Send();
                s.Receive();       
                Thread.sleep(sleepTime);
            }
        } catch (IOException e) 
        {
            System.out.println("Fatal I/O Error !"); 
            System.exit(0);
        } 
    }
    void Connect(String ip, int sendPort) throws SocketException, UnknownHostException
    {
        this.sendPort = sendPort;
        int socketPort = sendPort + 4000;
        s = new DatagramSocket(socketPort);
        dps = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
        dps.setData(msg.getBytes(), 0, msg.length());
        int offset = dps.getOffset();

        dpr = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
        iaddr = InetAddress.getByName(ip);
        System.out.println("Target IP: "+iaddr.getHostAddress());
        System.out.println("Hosted on port "+socketPort+" sending to port "+sendPort);
        s.setSoTimeout(timeout);
    }
    void Send() throws IOException
    {
   //     dps.setLength(BUFSIZE);// avoid shrinking the packet buffer
				
        // print out client's address 
        // we add our name
        byte[] data = dps.getData();
        dps.setAddress(iaddr);
        dps.setPort(sendPort);
        System.out.println("Sending: "+msg);
        // Send it right back 
        s.send(dps);
    }
    void Receive() throws IOException
    {
//        dp.setLength(dp.getLength());// avoid shrinking the packet buffer
        try {
            dpr.setLength(BUFSIZE);
            s.receive(dpr);
            byte[] receivedBytes = dpr.getData();
            String text = new String(receivedBytes);
     //       System.out.println("text: "+text);
            System.out.println("Message from: "+dpr.getAddress().getHostAddress()+":"+dpr.getPort()+" "+text);
        } catch(SocketTimeoutException e)
        {
            return;
        }
    }
}