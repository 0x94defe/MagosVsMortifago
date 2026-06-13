package test;

import app.BandoManager;
import sim.Bando;
import sim.ETipoRelacion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BandoManagerTest {

    @Test
    void buildBando_debeConstruirRelacionesSimetricas() {
        BandoManager manager = new BandoManager()
            .registrarBando("MAGOS")
            .registrarBando("MORTIFAGOS")
            .definirRelacion("MAGOS", "MORTIFAGOS", BandoManager.ENEMIGO);

        Bando bando = manager.buildBando();

        assertEquals(2, bando.getCantidadBandos());
        assertTrue(bando.esEnemigo(manager.getId("MAGOS"), manager.getId("MORTIFAGOS")));
        assertTrue(bando.esEnemigo(manager.getId("MORTIFAGOS"), manager.getId("MAGOS")));
        assertEquals(ETipoRelacion.AMIGO, bando.getRelacion(manager.getId("MAGOS"), manager.getId("MAGOS")));
    }
}
