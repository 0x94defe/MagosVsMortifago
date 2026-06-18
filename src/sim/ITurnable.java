package sim;

public interface ITurnable {
    void alIniciar(IAfectable objetivo);
    void alConsumirse(IAfectable entidad, int turnosRestantes);
    void alTerminar(IAfectable objetivo);
    boolean debeTerminar(IAfectable objetivo);
    boolean restringeAccion();
    String getDescripcionEfecto();
}
