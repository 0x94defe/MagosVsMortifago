package hogwarts;
import sim.IAfectable;
import sim.ICasteable;

public final class BHechizos {
	private BHechizos() {}
	
	
	public static void aplicarExpelliarmus(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirDanio(habilidad.getValorEspecifico());
    }

	public static void aplicarAvadaKedavra(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) { 
	     objetivo.recibirDanio(habilidad.getValorEspecifico()); 
	}
	
    public static void aplicarExpectoPatronum(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirCuracion(habilidad.getValorEspecifico());
    }

    public static void aplicarEtLadronen(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) { 
        objetivo.descontarMana(habilidad.getValorEspecifico());
        lanzador.recibirMana(habilidad.getValorEspecifico());
    }
    
    public static void aplicarProtego(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirEfecto(habilidad.getComportamiento(), habilidad.getValorEspecifico()); 
    }
    
    public static void aplicarStupefy(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
    	objetivo.recibirEfecto(habilidad.getComportamiento(), habilidad.getValorEspecifico());
    }
    
    // ====================== NUEVOS HECHIZOS ======================
    /** Sacrifica HP del lanzador a cambio del doble en maná: pierde X HP, gana 2X MP. */
    public static void aplicarSanguisVitae(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        int hpSacrificado = habilidad.getValorEspecifico();
        lanzador.recibirDanio(hpSacrificado);
        lanzador.recibirMana(hpSacrificado * 2);
    }
 
    /** Veneno turnado — daño por turno via ETipoEncantamiento.VENENO. */
    public static void aplicarSectumsempra(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirEfecto(habilidad.getComportamiento(), habilidad.getValorEspecifico());
    }
 
    /** Cura por turnos — regeneración progresiva via ETipoEncantamiento.REGENERACION. */
    public static void aplicarVulneraSanentur(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirEfecto(habilidad.getComportamiento(), habilidad.getValorEspecifico());
    }
 
    /** Daño directo de fuego — más fuerte, corto alcance. */
    public static void aplicarIncendio(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirDanio(habilidad.getValorEspecifico());
    }
 
    /** Daño directo de hielo — más débil, barato, largo alcance. */
    public static void aplicarGlacius(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirDanio(habilidad.getValorEspecifico());
    }
 
    /** Ralentiza al objetivo via ETipoEncantamiento.RALENTIZAR. */
    public static void aplicarPietrificus(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirEfecto(habilidad.getComportamiento(), habilidad.getValorEspecifico());
    }
 
    /** Curación fuerte de un solo golpe — cara, más poderosa que Expecto Patronum. */
    public static void aplicarEpiskey(IAfectable lanzador, IAfectable objetivo, ICasteable habilidad) {
        objetivo.recibirCuracion(habilidad.getValorEspecifico());
    }


}
