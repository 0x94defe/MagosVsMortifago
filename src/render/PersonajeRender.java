package render;
import sim.IObservable;

import java.awt.Color;


public class PersonajeRender {  
	public static Color getColor(IObservable e) {
	    String bando = e.getNombreBando().toUpperCase();
	    String rol = e.getNombreClase();
	    //int idBando = e.getIdBando();

	    return switch (bando) {
	        case "MAGO" -> switch (rol) {
	            // Podés usar condiciones específicas combinando rol e ID
	            case "Mago Raso"                   -> new Color(120, 150, 220); // Un azul base un poco más oscuro
	            case "Auror"                       -> new Color(60, 130, 240);  // Azul eléctrico de combate
	            case "Profesor"                    -> new Color(100, 220, 200); // Cian menta
	            case "Estudiante"                  -> new Color(130, 220, 130); // Verde suave
	            default                            -> new Color(90, 160, 250);  // Tu azul sutil por defecto
	        };
	
	        case "MORTIFAGO" -> switch (rol) {
	            case "Mortifago Raso" -> new Color(140, 110, 170); // Violeta sombrío
	            case "Comandante"     -> new Color(240, 90, 90);   // Rojo peligro
	            case "Lacayo"         -> new Color(240, 150, 70);  // Naranja fuego
	            default               -> new Color(200, 70, 70);   // Rojo base
	        };
	
	        case "TERCER_BANDO", "NEUTRO" -> switch (rol) {
	            case "Criatura" -> new Color(210, 180, 140); // Marrón claro / Tierra
	            case "Líder"    -> new Color(230, 190, 80);  // Dorado
	            default         -> new Color(200, 200, 100); // Amarillo verdoso
	        };
	
	        default -> Color.GRAY;
	    };
	}
	
    public static char getSimbolo(IObservable e) {

        return switch (e.getNombreBando().toUpperCase()) {
            case "MAGO" -> switch (e.getNombreClase()) {
                case "Auror"      -> 'A';
                case "Profesor"   -> 'P';
                case "Estudiante" -> 'E';
                default           -> 'M'; // Mago Raso / Base
            };
            case "MORTIFAGO" -> switch (e.getNombreClase()) {
                case "Comandante" -> 'C';
                case "Lacayo"     -> 'L';
                default           -> 'W'; // Mortifago Raso / Base
            };
            case "NEUTRO", "CRIATURA" -> 'X';
            default -> '?';
        };
    }
}
