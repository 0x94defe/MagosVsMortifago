package hogwarts;
import sim.IAfectable;
import sim.ICasteable;
import sim.IEstrategiaHabilidad;
import sim.ITurnable;

import java.util.Set;


public abstract class AHabilidad implements ICasteable {
	private final String nombre;
	private final String descripcion;
	private ENivel nivelRequerido;
	private EAfinidad afinidad;
    private int coste;
    private int distancia;
    private final Set<EFaccion> faccionesPermitidas;
    private int valorEspecial;
    private IEstrategiaHabilidad estrategia;

    protected AHabilidad(String nombre, String descr, ENivel nivelRequerido, EAfinidad afinidad, int coste, int distancia,
    		             Set<EFaccion> faccionesPermitidas, int valorEspecial, IEstrategiaHabilidad estrategia)
    {
        this.nombre = nombre;
		this.descripcion = descr;
		this.nivelRequerido = nivelRequerido;
		this.afinidad = afinidad;
        this.coste = coste;
        this.distancia = distancia;
        this.faccionesPermitidas = faccionesPermitidas;
        this.valorEspecial = valorEspecial;
        this.estrategia = estrategia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Habilidad:    ").append(nombre).append("\n");
        sb.append("Nivel req:    ").append(nivelRequerido).append("\n");
        sb.append("Afinidad:     ").append(afinidad).append("\n");
        sb.append("Tipo:         ").append(getNombreHabilidad()).append("\n");
        sb.append("Faccion:      ").append(faccionesPermitidas).append("\n");
        sb.append("Descripcion:  ").append("\n\t   ");
        sb.append(descripcion.replaceAll("(.{1,21})(?:\\s+|$)", "$1\n\t   ").trim()).append("\n");
        sb.append("-- Estadisticas --").append("\n");
        sb.append("Coste MP:    ").append(coste).append(" MP\n");
        sb.append("Alcance:     ").append(distancia).append(" TILE\n");
        sb.append("Valor:       ").append(valorEspecial);
        sb.append(getDetallesEspecificos());

        return sb.toString();
    }
    public abstract String getDetallesEspecificos();
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AHabilidad)) return false;
        AHabilidad otro = (AHabilidad) o;
        return this.getNombre().equals(otro.getNombre());
    }
    @Override
    public int hashCode() {
        return getNombre().hashCode();
    }
    
    // interfaze casteable
    public String getNombre()       { return nombre; }
    public abstract String getNombreHabilidad();
	public int getCosteRecurso()    { return coste; }
	public int getDistanciaAtaque() { return distancia; }
    public int getValorEspecifico() { return valorEspecial; }

    public abstract ITurnable getComportamiento();
    public void ejecutar(IAfectable lanzador, IAfectable objetivo) { estrategia.emplearEstrategia(lanzador, objetivo, this); }
    
    public void setValorEspecifico(int nuevoValor) { valorEspecial = nuevoValor; }
	public void setBonificacion(double proporcion) { valorEspecial *= proporcion; }
    public void setNuevoComportamiento(IEstrategiaHabilidad nuevaEstrategia) { estrategia = nuevaEstrategia; }

    public abstract boolean esHabilidadOfensiva();
	public abstract boolean esHabilidadTurnable();
    public abstract boolean esHabilidadEspecial();

    
    // getters
    public Set<EFaccion> getFacciones() { return Set.copyOf(faccionesPermitidas); }
    public ENivel getNivelRequerido()   { return nivelRequerido; }
    public EAfinidad getAfinidad()      { return afinidad; }
}
