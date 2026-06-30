package hogwarts;
import sim.IEstrategiaHabilidad;
import sim.ITurnable;

import java.util.Set;


public class CHechizo extends AHabilidad implements Cloneable {
    private final ETipoHabilidad tipoHechizo;  
    
    public CHechizo(String nombre, String descr, ENivel nivelRequerido, EAfinidad afinidad, int coste, int distancia,
    		        Set<EFaccion> faccionesPermitidas, int valorEspecial, IEstrategiaHabilidad estrategia,
    		        ETipoHabilidad tipoHechizo)
    {
        super(nombre, descr, nivelRequerido, afinidad, coste, distancia, faccionesPermitidas, valorEspecial, estrategia);
        this.tipoHechizo = tipoHechizo;
    }

    public CHechizo clonar() {
        try { return (CHechizo) super.clone(); } 
        catch (CloneNotSupportedException e) { throw new AssertionError(); }
    }
    protected String getMasDetallesEspecificos() {	return ""; }

    public String getDetallesEspecificos() {
        StringBuilder sb = new StringBuilder();

        sb.append(" " + tipoHechizo.getStat() + "\n");
        sb.append("Tipo Hechizo: ").append(tipoHechizo).append("\n");
        sb.append(getMasDetallesEspecificos());
        
        return sb.toString();
    }


    //interfaz
    public String getNombreHabilidad() { return "Hechizo"; }
    public ETipoHabilidad getTipoHabilidad() { return tipoHechizo; }
	public ITurnable getComportamiento() { return null;	}
	public boolean esHabilidadOfensiva() { return tipoHechizo.esOfensivo(); }
	public boolean esHabilidadTurnable() { return tipoHechizo.esTurnado(); }
	public boolean esHabilidadEspecial() { return tipoHechizo.esEspecial(); }
}
