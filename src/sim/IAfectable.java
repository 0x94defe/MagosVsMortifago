package sim;

import hogwarts.AHabilidad;
import hogwarts.ETipoEncantamiento;

public interface IAfectable {
	void descontarMana(int cantidad);
    void recibirDanio(int cantidad);
    void recibirCuracion(int cantidad);
    void recibirEfecto(ETipoEncantamiento efecto, int duracion);
    AHabilidad getHabilidadPersonal(AHabilidad habilidad);
}
