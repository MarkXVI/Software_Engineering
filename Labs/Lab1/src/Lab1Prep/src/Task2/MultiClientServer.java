package Task2;

import java.io.*;
import java.net.*;

public class MultiClientServer extends Thread {

    Integer PORT = 1234;

    public MultiClientServer() {}

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket= new ServerSocket(PORT);
            System.out.println(serverSocket.getInetAddress());
            // gives 0.0.0.0 when it is not connected to TCP/IP network

            System.out.println(serverSocket.getLocalPort());
            // the PORT number where it is listening

        }catch (IOException e) {
            System.out.println("Could not listen on port: " + PORT);
            System.exit(-1);
        }
        Socket clientSocket = null;
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                Server server = new Server(clientSocket);
                server.start();
            } catch (IOException e) {
                System.out.println("Accept failed: " + PORT);
                System.exit(-1);
            }
        }
    }
}
