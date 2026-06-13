package render;
import hogwarts.*;
import sim.Entidad;

import java.awt.Color;


public class PersonajeRender {
    public static Color getColor(Entidad p) {
    	if (p.getInstanciable() instanceof CMagoAuror){
             return new Color(90, 160, 250);   // Azul sutil
        }
    	if (p.getInstanciable() instanceof CMagoProfesor) {
            return new Color(100, 220, 200);  // Cian menta
        }
    	if (p.getInstanciable() instanceof CMagoEstudiante) {
            return new Color(120, 210, 120);  // Verde claro
        }
    	if (p.getInstanciable() instanceof CMortifagoComandante) {
            return new Color(240, 100, 100);  // Rojo carmesí
        }
    	if (p.getInstanciable() instanceof CMortifagoLacayo) {
            return new Color(240, 160, 80);   // Naranja/Ámbar
        }

    	// Héroes genéricos 
    	if (p.getInstanciable() instanceof CPersonajeMago) {
            return new Color(190, 130, 240);  // Púrpura mágico
        }

    	if (p.getInstanciable() instanceof CPersonajeMortifago) {
            return new Color(190, 130, 240);  // Púrpura mágico
        }
        return Color.GRAY;
    }

    public static char getSimbolo(Entidad p) {
        if (p.getInstanciable() instanceof CPersonajeMago){
            return 'M';
        }   

        if (p.getInstanciable() instanceof CPersonajeMortifago) {
            return 'X';
        }

        return '?';
    }
}
