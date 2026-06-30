package app;
import hogwarts.*;

import java.util.Set;


public enum FLibroDeHechizos {
    EXPELLIARMUS(new CHechizo("Expelliarmus", "Ataque rapido de luz", ENivel.INICIAL, EAfinidad.LUZ,
    						  20, 2, Set.of(EFaccion.MAGO, EFaccion.MORTIFAGO),
    						  10, BHechizos::aplicarExpelliarmus, ETipoHabilidad.DESTRUCTIVO)),
    AVADA_KEDAVRA(new CHechizo("Avada Kedavra", "", ENivel.INICIAL, EAfinidad.OSCURIDAD,
    						   100, 2, Set.of(EFaccion.MORTIFAGO),
    						   200, BHechizos::aplicarAvadaKedavra, ETipoHabilidad.DESTRUCTIVO)),
    EXPECTO_PATRONUM(new CHechizo("Expecto Patronum", "", ENivel.INICIAL, EAfinidad.LUZ,
    						  	  50, 2, Set.of(EFaccion.MAGO, EFaccion.MORTIFAGO),
    						  	  35, BHechizos::aplicarExpectoPatronum, ETipoHabilidad.CURATIVO)),
    ET_LADRONEN(new CHechizo("Et Ladronen", "Roba mana", ENivel.INICIAL, EAfinidad.OSCURIDAD,
    						 15, 2, Set.of(EFaccion.MORTIFAGO),
    						 35, BHechizos::aplicarEtLadronen, ETipoHabilidad.ENCANTAR)),
    PROTEGO(new CHechizoEncantamiento("Protego", "", ENivel.INICIAL, EAfinidad.LUZ,
    								  50, 2, Set.of(EFaccion.MAGO),
    								  10, BHechizos::aplicarProtego, ETipoEncantamiento.ESCUDO)),
    STUPEFY(new CHechizoEncantamiento("Stupefy", "", ENivel.INICIAL, EAfinidad.NEUTRA,
    								  15, 2, Set.of(EFaccion.MAGO, EFaccion.MORTIFAGO),
    								  1, BHechizos::aplicarStupefy, ETipoEncantamiento.ATURDIR)),

    // ====================== NUEVOS HECHIZOS ======================
	 
    // Sacrifica HP propio a cambio del doble en maná. valorEspecial = HP sacrificado (50).
    SANGUIS_VITAE(new CHechizo(
    		"Sanguis Vitae", "Sacrifica vida propia a cambio del doble en maná", ENivel.INICIAL, EAfinidad.NEUTRA, 0, 0,
    		Set.of(EFaccion.MORTIFAGO), 50, BHechizos::aplicarSanguisVitae, ETipoHabilidad.UTILIDAD)),
 
    // Veneno turnado — reutiliza ETipoEncantamiento.VENENO (15 daño/turno mientras dure)
    SECTUMSEMPRA(new CHechizoEncantamiento(
    		"Sectumsempra", "Maldición oscura que envenena al objetivo", ENivel.INICIAL, EAfinidad.OSCURIDAD, 25, 3,
    		Set.of(EFaccion.MORTIFAGO), 3, BHechizos::aplicarSectumsempra, ETipoEncantamiento.VENENO)),
 
    // Cura por turnos — reutiliza ETipoEncantamiento.REGENERACION (10 HP/turno mientras dure)
    VULNERA_SANENTUR(new CHechizoEncantamiento(
    		"Vulnera Sanentur", "Encantamiento de sanación progresiva", ENivel.INICIAL, EAfinidad.LUZ, 30, 2,
    		Set.of(EFaccion.MAGO), 3, BHechizos::aplicarVulneraSanentur, ETipoEncantamiento.REGENERACION)),
 
    // Daño directo de fuego — más fuerte que Expelliarmus, alcance corto
    INCENDIO(new CHechizo(
    		"Incendio", "Ráfaga de fuego concentrado", ENivel.INICIAL, EAfinidad.OSCURIDAD, 35, 1,
    		Set.of(EFaccion.MAGO, EFaccion.MORTIFAGO), 45, BHechizos::aplicarIncendio, ETipoHabilidad.DESTRUCTIVO)),
 
    // Daño directo de hielo — más débil y barato, largo alcance
    GLACIUS(new CHechizo(
    		"Glacius", "Proyectil de hielo a distancia", ENivel.INICIAL, EAfinidad.NEUTRA, 12, 4,
    		Set.of(EFaccion.MAGO, EFaccion.MORTIFAGO), 18, BHechizos::aplicarGlacius, ETipoHabilidad.DESTRUCTIVO)),
 
    // Ralentiza al objetivo — reutiliza ETipoEncantamiento.RALENTIZAR
    PIETRIFICUS(new CHechizoEncantamiento(
    		"Pietrificus", "Petrifica parcialmente al objetivo, reduciendo su movimiento", ENivel.INICIAL, EAfinidad.NEUTRA, 20, 3,
    		Set.of(EFaccion.MAGO, EFaccion.MORTIFAGO), 2, BHechizos::aplicarPietrificus, ETipoEncantamiento.RALENTIZAR)),
 
    // Curación fuerte de un solo golpe — cara, más poderosa que Expecto Patronum
    EPISKEY(new CHechizo(
    		"Episkey", "Sanación instantánea de heridas graves", ENivel.INICIAL, EAfinidad.LUZ, 60, 1,
    		Set.of(EFaccion.MAGO), 60, BHechizos::aplicarEpiskey, ETipoHabilidad.CURATIVO));

	

    private final CHechizo prototipo;
    FLibroDeHechizos(CHechizo prototipo) { this.prototipo = prototipo; }

    public CHechizo construir()         { return prototipo.clonar(); }
    public ENivel getNivelRequerido()   { return prototipo.getNivelRequerido(); }
    public Set<EFaccion> getFacciones() { return prototipo.getFacciones(); }
    
    public String getDescripcion()      { return prototipo.toString(); }
}
