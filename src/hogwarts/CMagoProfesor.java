package hogwarts;

public final class CMagoProfesor extends CPersonajeMago {
	public static final int SALUD_INICIAL = 100;
	public static final int MANA_INICIAL  = 200;
	public static final int VELOCIDAD_INICIAL = 2;

    public CMagoProfesor(String nombre) {
        super(nombre, DESCRIPCION, SALUD_INICIAL, MANA_INICIAL, VELOCIDAD_INICIAL);
    }
    public CMagoProfesor(String nombre, String descr) {
        super(nombre, descr, SALUD_INICIAL, MANA_INICIAL, VELOCIDAD_INICIAL);
    }
    
	@Override
	public String getNombreClase() { return "Profesor";	}
}
