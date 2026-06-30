package hogwarts;

import app.FLibroDeHechizos;

public class CPersonajeMortifago extends APersonaje {
	public static final ENivel NIVEL = ENivel.INICIAL;
	public static final EAfinidad AFINIDAD = EAfinidad.OSCURIDAD;
	public static final String DESCRIPCION = "Se enfoca en ataques oscuros y habilidades especiales.";
	public static final EFaccion FACCION = EFaccion.MORTIFAGO;
	public static final FLibroDeHechizos[] HECHIZOS_RACIALES_MORTIFAGO = {
		    FLibroDeHechizos.EXPELLIARMUS,       // Ataque básico (permitido en su Set)
		    FLibroDeHechizos.AVADA_KEDAVRA,      // Daño masivo oscuro
		    FLibroDeHechizos.ET_LADRONEN,      // Daño masivo oscuro
		    FLibroDeHechizos.SECTUMSEMPRA,       // NUEVO: Maldición/Veneno por turnos oscuro
		    FLibroDeHechizos.INCENDIO,           // NUEVO: Daño de fuego a corto alcance oscuro
		    FLibroDeHechizos.PIETRIFICUS,        // NUEVO: Control de movimiento neutro
		    FLibroDeHechizos.GLACIUS,            // NUEVO: Ataque de hielo largo alcance neutro
		    FLibroDeHechizos.SANGUIS_VITAE       // NUEVO: Sacrificio oscuro para ganar maná neutro
		};
    

    public CPersonajeMortifago(String nombre, String descr, ENivel nivelPersonaje, int puntosMana, int puntosSalud, int velocidad, FLibroDeHechizos... hechizosParticulares) {
        super(
        	nombre,
        	descr == null? DESCRIPCION: descr,
        	nivelPersonaje,
        	AFINIDAD,
        	FACCION,
        	puntosMana,
        	puntosSalud,
        	velocidad,
        	combinarHechizos(HECHIZOS_RACIALES_MORTIFAGO, hechizosParticulares)
        );
	}
    
	@Override
	public String getNombreClase() { return "Mortifago Raso"; }
	@Override
	protected double getMultiplicadorPropio(ETipoHabilidad tipo) {
	    return tipo == ETipoHabilidad.DESTRUCTIVO ? 1.3 : 1.0;
	}
}
