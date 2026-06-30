package sim;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Escenario implements IEscenarioConsulta {
	private record RCoordenada(int posX, int posY) {}
	private record RInstanciableConBando(IInstanciable instanciable, int idBando) {}
	
	private final int ancho;
	private final int alto;
	private final String nombreDelMapa;
	private final BandoManager politicas;
	private final Map<RCoordenada, RInstanciableConBando> spawnPoints;
    private final List<IInstanciable> personajes;

    //agregar objetos
    //agregar critters

    public Escenario(int ancho, int alto, String nombreDelMapa, BandoManager bandos) {
        if (ancho <= 0 || ancho >= 50 || alto <= 0 || alto >= 50)
            throw new IllegalArgumentException("Valores de grilla no permitidos");
    	
        this.ancho = ancho;
        this.alto = alto;
    	this.nombreDelMapa = nombreDelMapa;
    	this.politicas = bandos;
        this.spawnPoints = new HashMap<>();
        this.personajes = new ArrayList<>();
    }

    //constructores
    public Turnero crearSimulacion() {
    	Grilla grillaNueva = new Grilla(ancho, alto);
        Map<Integer, List<Entidad>> bandoConEntidades = new HashMap<>();

    	for (Map.Entry<RCoordenada, RInstanciableConBando> e : spawnPoints.entrySet()) {
            RCoordenada coor = e.getKey();
            RInstanciableConBando spawn = e.getValue();

            Entidad ent = new Entidad(spawn.instanciable(), politicas.getBandoToken(spawn.idBando()));
            
            boolean pudoAgregar = grillaNueva.agregarEntidad(ent, coor.posX(), coor.posY());
            if (!pudoAgregar) throw new IllegalStateException(
            		"No hay espacio para spawnear a " + ent.getNombre() + ". Mismatch en spawn point (" + coor.posX() + "," + coor.posY() + ")"
            );

            if (ent.puedeActuar()) {
            	List<Entidad> acumParcial = bandoConEntidades.getOrDefault(spawn.idBando(), null);
            	if (acumParcial == null) acumParcial = new ArrayList<>();
            	acumParcial.add(ent);
            	bandoConEntidades.put(spawn.idBando(), acumParcial);
            }
        }
        
        
        return new Turnero(nombreDelMapa, grillaNueva, politicas, bandoConEntidades);
    }
    
    //helpers
    private boolean agregarAlMapa(IInstanciable i, int idBando, int x, int y) {
        RCoordenada coor = new RCoordenada(x, y);
        return spawnPoints.putIfAbsent(coor, new RInstanciableConBando(i, idBando)) == null;
    }

    //mutadores
    public boolean registrarPj(IInstanciable pj, int idBando, int x, int y) {
        if (agregarAlMapa(pj, idBando, x, y)) {
            personajes.add(pj);
            return true;
        }
        return false;
    }

    
   
    //getters
    public Map<RCoordenada, RInstanciableConBando> getSpawnPoints() { return spawnPoints; }
    public List<IInstanciable> getTodosLosPersonajes() { return personajes; }  
    public List<IInstanciable> getTodasLosInstanciables() {
        List<IInstanciable> listaDesenvuelta = new ArrayList<>();

        for (RInstanciableConBando record : spawnPoints.values()) {
            listaDesenvuelta.add(record.instanciable());
        }
        
        return listaDesenvuelta;
    }

    // interfaz
    public String getNombre() { return nombreDelMapa; }
	public String getDescripcionDeTodosLosPersonajes() {
		StringBuilder sbPersonajes = new StringBuilder();
		
		for (IInstanciable p : personajes) {
			sbPersonajes.append("===================================\n");
			sbPersonajes.append(p.toString()).append("\n");
		}
		
		return sbPersonajes.toString();
	}
}
