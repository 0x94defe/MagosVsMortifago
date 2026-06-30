package hogwarts;

public final class CMortifagoLacayo extends CPersonajeMortifago {
	public static final int SALUD_INICIAL = 100;
    public static final int MANA_INICIAL  = 50;
    public static final int VELOCIDAD_INICIAL = 2;

    public CMortifagoLacayo(String nombre) {
        super(nombre, DESCRIPCION, NIVEL, SALUD_INICIAL, MANA_INICIAL, VELOCIDAD_INICIAL);
    }

    
	@Override
	public String getNombreClase() { return "Lacayo"; }
}
