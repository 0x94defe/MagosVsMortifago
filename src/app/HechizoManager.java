package app;
import hogwarts.AHabilidad;
import hogwarts.ENivel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public final class HechizoManager {
	private HechizoManager() {}
    private static final Random random = new Random();

    public static AHabilidad crearHechizo(FLibroDeHechizos hechizo) {
        return hechizo.construir();
    }

    public static AHabilidad crearHechizo(ENivel dificultad) {
        List<FLibroDeHechizos> filtrados = new ArrayList<>();
        
        for (FLibroDeHechizos h : FLibroDeHechizos.values()) {
            if (h.getNivelRequerido() == dificultad) {
                filtrados.add(h);
            }
        }

        if (filtrados.isEmpty()) throw new IllegalArgumentException("No hay hechizos de nivel " + dificultad);
        

        int randomIndex = random.nextInt(filtrados.size());
        return filtrados.get(randomIndex).construir();
    }
}
