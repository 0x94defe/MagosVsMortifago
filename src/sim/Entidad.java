package sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Entidad implements IAfectable {
	private final IInstanciable ref;
    private RBandoToken bando;
    private RCoordenada coor;
    private int puntosSalud;
    private int puntosRecurso;
    private int movimiento;
    private Map<Integer, ICasteable> habilidadesDisponibles;
    private List<EfectoActivo> efectosActivos;
    private boolean bloqueaDanio;
    private boolean bloqueaMovimiento;
    private boolean bloqueaHechizos;
    private boolean bloqueaAccion;

    
    public Entidad(IInstanciable ref, RBandoToken bando, RCoordenada coor) {
        this.ref = ref;
        this.bando = bando;
        this.coor = coor;
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
    
    public void aprenderHabilidad(ICasteable habilidad) {
        int idNumerico = habilidad.getNombre().toUpperCase().hashCode();
        habilidadesDisponibles.put(idNumerico, habilidad);
    }
    
    public boolean usarHabilidad(ICasteable habilidad, Entidad objetivo) {	
    	int calc = this.puntosRecurso - habilidad.getCosteRecurso();
    	if (calc < 0) return false;
    	this.puntosRecurso = calc;
    	getHabilidadPersonal(habilidad).ejecutar(this, objetivo);
        return true;
    }
    
    public List<String> procesarEfectos() {
        List<String> mensajes = new ArrayList<>();
        
        for (EfectoActivo e : efectosActivos) {
            e.tick(this);
        }
        
        Iterator<EfectoActivo> it = efectosActivos.iterator();
        while (it.hasNext()) {
            EfectoActivo e = it.next();
            
            if (e.estaAgotado()) {
                e.terminar(this);
                mensajes.add("dejo de recibir el efecto: " + e.getNombreEfecto());
                it.remove();
            } else {
            	mensajes.add("todavia siente el efecto de: " + e.getNombreEfecto());
            }
        }
        return mensajes;
    }
    public boolean tieneEfectoQueRestringeAccion() {
        return bloqueaAccion;
    }
    
    public List<String> getMensajesEfectosActivos() {
        List<String> mensajes = new ArrayList<>();
        for (EfectoActivo e : efectosActivos) {
            mensajes.add(e.getMensaje());
        }
        return mensajes;
    }
    
    //interfaz afectable
    public void descontarMana(int cantidad) { puntosRecurso = Math.max(0, puntosRecurso - cantidad); }
    public void recibirDanio(int cantidad)  { 
    	if (bloqueaDanio) { bloqueaDanio = false; return;}
    	puntosSalud = Math.max(0, puntosSalud - cantidad);
    }
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
	public void setBloqueaDanio(boolean estado)   { bloqueaDanio = estado; }
	public void setBloqueaMovimiento(boolean estado) {bloqueaMovimiento  = estado; }
	public void setBloqueaHechizos(boolean estado) { bloqueaHechizos = estado; }
	public void setBloqueaAccion(boolean estado) { bloqueaAccion = estado; }
	public boolean getBloqueaDanio()      { return bloqueaDanio; }
	public boolean getBloqueaMovimiento() { return bloqueaMovimiento; }
	public boolean getBloqueaHechizos()   { return bloqueaHechizos; }
	public boolean getBloqueaAccion()     { return bloqueaAccion; }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Nombre:    ").append(ref.getNombre()).append("\n");
        sb.append("Faccion:   ").append(ref.getNombreFaccion()).append("\n");
        sb.append("Bando:     ").append(bando.nombre()).append("\n");
        sb.append("Clase:     ").append(ref.getNombreClase()).append("\n");
        sb.append("Descripcion: ").append("\n\t   ");
        sb.append(ref.getDescripcion().replaceAll("(.{1,21})(?:\\s+|$)", "$1\n\t   ").trim()).append("\n");
        sb.append("-- Estadisticas --").append("\n");
        sb.append("Estado:    ").append(getEstado()).append("\n");
        sb.append("Salud:     ").append(puntosSalud).append(" / ").append(ref.getPuntosSalud()).append(" HP\n");
        sb.append("Maná:      ").append(puntosRecurso).append(" / ").append(ref.getPuntosRecurso()).append(" MP\n");
        sb.append("Movimiento: ").append(movimiento).append(" / ").append(ref.getMovimiento()).append(" TILE\n");
        sb.append(ref.getDatosEspecificos());
        
        return sb.toString();
    }
    
    public IInstanciable getInstanciable() { return ref; }
    public String getNombre()      { return ref.getNombre(); }
    public int getIdBando() 	   { return bando.id(); }
    public String getNombreBando() { return bando.nombre(); }
    public int getMovimiento() 	   { return movimiento; }
    public int getPuntosMana() 	   { return puntosRecurso; }
    public List<ICasteable> getHabilidadesDisponibles() { return new ArrayList<>(habilidadesDisponibles.values()); }
    
    public boolean estaVivo()      { return puntosSalud > 0; }
    public boolean puedeActuar()   { return ref.puedeActuar(); }
    public boolean puedeMoverse()  { return ref.puedeMoverse(); }
    public int getPosX() { return coor.posX(); }
    public int getPosY() { return coor.posY(); }
    public EEstadoJugador getEstado() {
        if (!estaVivo())        return EEstadoJugador.MUERTO;
        if (bloqueaAccion)      return EEstadoJugador.ATURDIDO;
        if (bloqueaMovimiento)  return EEstadoJugador.INMOVILIZADO;
        if (bloqueaHechizos)    return EEstadoJugador.SILENCIADO;
        return EEstadoJugador.JUGANDO;
    }
    
    public void setPosicion(int nuevoPosX, int nuevoPosY) { coor = new RCoordenada(nuevoPosX, nuevoPosY); }
}