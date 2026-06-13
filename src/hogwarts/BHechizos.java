package hogwarts;
import sim.IAfectable;


public final class BHechizos {
	private BHechizos() {}
	
	
	public static void aplicarExpelliarmus(IAfectable lanzador, IAfectable objetivo) {
        CHechizo hechizoParticular = (CHechizo) lanzador.getHabilidadPersonal(FLibroDeHechizos.EXPELLIARMUS.getPrototipo());
        objetivo.recibirDanio(hechizoParticular.getValorEspecial());
    }

	public static void aplicarAvadaKedavra(IAfectable lanzador, IAfectable objetivo) { 
	     CHechizo hechizoParticular = (CHechizo) lanzador.getHabilidadPersonal(FLibroDeHechizos.AVADA_KEDAVRA.getPrototipo()); 
	     objetivo.recibirDanio(hechizoParticular.getValorEspecial()); 
	}
	
    public static void aplicarExpectoPatronum(IAfectable lanzador, IAfectable objetivo) {
        CHechizo hechizoParticular = (CHechizo) lanzador.getHabilidadPersonal(FLibroDeHechizos.EXPECTO_PATRONUM.getPrototipo());
        objetivo.recibirCuracion(hechizoParticular.getValorEspecial());
    }

    public static void aplicarEtLadronen(IAfectable lanzador, IAfectable objetivo) { 
        CHechizo hechizoParticular = (CHechizo) lanzador.getHabilidadPersonal(FLibroDeHechizos.ET_LADRONEN.getPrototipo()); 
        objetivo.descontarMana(hechizoParticular.getValorEspecial()); 
    }
    
    public static void aplicarProtego(IAfectable lanzador, IAfectable objetivo) {
        CHechizoEncantamiento encantamientoParticular = (CHechizoEncantamiento) lanzador.getHabilidadPersonal(FLibroDeHechizos.PROTEGO.getPrototipo());
        objetivo.recibirEfecto(encantamientoParticular.getTipoEncantamiento(), encantamientoParticular.getValorEspecial()); 
    }
    
    public static void aplicarStupefy(IAfectable lanzador, IAfectable objetivo) {
    	CHechizoEncantamiento encantamientoParticular = (CHechizoEncantamiento) lanzador.getHabilidadPersonal(FLibroDeHechizos.STUPEFY.getPrototipo());
        objetivo.recibirEfecto(encantamientoParticular.getTipoEncantamiento(), encantamientoParticular.getValorEspecial()); 
    }
}
