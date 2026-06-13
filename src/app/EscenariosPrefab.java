package app;
import hogwarts.*;
import sim.Escenario;


public abstract class EscenariosPrefab {
    private EscenariosPrefab() {}

    public static Escenario guerraCompleta() {
        PersonajeManager pm = new PersonajeManager();
        BandoManager bm = new BandoManager()
        	    .registrarBando("LOS_MAGOS")
        	    .registrarBando("LOS_MORTIFAGOS")
        	    .registrarBando("LOS_MUGGLES")
        	    .registrarBando("LOS_NEUTRALES")
        	    .definirRelacion("LOS_MAGOS", "LOS_MUGGLES", BandoManager.AMIGO)
        	    .definirRelacion("LOS_MAGOS", "LOS_NEUTRALES", BandoManager.ENEMIGO)
        	    .definirRelacion("LOS_MORTIFAGOS", "LOS_MAGOS", BandoManager.ENEMIGO)
        	    .definirRelacion("LOS_MORTIFAGOS", "LOS_MUGGLES", BandoManager.ENEMIGO);
        Escenario e = new Escenario("Guerra Completa", bm.buildBando());

        e.registrarMago(pm.crearMagoSiNoExiste(FCatalogoDePersonajes.HARRY), bm.getId("LOS_MAGOS"), 0, 0);
        e.registrarMago(new CMagoAuror("Mg. Arcano"), bm.getId("LOS_MAGOS"), 1, 0);
        e.registrarMago(new CMagoAuror("Auror #2"), bm.getId("LOS_MAGOS"), 2, 0);

        
        e.registrarMago(new CMagoProfesor("Albus"), bm.getId("LOS_MAGOS"), 3, 0);
        e.registrarMago(new CMagoProfesor("Cornelius"), bm.getId("LOS_MAGOS"), 4, 0);
        e.registrarMago(new CMagoProfesor("Black Snape"), bm.getId("LOS_MAGOS"), 5, 0);

		e.registrarMago(pm.crearMagoSiNoExiste(FCatalogoDePersonajes.HERMIONE), bm.getId("LOS_MAGOS"), 6, 0);
		e.registrarMago(new CMagoEstudiante("Alejo"), bm.getId("LOS_MAGOS"), 7, 0);
		e.registrarMago(new CMagoEstudiante("Luis"), bm.getId("LOS_MAGOS"), 8, 0);
		e.registrarMago(new CMagoEstudiante("Miguel"), bm.getId("LOS_MAGOS"), 9, 0);



		e.registrarMortifago(pm.crearMortifagoSiNoExiste(FCatalogoDePersonajes.VOLDEMORT), bm.getId("LOS_MORTIFAGOS"), 8, 9);
		e.registrarMortifago(new CMortifagoComandante("Malfoy"), bm.getId("LOS_MORTIFAGOS"), 7, 9);
		e.registrarMortifago(new CMortifagoLacayo("Lacayo de Malfoy #1"), bm.getId("LOS_MORTIFAGOS"), 6, 9);
		e.registrarMortifago(new CMortifagoLacayo("Lacayo de Malfoy #2"), bm.getId("LOS_MORTIFAGOS"), 5, 9);
		e.registrarMortifago(new CMortifagoLacayo("Lacayo de Malfoy #3"), bm.getId("LOS_MORTIFAGOS"), 4, 9);


		e.registrarMortifago(new CMortifagoComandante("El Innombrable"), bm.getId("LOS_MORTIFAGOS"), 3, 9);
		e.registrarMortifago(new CMortifagoLacayo("Lacayo del Innombrable #1"), bm.getId("LOS_MORTIFAGOS"), 2, 9);
		e.registrarMortifago(new CMortifagoLacayo("Lacayo del Innombrable #2"), bm.getId("LOS_MORTIFAGOS"), 1, 9);
		e.registrarMortifago(new CMortifagoLacayo("Lacayo del Innombrable #3"), bm.getId("LOS_MORTIFAGOS"), 0, 9);


        return e;
    }

    public static Escenario duelo() {
        PersonajeManager pm = new PersonajeManager();
        BandoManager bm = new BandoManager()
        	    .registrarBando("GRUPO_A")
        	    .registrarBando("GRUPO_B")
        	    .definirRelacion("GRUPO_A", "GRUPO_B", BandoManager.ENEMIGO);
        Escenario e = new Escenario("Duelo a Muerte", bm.buildBando());
        
        e.registrarMago(pm.crearMagoSiNoExiste(FCatalogoDePersonajes.HARRY), bm.getId("GRUPO_A"), 0, 0);
        e.registrarMortifago(new CMortifagoComandante("Malfoy"), bm.getId("GRUPO_B"), 0, 2);
        
        return e;
    }
}