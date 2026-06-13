package sim;

import java.util.Map;

import hogwarts.AHabilidad;
import hogwarts.EFaccion;

public interface IInstanciable {
	public String getNombre();
	public String getDescripcion();
	public EFaccion getFaccion();
	public String getClase();
	public String getDatosEspecificos();
	public int getPuntosSalud();
	public int getPuntosRecurso();
	public int getVelocidad();
	public Map<Integer, AHabilidad> getHabilidades();
    public boolean puedeActuar();
    public boolean puedeMoverse();
}
