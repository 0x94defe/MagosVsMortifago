package hogwarts;

import app.FLibroDeHechizos;

public class CPersonajeMago extends APersonaje {
	public static final ENivel NIVEL = ENivel.INICIAL;
	public static final EAfinidad AFINIDAD = EAfinidad.LUZ;
	public static final String DESCRIPCION = "Se especializa en hechizos defensivos y de ataque.";
	public static final EFaccion FACCION = EFaccion.MAGO;
	public static final FLibroDeHechizos[] HECHIZOS_RACIALES_MAGO = {
		    FLibroDeHechizos.EXPELLIARMUS,       // Ataque básico de luz
		    FLibroDeHechizos.PROTEGO,            // Escudo base
		    FLibroDeHechizos.EXPECTO_PATRONUM,   // Curación estándar
		    FLibroDeHechizos.STUPEFY,            // Aturdimiento neutro
		    FLibroDeHechizos.VULNERA_SANENTUR,   // NUEVO: Curación por turnos (Regeneración)
		    FLibroDeHechizos.EPISKEY,            // NUEVO: Curación fuerte directa
		    FLibroDeHechizos.PIETRIFICUS,        // NUEVO: Ralentizar (Neutro, permitido para magos)
		    FLibroDeHechizos.GLACIUS,            // NUEVO: Ataque de hielo a distancia (Neutro)
		};
    

    public CPersonajeMago(String nombre, String descr, ENivel nivelPersonaje, int puntosMana, int puntosSalud, int velocidad, FLibroDeHechizos... hechizosParticulares) {
        super(
        	nombre,
        	descr == null? DESCRIPCION: descr,
        	nivelPersonaje,
        	AFINIDAD,
        	FACCION,
        	puntosMana,
        	puntosSalud,
        	velocidad,
        	combinarHechizos(HECHIZOS_RACIALES_MAGO, hechizosParticulares)
        );
	}
    
	@Override
	public String getNombreClase() { return "Mago Raso"; }
	@Override
	protected double getMultiplicadorPropio(ETipoHabilidad tipo) {
	    return tipo == ETipoHabilidad.CURATIVO ? 1.3 : 1.0;
	}
}
