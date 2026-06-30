package sim;

public interface IAfectable {
    void descontarMana(int cantidad);
    void recibirDanio(int cantidad);
    void recibirCuracion(int cantidad);
    void recibirMana(int cantidad);
    void recibirEfecto(ITurnable efecto, int duracion);    

    
    boolean estaVivo(); 
}
