package sim;

public interface ITurnable {
    void alIniciar(IAlterable objetivo);
    void alConsumirse(IAlterable entidad, int turnosRestantes);
    void alTerminar(IAlterable objetivo);
    boolean debeTerminar(IAlterable objetivo);
    boolean restringeAccion();
    String getDescripcionEfecto();
}
