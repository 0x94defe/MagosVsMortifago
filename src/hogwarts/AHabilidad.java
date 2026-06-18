package hogwarts;
import sim.IAfectable;
import sim.ICasteable;

import java.util.Set;


public abstract class AHabilidad implements ICasteable {
	private final String nombre;
	private final String descripcion;
    private final int coste;
    private final int distancia;
    private final Set<EFaccion> faccionesPermitidas;

    protected AHabilidad(String nombre, String descr, int coste, int distancia, Set<EFaccion> faccionesPermitidas) {
        this.nombre = nombre;
		this.descripcion = descr;
        this.coste = coste;
        this.distancia = distancia;
        this.faccionesPermitidas = faccionesPermitidas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Habilidad:    ").append(nombre).append("\n");
        sb.append("Tipo:         ").append(getTipoHabilidad()).append("\n");
        sb.append("Faccion:      ").append(faccionesPermitidas).append("\n");
        sb.append("Descripcion:  ").append("\n\t   ");
        sb.append(descripcion.replaceAll("(.{1,21})(?:\\s+|$)", "$1\n\t   ").trim()).append("\n");
        sb.append("-- Estadisticas --").append("\n");
        sb.append(getDetallesEspecificos());
        sb.append("Coste MP:    ").append(coste).append(" MP\n");
        sb.append("Alcance:     ").append(distancia).append(" TILE");

        return sb.toString();
    }
    public abstract String getDetallesEspecificos();
    
    // interfaze casteable
    public String getNombre() { return nombre; }
	public int getCosteRecurso() { return coste; }
	public int getDistanciaAtaque() { return distancia; }
    @Override
    public abstract String getTipoHabilidad();
    @Override
    public abstract void ejecutar(IAfectable lanzador, IAfectable objetivo);
    @Override
    public abstract boolean esHabilidadOfensiva();
    @Override 
	public abstract boolean esHabilidadTurnable();
    @Override
    public abstract boolean esHabilidadEspecial();
    
    // getters
    public Set<EFaccion> getFacciones() { return Set.copyOf(faccionesPermitidas); }
}
