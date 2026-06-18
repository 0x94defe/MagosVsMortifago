package hogwarts;
import sim.IAfectable;
import sim.ITurnable;

import java.util.Set;
import java.util.function.BiConsumer;


public class CHechizo extends AHabilidad implements Cloneable {
    private final ETipoHabilidad tipoHechizo;
    private int valorEspecial;
    private BiConsumer<IAfectable, IAfectable> estrategia;
    
    
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
    protected String getMasDetallesEspecificos() {	return ""; }

    public String getDetallesEspecificos() {
        StringBuilder sb = new StringBuilder();

        sb.append("Tipo Hechizo: ").append(tipoHechizo).append("\n");
        sb.append(getMasDetallesEspecificos());
        sb.append("Valor:       ").append(valorEspecial).append(" " + tipoHechizo.getStat() + "\n");
        
        return sb.toString();
    }
    public void ejecutar(IAfectable lanzador, IAfectable objetivo) { estrategia.accept(lanzador, objetivo); }
    public String getTipoHabilidad() { return "Hechizo"; }
	public boolean esHabilidadOfensiva() { return tipoHechizo.esOfensivo(); }
	public boolean esHabilidadTurnable() { return tipoHechizo.esTurnado(); }
	public boolean esHabilidadEspecial() { return tipoHechizo.esEspecial(); }
    public int getValorEspecifico() { return valorEspecial; }
    public void setValorEspecifico(int nuevoValor) { valorEspecial = nuevoValor; }
    public void setNuevoComportamiento(BiConsumer<IAfectable, IAfectable> nuevaEstrategia) { estrategia = nuevaEstrategia; }
	public ITurnable getComportamiento() { return null;	}
}
