package app;
import hogwarts.APersonaje;
import hogwarts.CMagoAuror;
import hogwarts.CMagoEstudiante;
import hogwarts.CMagoProfesor;
import hogwarts.CMortifagoComandante;
import hogwarts.CMortifagoLacayo;

import java.util.Random;


public class PersonajeManager {
	private PersonajeManager() {}
	private static final String[] NOMBRES_MAGOS = {"Gideon", "Fabian", "Kingsley", "Nymphadora", "Remus", "Sirius", "Amos", "Cedric"};
    private static final String[] NOMBRES_MORTIFAGOS = {"Avery", "Macnair", "Yaxley", "Dolohov", "Rowle", "Crabbe", "Goyle", "Jugson"};
    private static final Random random = new Random();
    
    public static APersonaje crearMago(FCatalogoDePersonajes per) {
        return per.construir();
    }
    public static APersonaje crearMortifago(FCatalogoDePersonajes per) {
    	return per.construir();
    }
    
    public static APersonaje crearMago() {
        String nombre = NOMBRES_MAGOS[random.nextInt(NOMBRES_MAGOS.length)];

        int rolAleatorio = random.nextInt(3);
        APersonaje magoGenerado = switch (rolAleatorio) {
            case 0 -> new CMagoAuror("Auror " + nombre);
            case 1 -> new CMagoProfesor("Prof. " + nombre);
            case 2 -> new CMagoEstudiante("Estudiante " + nombre);
            default -> throw new IllegalArgumentException("Unexpected value: " + rolAleatorio);
        };

        return magoGenerado;
    }
    public static APersonaje crearMortifago() {
        String nombre = NOMBRES_MORTIFAGOS[random.nextInt(NOMBRES_MORTIFAGOS.length)];

        int rolAleatorio = random.nextInt(2);
        APersonaje mortifagoGenerado = switch (rolAleatorio) {
            case 0 -> new CMortifagoComandante("Comandante " + nombre);
            case 1 -> new CMortifagoLacayo("Lacayo. " + nombre);
            default -> throw new IllegalArgumentException("Unexpected value: " + rolAleatorio);
        };

        return mortifagoGenerado;
    }
}
