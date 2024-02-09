// Game.java
import java.io.*;
import java.net.*;
import java.util.*;

public class Game implements Runnable {
    private static final double DEFAULT_TARGET_SCORE = 7.5;
    private double targetScore=7.5;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<Double> mazziereHand;
    private List<Double> giocatoreHand;
    private MazzoDiCarte mazzo;
    private boolean riavviaGioco = true; 
   
    
    public Game(Socket socket) {
        this.clientSocket = socket;
        this.mazziereHand = new ArrayList<>();
        this.giocatoreHand = new ArrayList<>();
        this.mazzo = new MazzoDiCarte();
    }
    
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("Benvenuto al gioco Sette e Mezzo!");
            out.println("Inserisci il tuo nome:");

            String playerName = in.readLine();

            while (riavviaGioco) { 
                distribuisciCarte();
                gestisciGiocatore();
                gestisciMazziere();
                determinaVincitore();
                out.println("Vuoi giocare di nuovo? (sì/no)");
                String risposta = in.readLine();
                if (!risposta.equalsIgnoreCase("si")) {
                    riavviaGioco = false;
                    out.println("Grazie per aver giocato!"); 
                  
                }
            }
            
           
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
                
           
    
    

    private void distribuisciCarte() {
        mazziereHand.clear();
        giocatoreHand.clear();

        mazziereHand.add(mazzo.pescaCarta());
        giocatoreHand.add(mazzo.pescaCarta());
    }

    private void gestisciGiocatore() throws IOException {
        boolean pescaAncora = true;
        while (pescaAncora) {
            out.println("La tua mano: " + giocatoreHand + ". Il punteggio è: " + calcolaPunteggio(giocatoreHand));
            out.println("Desideri una carta aggiuntiva? (sì/no)");
            String risposta = in.readLine();
            if (risposta.equalsIgnoreCase("si")) {
                double cartaPescata = mazzo.pescaCarta();
                giocatoreHand.add(cartaPescata);
                double punteggio = calcolaPunteggio(giocatoreHand);
                out.println("Hai pescato una carta: " + cartaPescata + ". La tua mano: " + giocatoreHand + ". Il punteggio è: " + punteggio);
                if (punteggio > 7.5) {
                    out.println("Hai sballato! Il punteggio è superiore a 7.5.");
                    pescaAncora = false;
                    break;
                }
            } else if (risposta.equalsIgnoreCase("no")) {
                pescaAncora = false;
            } else {
                out.println("Risposta non valida. Rispondi 'sì' o 'no'.");
            }
        }
    }
    

    private void gestisciMazziere() {
        double punteggio = calcolaPunteggio(giocatoreHand);
        if (punteggio > 7.5) {
            
            return;
          
        }
        double punteggioMazziere = calcolaPunteggio(mazziereHand);
        while (punteggioMazziere < 5) {
            mazziereHand.add(mazzo.pescaCarta());
            punteggioMazziere = calcolaPunteggio(mazziereHand);
        }
        out.println("Il punteggio del mazziere è: " + punteggioMazziere + ". La mano del mazziere: " + mazziereHand);
    }

    private void determinaVincitore() {
        double punteggioMazziere = calcolaPunteggio(mazziereHand);
        double punteggioGiocatore = calcolaPunteggio(giocatoreHand);

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

    private double calcolaPunteggio(List<Double> hand) {
        double punteggio = 0;
        for (double carta : hand) {
            if (carta <= 7) {
                punteggio += carta;
            } else {
                punteggio += 0.5;
            }
        }
        return punteggio;
    }
}
