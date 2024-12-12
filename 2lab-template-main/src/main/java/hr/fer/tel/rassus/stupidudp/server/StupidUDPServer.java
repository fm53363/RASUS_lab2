/*
 * This code has been developed at Departement of Telecommunications,
 * Faculty of Electrical Engineering and Computing, University of Zagreb.
 */
package hr.fer.tel.rassus.stupidudp.server;

import hr.fer.tel.rassus.stupidudp.mapper.SensorPacketMapper;
import hr.fer.tel.rassus.stupidudp.model.SensorPacket;
import hr.fer.tel.rassus.stupidudp.network.SimpleSimulatedDatagramSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

/**
 * @author Krešimir Pripužić <kresimir.pripuzic@fer.hr>
 */
public class StupidUDPServer {

    private int port;
    private DatagramSocket socket;
    private double lossRate;
    private int averageDelay;

    public StupidUDPServer(int port, double lossRate, int averageDelay) throws SocketException {
        // create a UDP socket and bind it to the specified port on the local
        // host
        this.socket = new SimpleSimulatedDatagramSocket(port, 0.2, 200);
        this.port = socket.getLocalPort();
        //SOCKET -> BIND
    }

    public int getPort() {
        return this.port;
    }

    public void startServer(List<SensorPacket> list) throws IOException {
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

            try {
                SensorPacket rcvPacket = SensorPacketMapper.toSensorPacket(rcvStr);
            } catch (Exception e) {
                System.out.println("Error parsing packet: " + e.getMessage());
            }
            //list.add(SensorPacketMapper.toSensorPacket(rcvStr));

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
