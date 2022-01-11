import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Server {

    private static final int port = 7777; // Server-Port
    private static final String folderPath = System.getProperty("user.home") + "/Desktop/Messages/";

    // Request-Konstanten
    private static final String SAVE = "SAVE";
    private static final String KEY = "KEY";
    private static final String GET = "GET";
    private static final String OK = "OK";
    private static final String FAILED = "FAILED";
    private static final String BAD_REQUEST = "BAD REQUEST";

    public static void main(String[] args) {
        ServerSocket server = null;
        BufferedReader networkIn = null;
        PrintWriter networkOut = null;

        // äußerer try-Block -> Server-Teil
        try {
            server = new ServerSocket(port); // Server starten
            System.out.println("Server running on Port " + port);
            while(true) {
                Socket s = null;
                // innerer try-Block -> einzelne Verbindungen
                try {
                    s = server.accept(); // auf Verbindung durch Client warten

                    networkIn = new BufferedReader(new InputStreamReader(s.getInputStream())); // eingehende Nachrichten des Clients
                    networkOut = new PrintWriter(s.getOutputStream());                         // ausgehende Nachrichten an den Client

                    String clientRequest = networkIn.readLine(); // eingehende Anfrage des Clients

                    // Kommando (GET/SAVE) von Rest der Nachricht trennen
                    String[] splitRequest = clientRequest.split(" ");
                    String command = splitRequest[0].toUpperCase(); // das Kommando
                    String requestBody = getRequestBodyFromSplitString(splitRequest); // der Rest der Nachricht

                    // Das Kommando interpretieren
                    String response = "";
                    switch(command) {
                        case SAVE:
                            response = saveTextWithKey(requestBody);    // Verarbeiten der SAVE-Anfrage
                            break;
                        case GET:
                            response = getByKey(requestBody);           // Verarbeiten der GET-Anfrage
                            break;
                        default:
                            response = BAD_REQUEST;                     // weder SAVE noch GET
                    }

                    System.out.println("\n========================");
                    System.out.println("Client-request received");
                    System.out.println("========================");
                    System.out.println("Client port: " + s.getPort());
                    System.out.println("Command: " + command);
                    System.out.println("Request Body:\n" + requestBody);
                    System.out.println("========================");
                    System.out.println("Sending response: " + response);
                    System.out.println("========================\n");

                    networkOut.println(response); // Antwort in den Output schreiben
                    networkOut.flush();           // Output verschicken
                } catch (IOException e) {
                } finally {
                    // Ressourcen der Verbindung aufräumen
                    if (s != null) s.close();
                    if (networkIn != null) networkIn.close();
                    if (networkOut != null) networkOut.close();
                }
            } // end while
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Ressourcen des Servers aufräumen
                if (server != null) server.close();
            } catch (IOException e) {}
        }
    }

    /**
     * Diese Methode gibt zu einen eingegebenen Key eine
     * entsprechende Antwort zurück. (OK message ODER FAILED)
     * 
     * @param key Der Key, unter dem die Nachricht gespeichert ist.
     * @return    Die Antwort, die an den Client gesendet wird.
     */
    private static String getByKey(String key) {
        BufferedReader bufferedFileReader = null;
        try {
            /* BufferedReader, der von der dem Key
               entsprechenden Datei ließt. */
            bufferedFileReader = new BufferedReader(
                new FileReader(folderPath + key + ".txt")
            );
            String line = bufferedFileReader.readLine();
            return OK + " " + line;
        } catch (IOException e) {
            // Fehlerfall: keine Datei zu Key gefunden
            return FAILED;
        } finally {
            // Ressourcen aufräumen
            try {
                if (bufferedFileReader != null)
                    bufferedFileReader.close();
            } catch (IOException e) {}
        }
    }

    /**
     * Diese Methode Speichert eine gesendete Nachricht
     * unter einer zufälligen UUID.
     * 
     * @param message Die zu speichernde Nachricht.
     * @return        Der Key, unter dem diese Nachricht
     *                gespeichert wird/aufgerufen werden
     *                kann.
     */
    private static String saveTextWithKey(String message) throws IOException {
        String key = UUID.randomUUID().toString();
        PrintWriter printWriter = new PrintWriter(new FileWriter(folderPath + key + ".txt"));
        printWriter.write(message);
        printWriter.close();
        return KEY + " " + key;
    }

    /**
     * Diese Methode fügt die getrennten Einzelteile der Nachricht
     * zu einem String zusammen und gibt den String zurück.
     * Dabei wird das erste Wort (das Kommando) übersprungen.
     * 
     * @param splitRequest Die einzelnen Wörter der Nachricht als String-Array.
     * @return             Die zusammengefügte Nachricht.
     */
    private static String getRequestBodyFromSplitString(String[] splitRequest) {
        String message = "";
        // erstes Element des Arrays (Kommando) überspringen
        for (int i = 1; i < splitRequest.length; i++) {
            message += splitRequest[i];
            if (i != (splitRequest.length-1)) {
                message += " ";
            }
        }
        return message;
    }
}
