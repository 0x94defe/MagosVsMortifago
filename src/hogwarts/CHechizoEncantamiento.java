package hogwarts;
import sim.IAfectable;
import sim.ITurnable;

import java.util.Set;
import java.util.function.BiConsumer;


public final class CHechizoEncantamiento extends CHechizo {
	private final ETipoEncantamiento tipoEncantamiento;

	public CHechizoEncantamiento(
			String nombre, String descr, int coste, int distancia, Set<EFaccion> faccionesPermitidas,
			ETipoEncantamiento tipoEncantamiento, int valorEspecial, BiConsumer<IAfectable, IAfectable> estrategia
	) {
		super(nombre, descr, coste, distancia, faccionesPermitidas, tipoEncantamiento.getTipoHabilidad(), valorEspecial, estrategia);
		this.tipoEncantamiento = tipoEncantamiento;
	}	
	
	@Override   
    public CHechizoEncantamiento clonar() { return (CHechizoEncantamiento) super.clonar(); }
	
	@Override
	protected String getMasDetallesEspecificos() { return "Tipo Encatamiento:   " + tipoEncantamiento + "\n"; }
	
	@Override
	public ITurnable getComportamiento() { return tipoEncantamiento; }
}
