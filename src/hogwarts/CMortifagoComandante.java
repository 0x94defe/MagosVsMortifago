package hogwarts;

public final class CMortifagoComandante extends CPersonajeMortifago {
    public static final int SALUD_INICIAL = 200;
    public static final int MANA_INICIAL  = 100;
    public static final int VELOCIDAD_INICIAL = 2;

    public CMortifagoComandante(String nombre) {
        super(nombre, null, SALUD_INICIAL, MANA_INICIAL, VELOCIDAD_INICIAL);
    }
    
	@Override
	public String getClase(){ 
        return "Comandante";	
	}
}
