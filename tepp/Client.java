import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {
    
            String serverResponse;
            boolean firstTime = true;
            while ((serverResponse = in.readLine()) != null) {
                if (serverResponse.startsWith("Inserisci il tuo nome:") && firstTime) {
                    System.out.println(serverResponse);
                    String playerName = scanner.nextLine();
                    out.println(playerName);
                    firstTime = false;
                } else {
                    System.out.println("Server: " + serverResponse);
    
                    if (serverResponse.startsWith("Vuoi giocare di nuovo?")) {
                        String risposta = scanner.nextLine();
                        out.println(risposta.toLowerCase());
                        if (risposta.equalsIgnoreCase("no")) {
                            break;
                        }
                    }

                    if (serverResponse.startsWith("Desideri una carta aggiuntiva?")) {
                        String risposta = scanner.nextLine();
                        out.println(risposta.toLowerCase());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}