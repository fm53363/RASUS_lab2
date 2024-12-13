/*
 * This code has been developed at Departement of Telecommunications,
 * Faculty of Electrical Engineering and Computing, University of Zagreb.
 */
package hr.fer.tel.rassus.stupidudp.server;

import hr.fer.tel.rassus.stupidudp.mapper.SensorPacketMapper;
import hr.fer.tel.rassus.stupidudp.model.SensorPacket;
import hr.fer.tel.rassus.stupidudp.model.VectorClock;
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

    private VectorClock vectorClock;
    private List<SensorPacket> packets;
    private List<SensorPacket> intervalPackets;

    public StupidUDPServer(int port, double lossRate, int averageDelay) throws SocketException {
        // create a UDP socket and bind it to the specified port on the local
        // host
        this.lossRate = lossRate;
        this.averageDelay = averageDelay;
        this.socket = new SimpleSimulatedDatagramSocket(port, 0.2, 200);
        this.port = socket.getLocalPort();

        //SOCKET -> BIND
    }

    public void setVectorClock(VectorClock vectorClock) {
        this.vectorClock = vectorClock;
    }

    public void setPackets(List<SensorPacket> packets) {
        this.packets = packets;
    }

    public void setIntervalPackets(List<SensorPacket> intervalPackets) {
        this.intervalPackets = intervalPackets;
    }

    public int getPort() {
        return this.port;
    }

    public void startServer(List<SensorPacket> list) throws IOException {
        byte[] rcvBuf = new byte[256]; // received bytes
        byte[] sendBuf = new byte[256];// sent bytes
        String rcvStr;

        System.out.println("    Starting UDP server...");

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
            System.out.println("    Server received: " + rcvStr);

            try {
                SensorPacket rcvPacket = SensorPacketMapper.toSensorPacket(rcvStr);
                this.vectorClock.updateAfterReceiving(rcvPacket.getVectorClock());
                this.intervalPackets.add(rcvPacket);
                this.packets.add(rcvPacket);
            } catch (Exception e) {
                System.out.println("    Error parsing packet: " + e.getMessage());
            }
            //list.add(SensorPacketMapper.toSensorPacket(rcvStr));

            // encode a String into a sequence of bytes using the platform's
            // default charset
            //sendBuf = rcvStr.toUpperCase().getBytes();
            String msg = "P:" + SensorPacketMapper.toSensorPacket(rcvStr).getReading().toString();
            sendBuf = msg.getBytes();
            System.out.println("    Server sends: " + msg);

            // create a DatagramPacket for sending packets
            DatagramPacket sendPacket = new DatagramPacket(sendBuf,
                    sendBuf.length, packet.getAddress(), packet.getPort());

            // send packet
            socket.send(sendPacket); //SENDTO


        }
    }
}
