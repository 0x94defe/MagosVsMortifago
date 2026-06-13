package tests;

import hogwarts.CMagoAuror;
import hogwarts.CPersonajeMago;
import hogwarts.CPersonajeMortifago;
import hogwarts.EFaccion;
import hogwarts.FLibroDeHechizos;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonajesTest {

    @Test
    void polimorfismo_debeRetornarFaccionYClaseCorrecta() {
        CPersonajeMago mago = new CMagoAuror("Auror");
        CPersonajeMortifago mortifago = new hogwarts.CMortifagoComandante("Comandante");

        assertEquals(EFaccion.MAGO, mago.getFaccion());
        assertEquals("Auror", mago.getClase());

        assertEquals(EFaccion.MORTIFAGO, mortifago.getFaccion());
        assertEquals("Comandante", mortifago.getClase());
    }

    @Test
    void construirHechizos_debeEvitarDuplicados() {
        CPersonajeMago mago = new CPersonajeMago(
            "Mago Test",
            null,
            100,
            100,
            2,
            FLibroDeHechizos.PROTEGO,
            FLibroDeHechizos.PROTEGO,
            FLibroDeHechizos.STUPEFY
        );

        String[] nombres = Arrays.stream(mago.getHechizosDisponibles())
            .map(h -> h.getNombre().trim().toUpperCase().replace(" ", "_"))
            .toArray(String[]::new);

        Set<String> distintos = Arrays.stream(nombres).collect(Collectors.toSet());
        assertEquals(distintos.size(), nombres.length);
        assertTrue(distintos.contains("PROTEGO"));
        assertTrue(distintos.contains("STUPEFY"));
    }
}
