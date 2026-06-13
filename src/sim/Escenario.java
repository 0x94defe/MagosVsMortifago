package sim;

import hogwarts.CPersonajeMago;
import hogwarts.CPersonajeMortifago;
import hogwarts.APersonaje;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Escenario {
	private final String nombre;
	private final Bando bandos;
	private final Map<RCoordenada, RInstanciableConBando> spawnPoints;
    private final List<CPersonajeMago> magos;
    private final List<CPersonajeMortifago> mortifagos;
    //agregar objetos
    //agregar critters

    public Escenario(String nombre, Bando bandos) {
    	this.nombre = nombre;
    	this.bandos = bandos;
        this.spawnPoints = new HashMap<>();
        this.magos = new ArrayList<>();
        this.mortifagos = new ArrayList<>();
    }


    public boolean registrarMago(CPersonajeMago pj, int idBando, int x, int y) {
        if (agregarAlMapa(pj, idBando, x, y)) {
            magos.add(pj);
            return true;
        }
        return false;
    }
    public boolean registrarMortifago(CPersonajeMortifago pj, int idBando, int x, int y) {
        if (agregarAlMapa(pj, idBando, x, y)) {
            mortifagos.add(pj);
            return true;
        }
        return false;
    }
    private boolean agregarAlMapa(IInstanciable i, int idBando, int x, int y) {
        RCoordenada coor = new RCoordenada(x, y);
        return spawnPoints.putIfAbsent(coor, new RInstanciableConBando(i, idBando)) == null;
    }

    public String getNombre() { 
        return nombre; 
    }

    public Bando getBandosDisponibles() { 
        return bandos; 
    }

    public Map<RCoordenada, RInstanciableConBando> getSpawnPoints() { 
        return this.spawnPoints; 
    }

    public List<CPersonajeMortifago> getMortifagos() { 
        return this.mortifagos; 
    }

    public List<CPersonajeMago> getMagos() { 
        return this.magos; 
    }

    public List<APersonaje> getTodosLosPersonajes() {
        List<APersonaje> todos = new ArrayList<>();
        
        todos.addAll(this.magos);
        todos.addAll(this.mortifagos);
        
        return todos;
    }
    
    public List<IInstanciable> getTodasLosInstanciables() {
        List<IInstanciable> listaDesenvuelta = new ArrayList<>();

        for (RInstanciableConBando record : spawnPoints.values()) {
            listaDesenvuelta.add(record.instanciable());
        }
        
        return listaDesenvuelta;
    }
}
