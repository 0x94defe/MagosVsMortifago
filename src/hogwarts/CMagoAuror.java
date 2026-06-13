package hogwarts;

public final class CMagoAuror extends CPersonajeMago {
	public static final int SALUD_INICIAL = 300;
    public static final int MANA_INICIAL  = 150;
    public static final int VELOCIDAD_INICIAL  = 4;
    public static final String DESCRIPCION  = "Son magos excepcionales!";

    public CMagoAuror(String nombre) {
        super(nombre, DESCRIPCION, SALUD_INICIAL, MANA_INICIAL, VELOCIDAD_INICIAL);
    }
    
	@Override
	public String getClase() { 
        return "Auror";	
    }
}
