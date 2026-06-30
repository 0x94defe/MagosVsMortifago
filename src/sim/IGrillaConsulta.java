package sim;

public interface IGrillaConsulta {
    int getAncho();
    int getAlto();
    IObservable getEntidad(int x, int y);
    int calcularDistancia(int x1, int y1, int x2, int y2);
    
    boolean estaFueraDeGrilla(int x, int y);
    boolean estaLibre(int x, int y);
}
