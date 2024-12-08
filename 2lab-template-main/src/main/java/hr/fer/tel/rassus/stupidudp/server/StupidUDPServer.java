/*
 * This code has been developed at Departement of Telecommunications,
 * Faculty of Electrical Engineering and Computing, University of Zagreb.
 */
package hr.fer.tel.rassus.stupidudp.server;

import hr.fer.tel.rassus.stupidudp.network.SimpleSimulatedDatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author Krešimir Pripužić <kresimir.pripuzic@fer.hr>
 */
public class StupidUDPServer {

    private int port;
    private DatagramSocket socket;

    public int getPort() {
        return this.port;
    }

    public StupidUDPServer(int port) throws SocketException {
        // create a UDP socket and bind it to the specified port on the local
        // host
       this.socket = new SimpleSimulatedDatagramSocket(port, 0.2, 200);
        this.port = socket.getLocalPort();
        //SOCKET -> BIND
    }


    public void startServer () throws IOException {
        byte[] rcvBuf = new byte[256]; // received bytes
        byte[] sendBuf = new byte[256];// sent bytes
        String rcvStr;

        System.out.println("Starting UDP server...");

        while (true) { //OBRADA ZAHTJEVA
            // create a DatagramPacket for receiving packets
            DatagramPacket packet = new DatagramPacket(rcvBuf, rcvBuf.length);

            // receive packet
            socket.receive(packet); //RECVFROM

            // construct a new String by decoding the specified subarray of
            // bytes
            // using the platform's default charset
            rcvStr = new String(packet.getData(), packet.getOffset(),
                    packet.getLength());
            System.out.println("Server received: " + rcvStr);

            // encode a String into a sequence of bytes using the platform's
            // default charset
            sendBuf = rcvStr.toUpperCase().getBytes();
            System.out.println("Server sends: " + rcvStr.toUpperCase());

            // create a DatagramPacket for sending packets
            DatagramPacket sendPacket = new DatagramPacket(sendBuf,
                    sendBuf.length, packet.getAddress(), packet.getPort());

            // send packet
            socket.send(sendPacket); //SENDTO
        }
    }
}
