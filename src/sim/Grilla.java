package sim;


public class Grilla {
	private final int ancho;
    private final int alto;
    private final Entidad[][] celdas;

    public Grilla(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new Entidad[ancho][alto];
    }

    public Entidad agregarEntidad(IInstanciable i, RBandoToken b, RCoordenada c) {
    	if (estaFueraDeGrilla(c.posX(), c.posY())) return null;
    	
        if (celdas[c.posX()][c.posY()] != null) { //la celda esta ocupada
            RCoordenada nuevaPosicion = buscarCeldaLibreAdyacente(c.posX(), c.posY());
            
            if (nuevaPosicion == null) {
                System.out.println("No hay espacio para spawnear a " + i.getNombre());
                return null; 
            }

            c = nuevaPosicion;
        }
        
        Entidad e = new Entidad(i, b, c);
        celdas[c.posX()][c.posY()] = e;
        
        return e;
    }

    public boolean moverEntidad(Entidad e, int nuevoX, int nuevoY) {
    	if (estaFueraDeGrilla(nuevoX, nuevoY)) return false;
    	if (celdas[nuevoX][nuevoY] != null) return false;
    	
    	celdas[e.getPosX()][e.getPosY()] = null;
        celdas[nuevoX][nuevoY] = e;
        e.setPosicion(nuevoX, nuevoY);
        
        return true;
    }

    public boolean quitarEntidad(Entidad e) { 	
    	if (e == null) return false;
    	
        celdas[e.getPosX()][e.getPosY()] = null;
        
        return true;
    }


    public Entidad getEntidad(int x, int y) { return celdas[x][y]; }
    public boolean estaLibre(int x, int y)  { return celdas[x][y] == null; }
    public int getAlto() { return alto; }
    public int getAncho() { return ancho; }
    
    public boolean estaFueraDeGrilla(int x, int y) {
    	return x < 0 || x >= ancho || y < 0 || y >= alto;
    }
    private RCoordenada buscarCeldaLibreAdyacente(int x, int y) {
        // Definimos los movimientos para mirar: Abajo, Arriba, Derecha, Izquierda
        int[] frentesX = {0,  0, 1, -1};
        int[] frentesY = {1, -1, 0,  0};

        for (int i = 0; i < 4; i++) {
            int nuevoX = x + frentesX[i];
            int nuevoY = y + frentesY[i];

            if (!estaFueraDeGrilla(nuevoX, nuevoY)) {
                if (celdas[nuevoX][nuevoY] == null) {
                    return new RCoordenada(nuevoX, nuevoY);
                }
            }
        }
        
        return null;
    }
}
