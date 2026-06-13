package hogwarts;

import java.util.Set;

public enum FLibroDeHechizos {
    EXPELLIARMUS(new CHechizo("Expelliarmus", "Ataque rapido de luz", 20, 2, Set.of(EFaccion.MAGO, EFaccion.MORTIFAGO), ETipoHabilidad.DESTRUCTIVO, 10, BHechizos::aplicarExpelliarmus)),
    AVADA_KEDAVRA(new CHechizo("Avada Kedavra", "Maldicion letal de magia oscura", 100, 2, Set.of(EFaccion.MORTIFAGO), ETipoHabilidad.DESTRUCTIVO, 200, BHechizos::aplicarAvadaKedavra)),
    EXPECTO_PATRONUM(new CHechizo("Expecto Patronum", "Encantamiento de luz que restaura vitalidad", 50, 2, Set.of(EFaccion.MAGO), ETipoHabilidad.CURATIVO, 35, BHechizos::aplicarExpectoPatronum)),
    ET_LADRONEN(new CHechizo("Et Ladronen", "Drena mana del objetivo", 15, 2, Set.of(EFaccion.MAGO), ETipoHabilidad.ROBAR_MANA, 50, BHechizos::aplicarEtLadronen)),
    PROTEGO(new CHechizoEncantamiento("Protego", "Genera una barrera protectora temporal", 50, 2, Set.of(EFaccion.MAGO), ETipoEncantamiento.ESCUDO, 1, BHechizos::aplicarProtego)),
    STUPEFY(new CHechizoEncantamiento("Stupefy", "Aturde al objetivo y le hace perder el turno", 15, 2, Set.of(EFaccion.MAGO), ETipoEncantamiento.ATURDIR, 1, BHechizos::aplicarStupefy));


    private final CHechizo prototipo;
    FLibroDeHechizos(CHechizo prototipo) { 
        this.prototipo = prototipo; 
    }

    public Set<EFaccion> getFacciones() { 
        return prototipo.getFaccionesPermitidas(); 
    }
    public CHechizo construir() { 
        return prototipo.clonar(); 
    }
    public AHabilidad getPrototipo() { 
        return prototipo; 
    }
    public String getDescripcion() { 
        return prototipo.toString(); 
    }
}
