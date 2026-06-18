package hogwarts;

public class CPersonajeMago extends APersonaje {
	public static final String DESCRIPCION = "Se especializa en hechizos defensivos y de ataque.";
	public static final EFaccion FACCION = EFaccion.MAGO;
    public static final FLibroDeHechizos[] HECHIZOS_RACIALES = {
    	FLibroDeHechizos.EXPELLIARMUS,
        FLibroDeHechizos.PROTEGO,
        FLibroDeHechizos.EXPECTO_PATRONUM,
        FLibroDeHechizos.STUPEFY
    };
    

    public CPersonajeMago(String nombre, String descr, int puntosMana, int puntosSalud, int velocidad, FLibroDeHechizos... hechizosParticulares) {
        super(
        	nombre,
        	descr == null? DESCRIPCION: descr,
        	FACCION,
        	puntosMana,
        	puntosSalud,
        	velocidad,
        	construirHechizos(FACCION, HECHIZOS_RACIALES, hechizosParticulares)
        );
	}
    
	@Override
	public String getNombreClase() { return "Mago Raso"; }
}
