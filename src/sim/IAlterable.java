package sim;

public interface IAlterable extends IAfectable {
    void reducirMovimiento(int duracion);
    void restaurarMovimiento(int duracion);
    
    void setBloqueaDanio(boolean estado);
    void setBloqueaMovimiento(boolean estado);
    void setBloqueaHechizos(boolean estado);
    void setBloqueaAccion(boolean estado);
    
    boolean getBloqueaDanio();
    boolean getBloqueaMovimiento();
    boolean getBloqueaHechizos();
    boolean getBloqueaAccion();

    boolean puedeActuar(); 
}