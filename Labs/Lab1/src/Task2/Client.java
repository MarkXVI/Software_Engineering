package Task2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        int PORT = 1234;
        Socket socket = null;
        ObjectOutputStream out = null;
        try {
            socket = new Socket("83.249.137.137", PORT); // Task2 : InetAddress.getLocalHost()
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O");
            System.exit(1);
        }
        Scanner scanner = new Scanner(System.in);

        String userInput;
        try{
            while ((userInput = scanner.nextLine()) != null) {
                Description description = new Description(userInput, Calendar.getInstance().getTime());
                out.writeObject(description);
                if (userInput.equals("q"))
                    break;
            }
            out.close();
            socket.close();
        }catch (IOException ioe) {
            System.out.println("Failed");
            System.exit(-1);
        }
    }
}