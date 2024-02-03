import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private static final double TARGET_SCORE = 7.5;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Inserisci il tuo nome: ");
            String playerName = scanner.nextLine();
            out.println(playerName);

            String serverResponse = in.readLine();
            System.out.println("Server: " + serverResponse);

            out.println("start"); 

            
            while (true) {
                String message = in.readLine();
                System.out.println("Server: " + message);

                if (message.startsWith("Vuoi giocare di nuovo?")) {
                    String risposta = scanner.nextLine();
                    out.println(risposta.toLowerCase());
                    if (!risposta.equalsIgnoreCase("sì")) {
                        break;
                    }
                }

                if (message.startsWith("La tua mano")) {
                    String risposta = scanner.nextLine();
                    out.println(risposta.toLowerCase());
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
