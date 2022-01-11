import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String serverHostname = "localhost";
    private static final int serverPort = 7777;  


    public static void main(String[] args) {
        BufferedReader networkIn = null;
        PrintWriter networkOut = null;
        BufferedReader userIn = null;
        Socket socket = null;

        while (true) {
            System.out.println("Enter a command:");
            try {
                userIn = new BufferedReader(new InputStreamReader(System.in));
                String command = userIn.readLine();
                if (command.equals(".")) { break; }

                socket = new Socket(serverHostname, serverPort);
                System.out.println("\nConnected to Server: " + serverHostname + ":" + serverPort + "\n");

                networkIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );
                networkOut = new PrintWriter(socket.getOutputStream());

                networkOut.println(command);
                networkOut.flush();

                System.out.println("Response from server: " + networkIn.readLine());
                System.out.println("========================\n");


            } catch (IOException e) {
                System.out.println("Error connecting to the server: " + e.getMessage());
                break;
            }
        }
        try {
            if (networkIn != null) networkIn.close();
            if (networkOut != null) networkOut.close();
            if (userIn != null) userIn.close();
            if (socket != null) socket.close();
        } catch (IOException e) {}
    }
}
