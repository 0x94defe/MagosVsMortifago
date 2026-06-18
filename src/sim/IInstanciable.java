package sim;

import java.util.Map;

public interface IInstanciable {
	public String getNombre();
	public String getDescripcion();
	public String getNombreFaccion();
	public String getNombreClase();
	public String getDatosEspecificos();
	public int getPuntosSalud();
	public int getPuntosRecurso();
	public int getMovimiento();
	
	public Map<Integer, ICasteable> getHabilidades();
    public boolean puedeActuar();
    public boolean puedeMoverse();
}
