package hogwarts;

public final class CMagoEstudiante extends CPersonajeMago {
	public static final int SALUD_INICIAL = 50;
	public static final int MANA_INICIAL  = 50;
	public static final int VELOCIDAD_INICIAL = 2;
    
    public CMagoEstudiante(String nombre) {
        super(nombre, DESCRIPCION, SALUD_INICIAL, MANA_INICIAL, VELOCIDAD_INICIAL);
    }
    public CMagoEstudiante(String nombre, String descr) {
        super(nombre, descr, SALUD_INICIAL, MANA_INICIAL, VELOCIDAD_INICIAL);
    }
    
	@Override
	public String getNombreClase() { return "Estudiante"; }
}
