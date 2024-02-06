import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
    try {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server avviato sulla porta " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nuova connessione accettata: " + clientSocket);

            // Creazione di un thread per gestire la connessione del client
            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Messaggio dal client: " + inputLine);
                out.println("Risposta dal server: " + inputLine);
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
