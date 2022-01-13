import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static final String serverHostname = "localhost";
    private static final int serverPort = 7777;  

    public static void main(String[] args) {
        BufferedReader networkIn = null; // für Antworten des Servers
        PrintWriter networkOut = null;   // für Anfragen zum Server
        BufferedReader userIn            // für Nutzereingaben
            = new BufferedReader(new InputStreamReader(System.in));
        Socket socket = null;            // der Socket zum Server

        while (true) {
            System.out.println("\n========================");
            System.out.println("Enter a command:");
            try {
                // Nutzereingabe
                String command = userIn.readLine();
                if (command == null) {
                    System.out.println("Invalid input");
                    continue;
                }
                // Austritt aus der Endlosschleife
                if (command.equals(".")) { break; }

                // Verbindung zum Server aufbauen
                socket = new Socket(serverHostname, serverPort);
                System.out.println("Connected to Server: " + serverHostname + ":" + serverPort);
                System.out.println("========================");

                // Netzwerkeingabe und -ausgabe initialisieren 
                networkIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );
                networkOut = new PrintWriter(socket.getOutputStream());

                /* Kommando in den Output schreiben,
                   dann mittels flush() versenden. */
                networkOut.println(command);
                networkOut.flush();

                String response = networkIn.readLine(); // auf eine Antwort des Servers warten.
                System.out.println("Response from Server: " + response);
                System.out.println("========================\n");

            } catch (IOException e) {
                System.out.println("Could not connect to the Server: " + e.getMessage());
                break;
            } finally {
                // Aufräumen
                try {
                    if (networkIn != null) networkIn.close();
                    if (networkOut != null) networkOut.close();
                    if (socket != null) socket.close();
                } catch (IOException ignored) {}
            }
        }
        // Aufräumen
        try {
            userIn.close();
        } catch (IOException ignored) {}
    }
}
