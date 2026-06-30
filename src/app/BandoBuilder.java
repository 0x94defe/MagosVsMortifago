package app;
import sim.BandoManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hogwarts.EFaccion;


public class BandoBuilder {
	private record Relacion(int bandoA, int bandoB, int tipo) {}
	
	public static final int AMIGO = BandoManager.AMIGO;
    public static final int NEUTRAL = BandoManager.NEUTRAL;
    public static final int ENEMIGO = BandoManager.ENEMIGO;
    private int id_autogen = 0;
    private final Map<String, Integer> bandosRegistrados = new LinkedHashMap<>();
    private List<Relacion> relaciones = new ArrayList<>();


    public BandoBuilder registrarBando(String nombre) {
        String nombreNormalizado = nombre.toUpperCase();
        
        if (bandosRegistrados.containsKey(nombreNormalizado) == false) {
        	bandosRegistrados.put(nombreNormalizado, id_autogen);
        	id_autogen++;
        }
        
        return this;
    }
    public BandoBuilder registrarBando(EFaccion faccion) {
        return this.registrarBando(faccion.name());
    }
    
    public BandoBuilder definirRelacion(String bandoA, String bandoB, int tipoRelacion) {
    	Integer idBandoA = bandosRegistrados.get(bandoA.toUpperCase());
    	Integer idBandoB = bandosRegistrados.get(bandoB.toUpperCase());
    	
    	if (idBandoA == null || idBandoB == null)
    		throw new IllegalArgumentException("Hay un bando que no existe, revisar.");
    	
    	relaciones.add(new Relacion(idBandoA, idBandoB,tipoRelacion));
    	
        return this;
    }
    public BandoBuilder definirRelacion(EFaccion bandoA, EFaccion bandoB, int tipoRelacion) {
        return this.definirRelacion(bandoA.name(), bandoB.name(), tipoRelacion);
    }
    
    public BandoManager buildBando() {
        int cantidad = bandosRegistrados.size();
        int[][] matriz = new int[cantidad][cantidad];


        for (int i = 0; i < cantidad; i++) {
            java.util.Arrays.fill(matriz[i], NEUTRAL);
            matriz[i][i] = 1;
        }

        for (Relacion r : relaciones) {
            matriz[r.bandoA][r.bandoB] = r.tipo;
            matriz[r.bandoB][r.bandoA] = r.tipo;
        }

        String[] nombresOrdenados = new String[cantidad];
        for (Map.Entry<String, Integer> entry : bandosRegistrados.entrySet()) {
            nombresOrdenados[entry.getValue()] = entry.getKey();
        }
        return new BandoManager(nombresOrdenados, matriz);
    }


    public int getId(String nombreBando) {
    	Integer id = bandosRegistrados.get(nombreBando.toUpperCase());
        if (id == null) {
            throw new IllegalArgumentException("El bando '" + nombreBando + "' no existe en esta partida.");
        }
        return id;
    }
    public int getId(EFaccion faccion) {
        return this.getId(faccion.name());
    }
}
