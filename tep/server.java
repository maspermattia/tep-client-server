import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server avviato sulla porta " + PORT);

            // Gestione delle connessioni multiple
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione accettata: " + clientSocket);

                // Creazione di un thread per gestire la connessione del client
                Thread clientThread = new Thread(new Game(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
