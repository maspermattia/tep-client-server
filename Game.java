import java.io.*;
import java.net.*;
import java.util.*;

public class Game implements Runnable {
    private static final double DEFAULT_TARGET_SCORE = 7.5;
    private double targetScore;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<Double> mazziereHand; // Modifica: Corretto il tipo di dato
    private List<List<Double>> giocatoriHands; // Modifica: Corretto il tipo di dato
    private MazzoDiCarte mazzo;

    public Game(Socket socket) {
        this.targetScore = DEFAULT_TARGET_SCORE;
        this.clientSocket = socket;
        this.mazziereHand = new ArrayList<>();
        this.giocatoriHands = new ArrayList<>();
        this.mazzo = new MazzoDiCarte();
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("Benvenuto al gioco Sette e Mezzo!");

            while (true) {
                in.readLine();
                distribuisciCarte();
                gestisciGiocatori();
                gestisciMazziere();
                determinaVincitori();
                out.println("Vuoi giocare di nuovo? (sì/no)");
                String risposta = in.readLine();
                if (!risposta.equalsIgnoreCase("sì")) {
                    break;
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void distribuisciCarte() {
        mazziereHand.clear();
        giocatoriHands.clear();

        for (int i = 0; i < 1; i++) {
            mazziereHand.add(mazzo.pescaCarta());
            for (int j = 0; j < 1; j++) {
                List<Double> hand = new ArrayList<>(); // Modifica: Corretto il tipo di dato
                hand.add(mazzo.pescaCarta());
                giocatoriHands.add(hand);
            }
        }
    }

    private void gestisciGiocatori() throws IOException {
        for (int i = 0; i < giocatoriHands.size(); i++) {
            out.println("La tua mano: " + giocatoriHands.get(i) + ". Il punteggio è: " + calcolaPunteggio(giocatoriHands.get(i)));
            out.println("Desideri una carta aggiuntiva? (sì/no)");
            String risposta = in.readLine();
            while (risposta.equalsIgnoreCase("sì")) {
                giocatoriHands.get(i).add(mazzo.pescaCarta()); // Modifica: Pesca carta dal mazzo
                double punteggio = calcolaPunteggio(giocatoriHands.get(i));
                out.println("Hai pescato una carta. La tua mano: " + giocatoriHands.get(i) + ". Il punteggio è: " + punteggio);
                if (punteggio >= targetScore) {
                    break;
                }
                out.println("Desideri una carta aggiuntiva? (sì/no)");
                risposta = in.readLine();
            }
        }
    }

    private void gestisciMazziere() {
        double punteggioMazziere = calcolaPunteggio(mazziereHand);
        while (punteggioMazziere < targetScore) {
            mazziereHand.add(mazzo.pescaCarta()); // Modifica: Pesca carta dal mazzo
            punteggioMazziere = calcolaPunteggio(mazziereHand);
        }
        out.println("Il punteggio del mazziere è: " + punteggioMazziere + ". La mano del mazziere: " + mazziereHand);
    }

    private void determinaVincitori() {
        double punteggioMazziere = calcolaPunteggio(mazziereHand);
        for (int i = 0; i < giocatoriHands.size(); i++) {
            double punteggioGiocatore = calcolaPunteggio(giocatoriHands.get(i));
            if (punteggioGiocatore > targetScore) {
                out.println("Hai sballato con un punteggio di " + punteggioGiocatore);
            } else if (punteggioMazziere > targetScore) {
                out.println("Il mazziere ha sballato. Hai vinto con un punteggio di " + punteggioGiocatore);
            } else if (punteggioGiocatore > punteggioMazziere) {
                out.println("Hai vinto con un punteggio di " + punteggioGiocatore + " contro il punteggio del mazziere di " + punteggioMazziere);
            } else if (punteggioGiocatore == punteggioMazziere) {
                out.println("Pareggio con il mazziere. Il tuo punteggio è " + punteggioGiocatore);
            } else {
                out.println("Hai perso. Il tuo punteggio è " + punteggioGiocatore + " contro il punteggio del mazziere di " + punteggioMazziere);
            }
        }
    }

    private double calcolaPunteggio(List<Double> hand) { // Modifica: Corretto il tipo di dato
        double punteggio = 0;
        for (double carta : hand) { // Modifica: Corretto il tipo di dato
            if (carta <= 7) {
                punteggio += carta;
            } else {
                punteggio += 0.5;
            }
        }
        return punteggio;
    }
}
