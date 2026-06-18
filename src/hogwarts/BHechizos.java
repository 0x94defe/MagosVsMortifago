package hogwarts;
import sim.IAfectable;
import sim.ICasteable;

public final class BHechizos {
	private BHechizos() {}
	
	
	public static void aplicarExpelliarmus(IAfectable lanzador, IAfectable objetivo) {
		ICasteable hechizoParticular = lanzador.getHabilidadPersonal(FLibroDeHechizos.EXPELLIARMUS.getPrototipo());
        objetivo.recibirDanio(hechizoParticular.getValorEspecifico());
    }

	public static void aplicarAvadaKedavra(IAfectable lanzador, IAfectable objetivo) { 
		ICasteable hechizoParticular = lanzador.getHabilidadPersonal(FLibroDeHechizos.AVADA_KEDAVRA.getPrototipo()); 
	     objetivo.recibirDanio(hechizoParticular.getValorEspecifico()); 
	}
	
    public static void aplicarExpectoPatronum(IAfectable lanzador, IAfectable objetivo) {
    	ICasteable hechizoParticular = lanzador.getHabilidadPersonal(FLibroDeHechizos.EXPECTO_PATRONUM.getPrototipo());
        objetivo.recibirCuracion(hechizoParticular.getValorEspecifico());
    }

    public static void aplicarEtLadronen(IAfectable lanzador, IAfectable objetivo) { 
    	ICasteable hechizoParticular = lanzador.getHabilidadPersonal(FLibroDeHechizos.ET_LADRONEN.getPrototipo()); 
        objetivo.descontarMana(hechizoParticular.getValorEspecifico()); 
    }
    
    public static void aplicarProtego(IAfectable lanzador, IAfectable objetivo) {
    	ICasteable encantamientoParticular = lanzador.getHabilidadPersonal(FLibroDeHechizos.PROTEGO.getPrototipo());
        objetivo.recibirEfecto(encantamientoParticular.getComportamiento(), encantamientoParticular.getValorEspecifico()); 
    }
    
    public static void aplicarStupefy(IAfectable lanzador, IAfectable objetivo) {
    	ICasteable encantamientoParticular = lanzador.getHabilidadPersonal(FLibroDeHechizos.STUPEFY.getPrototipo());
    	objetivo.recibirEfecto(encantamientoParticular.getComportamiento(), encantamientoParticular.getValorEspecifico());
    }
}
