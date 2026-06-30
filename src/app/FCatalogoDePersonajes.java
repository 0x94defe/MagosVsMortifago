package app;

import hogwarts.APersonaje;
import hogwarts.CPersonajeMago;
import hogwarts.CPersonajeMortifago;
import hogwarts.EFaccion;
import hogwarts.ENivel;

public enum FCatalogoDePersonajes {
    VOLDEMORT(new CPersonajeMortifago("Lord Voldemort", "El sin nariz, se la esnifo todas", ENivel.EXPERTO, 150, 200, 4,
    		FLibroDeHechizos.AVADA_KEDAVRA)),
    HARRY(new CPersonajeMago("Harry Potter", "Tiene una marca en la frente", ENivel.AVANZADO, 100, 100, 4,
    		FLibroDeHechizos.EXPELLIARMUS,
    		FLibroDeHechizos.EXPECTO_PATRONUM)),
    HERMIONE(new CPersonajeMago("Hermione Granger", "La mejor en su clase", ENivel.AVANZADO, 120, 90, 4,
    		FLibroDeHechizos.PROTEGO,
    		FLibroDeHechizos.STUPEFY));


    private final APersonaje prototipo;
    FCatalogoDePersonajes(APersonaje prototipo) { this.prototipo = prototipo; }

    public APersonaje construir() { return prototipo.clonar(); }
    public EFaccion getFaccion()  { return prototipo.getFaccion(); }
}
