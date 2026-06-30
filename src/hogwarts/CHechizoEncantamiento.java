package hogwarts;
import sim.IEstrategiaHabilidad;
import sim.ITurnable;

import java.util.Set;


public final class CHechizoEncantamiento extends CHechizo {
	private final ETipoEncantamiento tipoEncantamiento;

	public CHechizoEncantamiento(String nombre, String descr, ENivel nivelRequerido, EAfinidad afinidad, int coste, int distancia,
			                     Set<EFaccion> faccionesPermitidas, int valorEspecial, IEstrategiaHabilidad estrategia,
								 ETipoEncantamiento tipoEncantamiento)
	{
		super(nombre, descr, nivelRequerido, afinidad, coste, distancia, faccionesPermitidas, valorEspecial, estrategia, tipoEncantamiento.getTipoHabilidad());
		this.tipoEncantamiento = tipoEncantamiento;
	}	
	
	@Override   
    public CHechizoEncantamiento clonar() { return (CHechizoEncantamiento) super.clonar(); }
	
	@Override
	protected String getMasDetallesEspecificos() { return "Tipo Encatamiento:   " + tipoEncantamiento + "\n"; }
	
	//interfaz
	@Override
	public ITurnable getComportamiento() { return tipoEncantamiento; }
}
