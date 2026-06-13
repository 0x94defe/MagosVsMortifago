package hogwarts;

public enum FCatalogoDePersonajes {
    VOLDEMORT(new CPersonajeMortifago("Lord Voldemort", "El sin nariz", 150, 200, 4,
    		FLibroDeHechizos.AVADA_KEDAVRA)),
    HARRY(new CPersonajeMago("Harry Potter", "Tiene una marca en la frente", 100, 100, 4,
    		FLibroDeHechizos.EXPELLIARMUS,
    		FLibroDeHechizos.EXPECTO_PATRONUM)),
    HERMIONE(new CPersonajeMago("Hermione Granger", "La mejor en su clase", 120, 90, 4,
    		FLibroDeHechizos.PROTEGO,
    		FLibroDeHechizos.STUPEFY));


    private final APersonaje prototipo;

    FCatalogoDePersonajes(APersonaje prototipo) { 
        this.prototipo = prototipo; 
    }

    public APersonaje construir() { 
        return prototipo.clonar(); 
    }
    public EFaccion getFaccion() { 
        return prototipo.getFaccion(); 
    }
}
