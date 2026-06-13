package tests;

import hogwarts.AHabilidad;
import hogwarts.CMagoAuror;
import hogwarts.CMortifagoComandante;
import hogwarts.CMortifagoLacayo;
import hogwarts.FLibroDeHechizos;
import org.junit.jupiter.api.Test;
import sim.Entidad;
import sim.RBandoToken;
import sim.RCoordenada;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HechizosTest {

    @Test
    void usarExpelliarmus_debeConsumirManaYEliminarObjetivoDebilitado() {
        Entidad lanzador = new Entidad(new CMagoAuror("Auror"), new RBandoToken(0, "A"), new RCoordenada(0, 0));
        Entidad objetivo = new Entidad(new CMortifagoLacayo("Lacayo"), new RBandoToken(1, "B"), new RCoordenada(0, 1));
        AHabilidad expelliarmus = FLibroDeHechizos.EXPELLIARMUS.getPrototipo();

        objetivo.recibirDanio(95);
        int manaInicial = lanzador.getPuntosMana();

        assertTrue(lanzador.usarHabilidad(expelliarmus, objetivo));
        assertEquals(manaInicial - expelliarmus.getCosteRecurso(), lanzador.getPuntosMana());
        assertFalse(objetivo.estaVivo());
    }

    @Test
    void usarAvadaKedavra_debeEliminarObjetivo() {
        Entidad lanzador = new Entidad(new CMortifagoComandante("Comandante"), new RBandoToken(0, "A"), new RCoordenada(0, 0));
        Entidad objetivo = new Entidad(new CMortifagoLacayo("Lacayo"), new RBandoToken(1, "B"), new RCoordenada(0, 1));

        assertTrue(lanzador.usarHabilidad(FLibroDeHechizos.AVADA_KEDAVRA.getPrototipo(), objetivo));
        assertFalse(objetivo.estaVivo());
    }

    @Test
    void usarEncantamientos_debeAplicarEstados() {
        Entidad lanzador = new Entidad(new CMagoAuror("Auror"), new RBandoToken(0, "A"), new RCoordenada(0, 0));
        Entidad objetivo = new Entidad(new CMagoAuror("Aliado"), new RBandoToken(0, "A"), new RCoordenada(0, 1));

        assertTrue(lanzador.usarHabilidad(FLibroDeHechizos.PROTEGO.getPrototipo(), objetivo));
        assertTrue(objetivo.estaProtegido());

        assertTrue(lanzador.usarHabilidad(FLibroDeHechizos.STUPEFY.getPrototipo(), objetivo));
        assertTrue(objetivo.estaAturdido());
    }
}
