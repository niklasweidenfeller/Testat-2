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

    private static final int port = 7777;
    private static final String folderPath = System.getProperty("user.home") + "/Desktop/Messages/";

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

        try {
            server = new ServerSocket(port);
            System.out.println("Server running on Port " + port);
            while(true) {
                Socket s = null;
                try {
                    s = server.accept();

                    networkIn = new BufferedReader(
                        new InputStreamReader(s.getInputStream())
                    );
                    networkOut = new PrintWriter(s.getOutputStream());

                    String clientRequest = networkIn.readLine();

                    String[] splitRequest = clientRequest.split(" ");
                    String command = splitRequest[0].toUpperCase();
                    String requestBody = getRequestBodyFromSplitString(splitRequest);

                    System.out.println("\n========================");
                    System.out.println("Client-request received");
                    System.out.println("========================");
                    System.out.println("Client port: " + s.getPort());
                    System.out.println("Command: " + command);
                    System.out.println("Request Body:\n" + requestBody);

                    String response = "";
                    switch(command) {
                        case SAVE:
                            response = saveTextWithKey(requestBody);
                            break;
                        case GET:
                            response = getByKey(requestBody);
                            break;
                        default:
                            response = BAD_REQUEST;
                    }

                    System.out.println("Sending response: " + response);
                    System.out.println("========================\n");
                    networkOut.println(response);
                    networkOut.flush();
                } catch (IOException e) {
                } finally {
                    if (s != null) s.close();
                    if (networkIn != null) networkIn.close();
                    if (networkOut != null) networkOut.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (server != null) server.close();
            } catch (IOException e) {}
        }
    }

    private static String getByKey(String key) {
        BufferedReader bufferedFileReader = null;
        try {
            bufferedFileReader = new BufferedReader(
                new FileReader(folderPath + key + ".txt")
            );
            String line = bufferedFileReader.readLine();
            bufferedFileReader.close();
            return OK + " " + line;
        } catch (IOException e) {
            return FAILED;
        }
    }


    private static String saveTextWithKey(String message) throws IOException {
        String key = UUID.randomUUID().toString();
        PrintWriter printWriter = new PrintWriter(new FileWriter(folderPath + key + ".txt"));
        printWriter.write(message);
        printWriter.close();
        return KEY + " " + key;
    }


    private static String getRequestBodyFromSplitString(String[] splitRequest) {
        String message = "";
        for (int i = 1; i < splitRequest.length; i++) {
            message += splitRequest[i];
            if (i != (splitRequest.length-1)) {
                message += " ";
            }
        }
        return message;
    }
}
