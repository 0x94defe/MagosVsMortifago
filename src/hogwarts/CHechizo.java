package hogwarts;
import sim.IAfectable;

import java.util.Set;
import java.util.function.BiConsumer;


public class CHechizo extends AHabilidad implements Cloneable {
    public final ETipoHabilidad tipoHechizo;
    private int valorEspecial;
    private final BiConsumer<IAfectable, IAfectable> estrategia;
    
    
    public CHechizo(
    		String nombre, String descr, int coste, int distancia, Set<EFaccion> faccionesPermitidas,
    		ETipoHabilidad tipoHechizo, int valorEspecial, BiConsumer<IAfectable, IAfectable> estrategia
    ) {
        super(nombre, descr, coste, distancia, faccionesPermitidas);
        this.tipoHechizo = tipoHechizo;
        this.valorEspecial = valorEspecial;
        this.estrategia = estrategia;
    }

    public CHechizo clonar() {
        try { return (CHechizo) super.clone(); } 
        catch (CloneNotSupportedException e) { throw new AssertionError(); }
    }
    
    
    @Override
    public String getDetallesEspecificos() {
        StringBuilder sb = new StringBuilder();

        sb.append("Tipo Hechizo: ").append(tipoHechizo).append("\n");
        sb.append(getMasDetallesEspecificos());
        sb.append("Valor:       ").append(valorEspecial).append(" " + tipoHechizo.getStat() + "\n");
        
        return sb.toString();
    }
    @Override
    public void ejecutar(IAfectable lanzador, IAfectable objetivo) {
    	estrategia.accept(lanzador, objetivo);
    }
    
    protected String getMasDetallesEspecificos() {
		return "";
	}
    
    public int getValorEspecial() {
    	return valorEspecial;
    }

	@Override
	public boolean esHabilidadOfensiva() { return tipoHechizo.esOfensivo(); }
	@Override
	public boolean esHabilidadEspecial() { return tipoHechizo.esEspecial(); }
}
