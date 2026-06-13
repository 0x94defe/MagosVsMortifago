package hogwarts;

import sim.IAfectable;

public interface Hechizo {
    String getNombre();
    int getCosteRecurso();
    int getDistanciaAtaque();
    void ejecutar(IAfectable lanzador, IAfectable objetivo);
    boolean esHabilidadOfensiva();
    boolean esHabilidadEspecial();
}
