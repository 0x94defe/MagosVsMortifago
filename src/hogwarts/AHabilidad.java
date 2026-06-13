package hogwarts;
import sim.IAfectable;

import java.util.Set;


public abstract class AHabilidad {
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
        
        sb.append("Hechizo:      ").append(nombre).append("\n");
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
    public abstract void ejecutar(IAfectable lanzador, IAfectable objetivo);
    public abstract boolean esHabilidadOfensiva();
    public abstract boolean esHabilidadEspecial();
    
    public String getNombre() { return nombre; }
	public int getCosteRecurso() { return coste; }
	public int getDistanciaAtaque() { return distancia; }
	public Set<EFaccion> getFaccionesPermitidas() { return Set.copyOf(faccionesPermitidas); }
}
