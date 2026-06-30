package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Entidad implements IObservable , IAlterable {
	private final IInstanciable ref;
    private RBandoToken bando;
    private int posX;
    private int posY;
    private int puntosSalud;
    private int puntosRecurso;
    private int movimiento;
    private Map<Integer, ICasteable> habilidadesDisponibles;
    private List<EfectoActivo> efectosActivos;
    private boolean bloqueaDanio;
    private boolean bloqueaMovimiento;
    private boolean bloqueaHechizos;
    private boolean bloqueaAccion;
    
    public Entidad(IInstanciable ref, RBandoToken bando) {
        this.ref = ref;
        this.bando = bando;
        this.posX = 0;
        this.posY = 0;
    	this.puntosSalud = ref.getPuntosSalud();
        this.puntosRecurso = ref.getPuntosRecurso();
        this.movimiento = ref.getMovimiento();
        this.habilidadesDisponibles = ref.getHabilidades();
        this.efectosActivos = new ArrayList<>();
        this.bloqueaDanio = false;
        this.bloqueaMovimiento = false;
        this.bloqueaHechizos = false;
        this.bloqueaAccion = false;
    }  
    
    public REntidadSnapshot crearSnapshot() {
        return new REntidadSnapshot(
        	posX,
        	posY,
            puntosSalud,
            puntosRecurso,
            movimiento,
            bloqueaDanio,
            bloqueaMovimiento,
            bloqueaHechizos,
            bloqueaAccion,
            this.getEstado()
        );
    }
    
    
    public void aprenderHabilidad(ICasteable habilidad) {
        int idNumerico = habilidad.getNombre().toUpperCase().hashCode();
        habilidadesDisponibles.put(idNumerico, habilidad);
    }
    
    public boolean usarHabilidad(ICasteable habilidad, IAfectable objetivo) {	
    	int calc = this.puntosRecurso - habilidad.getCosteRecurso();
    	if (calc < 0) return false;
    	this.puntosRecurso = calc;
    	getHabilidadPersonal(habilidad).ejecutar(this, objetivo);
        return true;
    }

    public boolean tieneEfectoQueRestringeAccion() {
        return bloqueaAccion;
    }
        
        
    //interfaz afectable
    public void descontarMana(int cantidad) { puntosRecurso = Math.max(0, puntosRecurso - cantidad); }
    public void recibirDanio(int cantidad)  { 
    	if (bloqueaDanio) { bloqueaDanio = false; return;}
    	//puntosSalud = Math.max(0, puntosSalud - cantidad);
    	puntosSalud -= cantidad;
    }
    public void recibirMana(int cantidad) {	puntosRecurso = Math.min(puntosRecurso + cantidad, ref.getPuntosRecurso()); }
    public void recibirCuracion(int cantidad) {	puntosSalud = Math.min(puntosSalud + cantidad, ref.getPuntosSalud()); }
    public void recibirEfecto(ITurnable comportamiento, int duracion) { 
        EfectoActivo efecto = new EfectoActivo(comportamiento, duracion);
        efecto.iniciar(this);
        efectosActivos.add(efecto);
    }
	public ICasteable getHabilidadPersonal(ICasteable habilidad) {
		int idNumerico = habilidad.getNombre().trim().toUpperCase().replace(" ", "_").hashCode();
		ICasteable habilidadDeLaEntidad = habilidadesDisponibles.get(idNumerico);
		if (habilidadDeLaEntidad == null) throw new IllegalStateException("No existe habilidad: '" + habilidad + "' para la entidad: '" + ref.getNombre() + "'");
		return habilidadDeLaEntidad;
	}
	
	
	public void reducirMovimiento(int duracion)   {	movimiento = Math.max(0, movimiento - duracion); }
	public void restaurarMovimiento(int duracion) {	movimiento = Math.min(movimiento + duracion, ref.getMovimiento()); }
	public void setBloqueaDanio(boolean estado)      { bloqueaDanio = estado; }
	public void setBloqueaMovimiento(boolean estado) { bloqueaMovimiento  = estado; }
	public void setBloqueaHechizos(boolean estado)   { bloqueaHechizos = estado; }
	public void setBloqueaAccion(boolean estado)     { bloqueaAccion = estado; }
	public boolean getBloqueaDanio()      { return bloqueaDanio; }
	public boolean getBloqueaMovimiento() { return bloqueaMovimiento; }
	public boolean getBloqueaHechizos()   { return bloqueaHechizos; }
	public boolean getBloqueaAccion()     { return bloqueaAccion; }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Nombre:    ").append(ref.getNombre()).append("\n");
        sb.append("Bando:     ").append(bando.nombre()).append("\n");
        sb.append("Estado:    ").append(getEstado()).append("\n");
        sb.append("Salud:     ").append(puntosSalud).append(" / ").append(ref.getPuntosSalud()).append(" HP\n");
        sb.append("Maná:      ").append(puntosRecurso).append(" / ").append(ref.getPuntosRecurso()).append(" MP\n");
        sb.append("Movimiento: ").append(movimiento).append(" / ").append(ref.getMovimiento()).append(" TILE\n");
        sb.append("Efectos Activos: ").append(efectosActivos.size()).append("\n").append(efectosActivos.toString()).append("\n");
        sb.append("-- Info Adicional --").append("\n");
        sb.append("Faccion:   ").append(ref.getNombreFaccion()).append("\n");
        sb.append("Clase:     ").append(ref.getNombreClase()).append("\n");
        sb.append("Descripcion: ").append("\n\t   ");
        sb.append(ref.getDescripcion().replaceAll("(.{1,21})(?:\\s+|$)", "$1\n\t   ").trim()).append("\n");
        sb.append(ref.getDatosEspecificos());
        
        return sb.toString();
    }
    
    public IInstanciable getInstanciable() { return ref; }
    public String getNombre()      { return ref.getNombre(); }
    public int getIdBando() 	   { return bando.id(); }
    public String getNombreBando() { return bando.nombre(); }
    public int getMovimiento() 	   { return movimiento; }
    public int getPuntosMana() 	   { return puntosRecurso; }
    public int getPuntosSalud()    { return puntosSalud; }
    public List<ICasteable> getHabilidadesDisponibles() { return new ArrayList<>(habilidadesDisponibles.values()); }
    public List<EfectoActivo> getEfectosActivos()       { return efectosActivos; }
    
    public boolean estaVivo()      { return puntosSalud > 0; }
    public boolean puedeActuar()   { return ref.puedeActuar(); }
    public boolean puedeMoverse()  { return ref.puedeMoverse(); }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public EEstadoEntidad getEstado() { return EEstadoEntidad.evaluarEstado(this); }
    
    public void setPosicion(int nuevoPosX, int nuevoPosY) { posX = nuevoPosX; posY = nuevoPosY; }


	public String getNombreClase() { return ref.getNombreClase(); }
	public String getNombreFaccion() { return ref.getNombreFaccion(); }
}