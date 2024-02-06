import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazzoDiCarte {
    private List<Integer> carte;

    public MazzoDiCarte() {
        carte = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            carte.add(i);
            carte.add(i);
            carte.add(i);
            carte.add(i);
        }
        mescolaMazzo();
    }

    public double pescaCarta() {
        if (carte.isEmpty()) {
            return 0.0; 
        }
        int valoreCarta = carte.remove(0);
        if (valoreCarta >= 8 && valoreCarta <= 10) {
            return 0.5; 
        }
        return valoreCarta;
    }

    private void mescolaMazzo() {
        Collections.shuffle(carte);
    }
}