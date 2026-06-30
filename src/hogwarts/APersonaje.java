package hogwarts;
import sim.ICasteable;
import sim.IInstanciable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import app.FLibroDeHechizos;

import java.util.Map;


public abstract class APersonaje implements Cloneable, IInstanciable {
    private final String nombre;
    private final String descripcion;
    private final ENivel nivelInicial;
    private final EAfinidad afinidad;
    private final EFaccion faccion;
    private final int puntosSalud;
	private final int puntosMana;
    private final int movimiento;
    private final Map<FLibroDeHechizos, CHechizo> hechizosDisponibles;

    protected APersonaje(String nombre, String descr, ENivel nivelInicial, EAfinidad afinidad, EFaccion faccion, int puntosSalud, int puntosMana, int movimiento, Set<FLibroDeHechizos> hechizos) {
        this.nombre = nombre;
        this.descripcion = descr;
        this.nivelInicial = nivelInicial;
        this.afinidad = afinidad;
        this.faccion = faccion;
        this.puntosSalud = puntosSalud;
        this.puntosMana = puntosMana;
        this.movimiento = movimiento;
        
        this.hechizosDisponibles = new HashMap<>();
        for (FLibroDeHechizos h : hechizos) {
        	aprenderHechizo(h);
        }
    }

    public APersonaje clonar() {
        try { return (APersonaje) super.clone(); } 
        catch (CloneNotSupportedException e) { throw new AssertionError(); }
    }
    

    
    private String datosDePersonaje() {
    	StringBuilder sb = new StringBuilder();
    	
        sb.append("Afinidad:  ").append(afinidad).append("\n");
        sb.append("Nivel:     ").append(nivelInicial).append("\n");
    	sb.append("Hechizos:  ");

        boolean primero = true;
        for (AHabilidad h : hechizosDisponibles.values()) {
            if (!primero) sb.append(",\n\t   ");            

            String nombreFormateado = h.getNombre().toUpperCase().replace(" ", "_");
            sb.append(nombreFormateado);
            
            primero = false;
        }
        
    	return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Nombre:    ").append(nombre).append("\n");
        sb.append("Nivel:     ").append(nivelInicial).append("\n");
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
    protected abstract double getMultiplicadorPropio(ETipoHabilidad tipo);
    public EFaccion getFaccion()    { return faccion; }
	public ENivel getNivelInicial() { return nivelInicial; }
	public EAfinidad getAfinidad()  { return afinidad; }
    
    //helper para los hijos, necesitar ser static para llamarlo en super()
	protected static Set<FLibroDeHechizos> combinarHechizos(FLibroDeHechizos[] raciales, FLibroDeHechizos[] particulares) {
	    Set<FLibroDeHechizos> todos = new HashSet<>();
	    todos.addAll(Arrays.asList(raciales));
	    todos.addAll(Arrays.asList(particulares));
	    return todos;
	}

	public void aprenderHechizo(FLibroDeHechizos h) {
	    if (hechizosDisponibles.containsKey(h)) {
	        System.out.println("ADVERTENIA: El personaje ya conoce el hechizo: " + h);
	        return;
	    }
	    if (!h.getFacciones().contains(faccion)) {
	    	System.out.println("ADVERTENIA: El hechizo " + h + " no puede ser usado por " + faccion);
	    	return;
	    }
	    if (h.getNivelRequerido().ordinal() > nivelInicial.ordinal()) {
	    	System.out.println("ADVERTENIA: El hechizo " + h + " requiere mayor nivel");
	    	return;
	    }
	    
	    CHechizo hechizo = h.construir();
	    if (hechizo.getAfinidad() == afinidad) 
	    	hechizo.setBonificacion(getMultiplicadorPropio(hechizo.getTipoHabilidad())); 
	    
	    hechizosDisponibles.put(h, hechizo);
	}
}
