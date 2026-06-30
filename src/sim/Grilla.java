package sim;

import java.util.ArrayList;
import java.util.List;

public class Grilla implements IGrillaConsulta {
	private final int ancho;
    private final int alto;
    private final Entidad[][] celdas;

    public Grilla(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new Entidad[ancho][alto];
    }

    // helpers
    private int[] buscarCeldaLibreAdyacente(int x, int y) {
        // Definimos los movimientos para mirar: Abajo, Arriba, Derecha, Izquierda
        int[] frentesX = {0,  0, 1, -1};
        int[] frentesY = {1, -1, 0,  0};

        for (int i = 0; i < 4; i++) {
            int nuevoX = x + frentesX[i];
            int nuevoY = y + frentesY[i];

            if (!estaFueraDeGrilla(nuevoX, nuevoY)) {
                if (celdas[nuevoX][nuevoY] == null) {
                	return new int[]{nuevoX, nuevoY};
                }
            }
        }
        
        return null;
    }
    
    private List<int[]> bresenham(int x0, int y0, int x1, int y1) {
        List<int[]> path = new ArrayList<>();
        
        int dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int x = x0, y = y0;
        
        while (true) {
            path.add(new int[]{x, y});
            if (x == x1 && y == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 <  dx) { err += dx; y += sy; }
        }
        
        return path;
    }
    
    //mutadores
    public boolean agregarEntidad(Entidad  e, int posX, int posY) {
        if (estaFueraDeGrilla(posX, posY)) return false;
        
        if (celdas[posX][posY] != null) { //celda ocupada
            int[] nuevaPosicion = buscarCeldaLibreAdyacente(posX, posY);
            
            if (nuevaPosicion == null) return false;

            posX = nuevaPosicion[0];
            posY = nuevaPosicion[1];
        }

        e.setPosicion(posX, posY);
        celdas[posX][posY] = e;
        
        return true;
    }

    public boolean moverEntidad(Entidad  e, int nuevoX, int nuevoY) {
    	if (estaFueraDeGrilla(nuevoX, nuevoY)) return false;
    	if (celdas[nuevoX][nuevoY] != null) return false;
    	
    	celdas[e.getPosX()][e.getPosY()] = null;
        celdas[nuevoX][nuevoY] = e;
        e.setPosicion(nuevoX, nuevoY);
        
        return true;
    }

    public boolean quitarEntidad(Entidad  e) { 	
    	if (e == null) return false;
    	
        celdas[e.getPosX()][e.getPosY()] = null;
        
        return true;
    }

    // consultores
    public Entidad resolverLineaDeTiro(Entidad atacante, Entidad objetivo, int alcance) {
        if (calcularDistancia(atacante, objetivo) > alcance) return null;

        List<int[]> path = bresenham(
            atacante.getPosX(), atacante.getPosY(),
            objetivo.getPosX(), objetivo.getPosY()
        );

        for (int[] celda : path) {
            int cx = celda[0], cy = celda[1];
            if (cx == atacante.getPosX() && cy == atacante.getPosY()) continue;
            Entidad en = celdas[cx][cy];
            if (en != null) return en;
        }
        return objetivo;
    }
    public int calcularDistancia(Entidad a, Entidad b) {
        return calcularDistancia(a.getPosX(), a.getPosY(), b.getPosX(), b.getPosY());
    }
    public Entidad getEntidad(int x, int y) { return celdas[x][y]; }   
    
    //interfaz
    public int getAlto()  { return alto; }
    public int getAncho() { return ancho; }
    public boolean estaLibre(int x, int y)  { return celdas[x][y] == null; }
    public boolean estaFueraDeGrilla(int x, int y) { return x < 0 || x >= ancho || y < 0 || y >= alto; }
    public int calcularDistancia(int x1, int y1, int x2, int y2) {//distanciaChebyshev 
        return Math.max(
            Math.abs(x1 - x2),
            Math.abs(y1 - y2)
        );
    }
}
