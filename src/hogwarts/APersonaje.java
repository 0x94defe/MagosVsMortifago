package hogwarts;
import sim.ICasteable;
import sim.IInstanciable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;


public abstract class APersonaje implements Cloneable, IInstanciable {
    private final String nombre;
    private final String descripcion;
    private final EFaccion faccion;
    private final int puntosSalud;
	private final int puntosMana;
    private final int movimiento;
    private final Map<FLibroDeHechizos, CHechizo> hechizosDisponibles;

    protected APersonaje(String nombre, String descr, EFaccion faccion, int puntosSalud, int puntosMana, int movimiento, Map<FLibroDeHechizos, CHechizo> hechizos) {
        this.nombre = nombre;
        this.descripcion = descr;
        this.faccion = faccion;
        this.puntosSalud = puntosSalud;
        this.puntosMana = puntosMana;
        this.movimiento = movimiento;
        this.hechizosDisponibles = hechizos;
    }

    public APersonaje clonar() {
        try { return (APersonaje) super.clone(); } 
        catch (CloneNotSupportedException e) { throw new AssertionError(); }
    }
    
    
    private String datosDePersonaje() {
    	StringBuilder sb = new StringBuilder();
    	
        sb.append("Hechizos:  ").append(
            	Arrays.stream(this.getHechizosDisponibles())
            	      .map(h -> h.getNombre().toUpperCase().replace(" ", "_"))
            	      .collect(Collectors.joining(",\n\t   ")));
        
    	return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Nombre:    ").append(nombre).append("\n");
        sb.append("Faccion:   ").append(faccion).append("\n");
        sb.append("Clase:     ").append(this.getNombreClase()).append("\n");
        sb.append("Descripcion: ").append("\n\t   ");
        sb.append(descripcion.replaceAll("(.{1,21})(?:\\s+|$)", "$1\n\t   ").trim()).append("\n");
        sb.append("-- Estadisticas --").append("\n");
        sb.append("Salud:     ").append(puntosSalud).append(" HP\n");
        sb.append("Maná:      ").append(puntosMana).append(" HP\n");
        sb.append("Movimiento: ").append(movimiento).append(" TILE\n");
        sb.append(datosDePersonaje());
        
        return sb.toString();
    }
    
    // interfaze instnciable
	public String getNombre()      { return nombre; }
	public String getDescripcion() { return descripcion; }
    public String getNombreFaccion() { return faccion.toString(); }
    @Override public abstract String getNombreClase();
	public String getDatosEspecificos() { return datosDePersonaje(); }
	public int getPuntosSalud()    { return puntosSalud; }
	public int getPuntosRecurso()  { return puntosMana; }
	public int getMovimiento()      { return movimiento; }
    public Map<Integer, ICasteable> getHabilidades() {
        Map<Integer, ICasteable> mapaFormateado = new HashMap<>();
        
        for (Map.Entry<FLibroDeHechizos, CHechizo> entrada : hechizosDisponibles.entrySet()) {
            int idNumerico = entrada.getKey().name().toUpperCase().hashCode();
            mapaFormateado.put(idNumerico, entrada.getValue());
        }
        
        return mapaFormateado;
    }
    public boolean puedeActuar() { return true; }
    public boolean puedeMoverse() { return true; }
      
    // Getters
    public CHechizo[] getHechizosDisponibles() { return hechizosDisponibles.values().toArray(new CHechizo[0]); }
    public EFaccion getFaccion() { return faccion; }
    
    //helper para los hijos, necesitar ser static para llamarlo en super()
    protected static Map<FLibroDeHechizos, CHechizo> construirHechizos(EFaccion faccion, FLibroDeHechizos[] a, FLibroDeHechizos[] b) {
    	Set<FLibroDeHechizos> todoJunto = new HashSet<>();
    	todoJunto.addAll(Arrays.asList(a));
    	todoJunto.addAll(Arrays.asList(b));

        Map<FLibroDeHechizos, CHechizo> hechizosDisponibles = new HashMap<>();
        for (FLibroDeHechizos h : todoJunto) {
            if (!h.getFacciones().contains(faccion)) {
                throw new IllegalArgumentException(
                    "El hechizo " + h + " no puede ser usado por " + faccion
                );
            }
            
            hechizosDisponibles.put(h, h.construir());
        }
        
        return hechizosDisponibles;
    }
}
