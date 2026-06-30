package app;
import hogwarts.*;
import sim.Escenario;
import sim.IEscenarioConsulta;

import java.util.ArrayList;
import java.util.List;


public enum FEscenariosPrefab {
    GUERRA_COMPLETA("Guerra Completa") {
        public Escenario construir() {
            BandoBuilder bm = new BandoBuilder()
            	    .registrarBando(EFaccion.MAGO)
            	    .registrarBando(EFaccion.MORTIFAGO)
            	    .registrarBando(EFaccion.MUGGLE)
            	    .definirRelacion(EFaccion.MAGO, EFaccion.MUGGLE, BandoBuilder.AMIGO)
            	    .definirRelacion(EFaccion.MORTIFAGO, EFaccion.MAGO, BandoBuilder.ENEMIGO)
            	    .definirRelacion(EFaccion.MORTIFAGO, EFaccion.MUGGLE, BandoBuilder.ENEMIGO);
            Escenario e = new Escenario(ANCHO_GRILLA, ALTO_GRILLA, getNombre(), bm.buildBando());

            e.registrarPj(PersonajeManager.crearMago(FCatalogoDePersonajes.HARRY), bm.getId(EFaccion.MAGO), 0, 0);
            e.registrarPj(new CMagoAuror("Mg. Arcano"), bm.getId(EFaccion.MAGO), 1, 0);
            e.registrarPj(new CMagoAuror("Auror #2"), bm.getId(EFaccion.MAGO), 2, 0);

            
            e.registrarPj(new CMagoProfesor("Albus"), bm.getId(EFaccion.MAGO), 3, 0);
            e.registrarPj(new CMagoProfesor("Cornelius"), bm.getId(EFaccion.MAGO), 4, 0);
            e.registrarPj(new CMagoProfesor("Black Snape"), bm.getId(EFaccion.MAGO), 5, 0);

    		e.registrarPj(PersonajeManager.crearMago(FCatalogoDePersonajes.HERMIONE), bm.getId(EFaccion.MAGO), 6, 0);
    		e.registrarPj(new CMagoEstudiante("Alejo"), bm.getId(EFaccion.MAGO), 7, 0);
    		e.registrarPj(new CMagoEstudiante("Luis"), bm.getId(EFaccion.MAGO), 8, 0);
    		e.registrarPj(new CMagoEstudiante("Miguel"), bm.getId(EFaccion.MAGO), 9, 0);



    		e.registrarPj(PersonajeManager.crearMortifago(FCatalogoDePersonajes.VOLDEMORT), bm.getId(EFaccion.MORTIFAGO), 8, 9);
    		e.registrarPj(new CMortifagoComandante("Malfoy"), bm.getId(EFaccion.MORTIFAGO), 7, 9);
    		e.registrarPj(new CMortifagoLacayo("Lacayo de Malfoy #1"), bm.getId(EFaccion.MORTIFAGO), 6, 9);
    		e.registrarPj(new CMortifagoLacayo("Lacayo de Malfoy #2"), bm.getId(EFaccion.MORTIFAGO), 5, 9);
    		e.registrarPj(new CMortifagoLacayo("Lacayo de Malfoy #3"), bm.getId(EFaccion.MORTIFAGO), 4, 9);


    		e.registrarPj(new CMortifagoComandante("El Innombrable"), bm.getId(EFaccion.MORTIFAGO), 3, 9);
    		e.registrarPj(new CMortifagoLacayo("Lacayo del Innombrable #1"), bm.getId(EFaccion.MORTIFAGO), 2, 9);
    		e.registrarPj(new CMortifagoLacayo("Lacayo del Innombrable #2"), bm.getId(EFaccion.MORTIFAGO), 1, 9);
    		e.registrarPj(new CMortifagoLacayo("Lacayo del Innombrable #3"), bm.getId(EFaccion.MORTIFAGO), 0, 9);
    		
    		 return e;
        }
    },
    DUELO("Duelo a Muerte") {
        public Escenario construir() {
        	BandoBuilder bm = new BandoBuilder()
            	    .registrarBando("GRUPO_A")
            	    .registrarBando("GRUPO_B")
            	    .definirRelacion("GRUPO_A", "GRUPO_B", BandoBuilder.ENEMIGO);
            Escenario e = new Escenario(ANCHO_GRILLA, ALTO_GRILLA, getNombre(), bm.buildBando());
            
            e.registrarPj(new CMortifagoComandante("Malfoy 2"), bm.getId("GRUPO_A"), 0, 0);
            e.registrarPj(new CMagoEstudiante("Alejo"), bm.getId("GRUPO_A"), 1, 0);
            e.registrarPj(new CMortifagoComandante("Malfoy"), bm.getId("GRUPO_B"), 0, 2);
            
            return e;
        }
    };

    private static final int ANCHO_GRILLA = 10;
    private static final int ALTO_GRILLA  = 10;
    private final String nombre;
    FEscenariosPrefab(String nombre) { this.nombre = nombre; }

    public abstract Escenario construir();
    public String getNombre() { return nombre; }
    public static List<IEscenarioConsulta> obtenerEscenariosDisponibles() {
    	List<IEscenarioConsulta> lista = new ArrayList<>();
    	
    	for (FEscenariosPrefab esc : FEscenariosPrefab.values()) {
    		lista.add(esc.construir());
    	}
    	
        return lista;
    }

}
