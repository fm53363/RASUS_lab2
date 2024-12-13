/*
 * This code has been developed at Departement of Telecommunications,
 * Faculty of Electrical Eengineering and Computing, University of Zagreb.
 */
package hr.fer.tel.rassus.stupidudp.client;

import hr.fer.tel.rassus.stupidudp.model.Sensor;
import hr.fer.tel.rassus.stupidudp.network.SimpleSimulatedDatagramSocket;

import java.io.IOException;
import java.net.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Krešimir Pripužić <kresimir.pripuzic@fer.hr>
 */
public class StupidUDPClient {


    private int serverPort;
    private Sensor server;
    private DatagramSocket socket;
    private InetAddress address;

    private double lossRate;
    private int averageDelay;

    public StupidUDPClient(Sensor server, double lossRate, int averageDelay) throws UnknownHostException, SocketException {
        this.server = server;
        this.serverPort = server.port();

        // determine the IP address of a host, given the host's name
        this.address = InetAddress.getByName(server.address());

        this.lossRate = lossRate;
        this.averageDelay = averageDelay;

        // create a datagram socket and bind it to any available
        // port on the local host
        //DatagramSocket socket = new SimulatedDatagramSocket(0.2, 1, 200, 50); //SOCKET
        this.socket = new SimpleSimulatedDatagramSocket(lossRate, averageDelay);
        System.out.println("Starting client that sends to server: " + server);
    }


    public void send(String sendString) throws IOException {
        byte[] rcvBuf = new byte[256]; // received bytes

        // encode this String into a sequence of bytes using the platform's
        // default charset and store it into a new byte array


        System.out.print("Client sends: ");
        // send each character as a separate datagram packet
        for (int i = 0; i < sendString.length(); i++) {
            byte[] sendBuf = new byte[1];// sent bytes
            sendBuf[0] = (byte) sendString.charAt(i);

            // create a datagram packet for sending data
            DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length,
                    address, this.serverPort);

            // send a datagram packet from this socket
            socket.send(packet); //SENDTO
            System.out.print(new String(sendBuf));
        }
        System.out.println("");

        StringBuffer receiveString = new StringBuffer();

        while (true) {
            // create a datagram packet for receiving data
            DatagramPacket rcvPacket = new DatagramPacket(rcvBuf, rcvBuf.length);

            try {
                // receive a datagram packet from this socket
                socket.receive(rcvPacket); //RECVFROM
            } catch (SocketTimeoutException e) {
                break;
            } catch (IOException ex) {
                Logger.getLogger(StupidUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }

            // construct a new String by decoding the specified subarray of bytes
            // using the platform's default charset
            receiveString.append(new String(rcvPacket.getData(), rcvPacket.getOffset(), rcvPacket.getLength()));

        }
        System.out.println("Client received: " + receiveString);

        // close the datagram socket
    }

    public boolean send1(String sendString) throws IOException {
        byte[] rcvBuf = new byte[256]; // received bytes
        // Encode the entire string into a byte array
        byte[] sendBuf = sendString.getBytes();
        System.out.println(this + " sends: " + sendString);
        // Create a single datagram packet for sending the entire string
        DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, address, this.serverPort);
        // Send the datagram packet from this socket

        socket.send(packet); // SENDTO
        System.out.println(this + " packet sent.");
        StringBuffer receiveString = new StringBuffer();
        // Create a datagram packet for receiving data
        DatagramPacket rcvPacket = new DatagramPacket(rcvBuf, rcvBuf.length);

        try {
            // Receive a datagram packet from this socket
            socket.receive(rcvPacket); // RECVFROM
        } catch (SocketTimeoutException e) {
            System.out.println(this + " Socket timed out.");
            return false;
            // Exit the loop on timeout
        } catch (IOException ex) {
            Logger.getLogger(StupidUDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Decode the received bytes and append them to the received string
        receiveString.append(new String(rcvPacket.getData(), rcvPacket.getOffset(), rcvPacket.getLength()));
        System.out.println(this + "potvrda " + receiveString);
        return true;
    }

    public void close() {
        socket.close();

    }

    @Override
    public String toString() {
        return "Client(server id=" +
                +server.id() + ") ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StupidUDPClient that)) return false;
        return Objects.equals(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(server);
    }
}
