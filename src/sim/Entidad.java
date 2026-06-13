package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hogwarts.AHabilidad;
import hogwarts.ETipoEncantamiento;


public class Entidad implements IAfectable {
	private final IInstanciable ref;
    private RCoordenada coor;
    private RBandoToken bando;
    private int puntosSalud;
    private int puntosRecurso;
    private int movimiento;
    private Map<Integer, AHabilidad> habilidadesDisponibles;
    private int escudoActivo;
    private int aturdido;

    
    public Entidad(IInstanciable ref, RBandoToken bando, RCoordenada coor) {
        this.ref = ref;
        this.bando = bando;
    	this.puntosSalud = ref.getPuntosSalud();
        this.puntosRecurso = ref.getPuntosRecurso();
        this.movimiento = ref.getVelocidad();
        this.habilidadesDisponibles = ref.getHabilidades();
        this.coor = coor;
        this.escudoActivo = 0;
        this.aturdido = 0;
    }  
    
    public void aprenderHabilidad(AHabilidad habilidad) {
        int idNumerico = habilidad.getNombre().toUpperCase().hashCode();
        habilidadesDisponibles.put(idNumerico, habilidad);
    }
    
    public boolean usarHabilidad(AHabilidad habilidad, Entidad objetivo) {	
    	int calc = this.puntosRecurso - habilidad.getCosteRecurso();
    	if (calc < 0) return false;
    	this.puntosRecurso = calc;
    	getHabilidadPersonal(habilidad).ejecutar(this, objetivo);
        return true;
    }
    
    //interfaz afectable
    @Override
    public void descontarMana(int cantidad) {
    	consumirEscudo();
        puntosRecurso = Math.max(0, puntosRecurso - cantidad);
    }
    @Override
    public void recibirDanio(int cantidad) {
    	consumirEscudo();
        puntosSalud = Math.max(0, puntosSalud - cantidad);
    }
    @Override
    public void recibirCuracion(int cantidad) {
    	puntosSalud = Math.min(puntosSalud + cantidad, ref.getPuntosSalud());
    }
    @Override
    public void recibirEfecto(ETipoEncantamiento efecto, int duracion) {
        switch (efecto) {
            case ESCUDO:   this.escudoActivo += duracion; break;
            case ATURDIR:  this.aturdido     += duracion; break;
        }
    }
	@Override
	public AHabilidad getHabilidadPersonal(AHabilidad habilidad) {
		int idNumerico = habilidad.getNombre().trim().toUpperCase().replace(" ", "_").hashCode();
		AHabilidad habilidadDeLaEntidad = habilidadesDisponibles.get(idNumerico);
		if (habilidadDeLaEntidad == null) throw new IllegalStateException("No existe habilidad: '" + habilidad + "' para la entidad: '" + ref.getNombre() + "'");
		return habilidadDeLaEntidad;
	}

    public void consumirAturdido() {
        aturdido = Math.max(0, aturdido - 1);
    }
    private void consumirEscudo() {
        escudoActivo = Math.max(0, escudoActivo - 1);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Nombre:    ").append(ref.getNombre()).append("\n");
        sb.append("Faccion:   ").append(ref.getFaccion()).append("\n");
        sb.append("Bando:     ").append(bando.nombre()).append("\n");
        sb.append("Clase:     ").append(ref.getClase()).append("\n");
        sb.append("Descripcion: ").append("\n\t   ");
        sb.append(ref.getDescripcion().replaceAll("(.{1,21})(?:\\s+|$)", "$1\n\t   ").trim()).append("\n");
        sb.append("-- Estadisticas --").append("\n");
        sb.append("Salud:     ").append(puntosSalud).append(" / ").append(ref.getPuntosSalud()).append(" HP\n");
        sb.append("Maná:      ").append(puntosRecurso).append(" / ").append(ref.getPuntosRecurso()).append(" HP\n");
        sb.append("Movimiento: ").append(movimiento).append(" / ").append(ref.getVelocidad()).append(" TILE\n");
        sb.append(ref.getDatosEspecificos());
        
        return sb.toString();
    }
    
    public IInstanciable getInstanciable() { return ref; }
    public String getNombre()      { return ref.getNombre(); }
    public int getIdBando() 	   { return bando.id(); }
    public String getNombreBando() { return bando.nombre(); }
    public int getMovimiento() 	   { return movimiento; }
    public int getPuntosMana() 	   { return puntosRecurso; }
    public List<AHabilidad> getHabilidadesDisponibles() { return new ArrayList<>(habilidadesDisponibles.values()); }
    public boolean estaVivo()      { return puntosSalud > 0; }
    public boolean estaAturdido()  { return aturdido > 0; }
    public boolean estaProtegido() { return escudoActivo > 0; }
    public boolean puedeActuar()   { return ref.puedeActuar(); }
    public boolean puedeMoverse()  { return ref.puedeMoverse(); }
    public int getPosX() { return coor.posX(); }
    public int getPosY() { return coor.posY(); }
    
    public void setPosicion(int nuevoPosX, int nuevoPosY) { coor = new RCoordenada(nuevoPosX, nuevoPosY); }
}