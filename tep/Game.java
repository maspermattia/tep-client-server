import java.io.*;
import java.net.*;
import java.util.*;

public class Game implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<Integer> mazziereHand;
    private List<List<Integer>> giocatoriHands;
    private MazzoDiCarte mazzo; 

    public Game(Socket socket) {
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

        for (int i = 0; i < 2; i++) {
            mazziereHand.add(mazzo.pescaCarta()); 
            for (int j = 0; j < 2; j++) {
                List<Integer> hand = new ArrayList<>();
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
                giocatoriHands.get(i).add(pescaCarta());
                double punteggio = calcolaPunteggio(giocatoriHands.get(i));
                out.println("Hai pescato una carta. La tua mano: " + giocatoriHands.get(i) + ". Il punteggio è: " + punteggio);
                if (punteggio >= TARGET_SCORE) {
                    break;
                }
                out.println("Desideri una carta aggiuntiva? (sì/no)");
                risposta = in.readLine();
            }
        }
    }

    private void gestisciMazziere() {
        double punteggioMazziere = calcolaPunteggio(mazziereHand);
        while (punteggioMazziere < TARGET_SCORE) {
            mazziereHand.add(pescaCarta());
            punteggioMazziere = calcolaPunteggio(mazziereHand);
        }
        out.println("Il punteggio del mazziere è: " + punteggioMazziere + ". La mano del mazziere: " + mazziereHand);
    }

    private void determinaVincitori() {
        double punteggioMazziere = calcolaPunteggio(mazziereHand);
        for (int i = 0; i < giocatoriHands.size(); i++) {
            double punteggioGiocatore = calcolaPunteggio(giocatoriHands.get(i));
            if (punteggioGiocatore > TARGET_SCORE) {
                out.println("Hai sballato con un punteggio di " + punteggioGiocatore);
            } else if (punteggioMazziere > TARGET_SCORE) {
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

    private double calcolaPunteggio(List<Integer> hand) {
        double punteggio = 0;
        for (int carta : hand) {
            if (carta <= 7) {
                punteggio += carta;
            } else { 
                if
                punteggio += 0.5;
            }
        }
        return punteggio;
    }
}
