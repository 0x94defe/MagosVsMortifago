package test;

import app.BandoManager;
import hogwarts.AHabilidad;
import hogwarts.CMortifagoComandante;
import hogwarts.CMortifagoLacayo;
import sim.Entidad;
import sim.Escenario;
import sim.SimuladorTurnos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReglaRondaHechizosTest {

    @Test
    void noDebePermitirRepetirMismoHechizoEnMismaRonda() {
        SimuladorTurnos simulador = crearDueloMortifagos();

        avanzarHasta(simulador, "Comandante");
        Entidad atacante = simulador.getEntidadActual();
        Entidad objetivo = buscarRival(simulador, "Comandante");

        AHabilidad expelliarmus = atacante.getHabilidadesDisponibles().stream()
            .filter(h -> "Expelliarmus".equals(h.getNombre()))
            .findFirst()
            .orElseThrow();

        assertTrue(simulador.lanzarHabilidad(expelliarmus, objetivo));
        assertFalse(simulador.lanzarHabilidad(expelliarmus, objetivo));

        long restantes = simulador.getHabilidadesDisponiblesParaObjetivo(objetivo).stream()
            .filter(h -> "Expelliarmus".equals(h.getNombre()))
            .count();
        assertEquals(0, restantes);
    }

    @Test
    void alCambiarDeRondaDebePermitirUsarDeNuevoElHechizo() {
        SimuladorTurnos simulador = crearDueloMortifagos();

        avanzarHasta(simulador, "Comandante");
        Entidad atacante = simulador.getEntidadActual();
        Entidad objetivo = buscarRival(simulador, "Comandante");

        AHabilidad expelliarmus = atacante.getHabilidadesDisponibles().stream()
            .filter(h -> "Expelliarmus".equals(h.getNombre()))
            .findFirst()
            .orElseThrow();

        assertTrue(simulador.lanzarHabilidad(expelliarmus, objetivo));

        while (!simulador.esFindeTurno()) {
            simulador.saltarTurno();
        }
        simulador.terminarRonda();

        avanzarHasta(simulador, "Comandante");
        atacante = simulador.getEntidadActual();
        objetivo = buscarRival(simulador, "Comandante");

        AHabilidad expelliarmusNuevaRonda = atacante.getHabilidadesDisponibles().stream()
            .filter(h -> "Expelliarmus".equals(h.getNombre()))
            .findFirst()
            .orElseThrow();

        assertTrue(simulador.lanzarHabilidad(expelliarmusNuevaRonda, objetivo));
    }

    private static SimuladorTurnos crearDueloMortifagos() {
        BandoManager manager = new BandoManager()
            .registrarBando("A")
            .registrarBando("B")
            .definirRelacion("A", "B", BandoManager.ENEMIGO);

        Escenario escenario = new Escenario("Duelo", manager.buildBando());
        escenario.registrarMortifago(new CMortifagoComandante("Comandante"), manager.getId("A"), 0, 0);
        escenario.registrarMortifago(new CMortifagoLacayo("Lacayo"), manager.getId("B"), 0, 1);

        return new SimuladorTurnos(escenario, 5, 5);
    }

    private static void avanzarHasta(SimuladorTurnos simulador, String nombre) {
        int guard = 0;
        while ((simulador.getEntidadActual() == null || !nombre.equals(simulador.getEntidadActual().getNombre())) && guard < 20) {
            if (simulador.esFindeTurno()) {
                simulador.terminarRonda();
            } else {
                simulador.saltarTurno();
            }
            guard++;
        }
        assertNotNull(simulador.getEntidadActual());
        assertEquals(nombre, simulador.getEntidadActual().getNombre());
    }

    private static Entidad buscarRival(SimuladorTurnos simulador, String nombreActual) {
        return simulador.getEntidades().stream()
            .filter(e -> !nombreActual.equals(e.getNombre()))
            .findFirst()
            .orElseThrow();
    }
}

