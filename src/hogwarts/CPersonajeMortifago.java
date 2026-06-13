package hogwarts;

public class CPersonajeMortifago extends APersonaje {
	public static final String DESCRIPCION = "Se enfoca en ataques oscuros y habilidades especiales.";
	public static final EFaccion FACCION = EFaccion.MORTIFAGO;
    public static final FLibroDeHechizos[] HECHIZOS_RACIALES = {
    	FLibroDeHechizos.EXPELLIARMUS,
    	FLibroDeHechizos.AVADA_KEDAVRA
    };
    

    public CPersonajeMortifago(String nombre, String descr, int puntosMana, int puntosSalud, int velocidad, FLibroDeHechizos... hechizosParticulares) {
        super(
        	nombre,
        	descr == null? DESCRIPCION: descr,
        	puntosMana,
        	puntosSalud,
        	velocidad,
        	construirHechizos(FACCION, HECHIZOS_RACIALES, hechizosParticulares)
        );
	}


    @Override
    public final EFaccion getFaccion(){ 
		return FACCION; 
	} //al ponerle final ninguna subclase se manda cagadas
    
	@Override
	public String getClase(){ 
		return "Mortifago Raso";	
	}
}
