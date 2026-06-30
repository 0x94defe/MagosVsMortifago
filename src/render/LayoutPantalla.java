package render;

import java.awt.*;


public class LayoutPantalla {

    private LayoutPantalla() {}

    private static final Rectangle PANTALLA = 
    	GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

    public static int getAncho() { return (int) PANTALLA.getWidth(); }
    public static int getAlto()  { return (int) PANTALLA.getHeight(); }
    public static int getX()     { return (int) PANTALLA.getX(); }
    public static int getY()     { return (int) PANTALLA.getY(); }

    public static void posicionarMenu(Window w) {
        int ancho = getAncho() / 4;
        w.setSize(ancho, getAlto());
        w.setLocation(getX(), getY());
    }

    public static void posicionarInfo(Window w) {
        int ancho = getAncho() / 4;
        w.setSize(ancho, getAlto());
        w.setLocation(getX() + getAncho() - ancho, getY());
    }

    public static void posicionarJuego(Window w) {
        int margen = getAncho() / 4;
        w.setSize(getAncho() - margen * 2, getAlto());
        w.setLocation(getX() + margen, getY());
        w.setMinimumSize(new Dimension(600, 500));
    }
}
