package sim;

import java.util.Map;


public interface IInstanciable {
	String getNombre();
	String getDescripcion();
	String getNombreFaccion();
	String getNombreClase();
	String getDatosEspecificos();
	String toString();
	int getPuntosSalud();
	int getPuntosRecurso();
	int getMovimiento();
	
	Map<Integer, ICasteable> getHabilidades();
    boolean puedeActuar();
    boolean puedeMoverse();
}
