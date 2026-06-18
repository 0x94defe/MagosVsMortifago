package sim;

public interface IAfectable {
	void descontarMana(int cantidad);
    void recibirDanio(int cantidad);
    void recibirCuracion(int cantidad);
    void recibirEfecto(ITurnable efecto, int duracion);
    
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
    
    ICasteable getHabilidadPersonal(ICasteable habilidad);
}
