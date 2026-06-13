package test;

import app.BandoManager;
import hogwarts.AHabilidad;
import hogwarts.CMortifagoComandante;
import hogwarts.CMortifagoLacayo;
import org.junit.jupiter.api.Test;
import sim.Entidad;
import sim.Escenario;
import sim.SimuladorTurnos;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class SimuladorTurnosTest {

    @Test
    void lanzarAvadaKedavra_debeFinalizarPartidaAlEliminarUltimoRival() {
        BandoManager manager = new BandoManager()
            .registrarBando("A")
            .registrarBando("B")
            .definirRelacion("A", "B", BandoManager.ENEMIGO);

        Escenario escenario = new Escenario("Duelo", manager.buildBando());
        escenario.registrarMortifago(new CMortifagoComandante("Comandante"), manager.getId("A"), 0, 0);
        escenario.registrarMortifago(new CMortifagoLacayo("Lacayo"), manager.getId("B"), 0, 1);

        SimuladorTurnos simulador = new SimuladorTurnos(escenario, 5, 5);

        // Asegura que llegue el turno del comandante sin depender del orden del mapa.
        int guard = 0;
        while ((simulador.getEntidadActual() == null || !"Comandante".equals(simulador.getEntidadActual().getNombre())) && guard < 8) {
            if (simulador.esFindeTurno()) {
                simulador.terminarRonda();
            } else {
                simulador.saltarTurno();
            }
            guard++;
        }

        Entidad actual = simulador.getEntidadActual();
        assertNotNull(actual);
        assertTrue("Comandante".equals(actual.getNombre()));

        AHabilidad avada = actual.getHabilidadesDisponibles().stream()
            .filter(h -> "Avada Kedavra".equals(h.getNombre()))
            .findFirst()
            .orElseThrow();

        Optional<Entidad> rival = simulador.getEntidades().stream()
            .filter(e -> !"Comandante".equals(e.getNombre()))
            .findFirst();

        assertTrue(rival.isPresent());
        assertTrue(simulador.lanzarHabilidad(avada, rival.get()));
        assertTrue(simulador.esJuegoTerminado());
    }
}
