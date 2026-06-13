package app;
import sim.Bando;
import sim.ETipoRelacion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BandoManager {
	private record Relacion(int bandoA, int bandoB, int tipo) {}
	
	public static final int AMIGO = ETipoRelacion.AMIGO.getValor();
    public static final int NEUTRAL = ETipoRelacion.NEUTRAL.getValor();
    public static final int ENEMIGO = ETipoRelacion.ENEMIGO.getValor();
    private int id_autogen = 0;
    private final Map<String, Integer> bandosRegistrados = new LinkedHashMap<>();
    private List<Relacion> relaciones = new ArrayList<>();


    public BandoManager registrarBando(String nombre) {
        String nombreNormalizado = nombre.toUpperCase();
        
        if (bandosRegistrados.containsKey(nombreNormalizado) == false) {
        	bandosRegistrados.put(nombreNormalizado, id_autogen);
        	id_autogen++;
        }
        
        return this;
    }
    public BandoManager definirRelacion(String bandoA, String bandoB, int tipoRelacion) {
    	Integer idBandoA = bandosRegistrados.get(bandoA.toUpperCase());
    	Integer idBandoB = bandosRegistrados.get(bandoB.toUpperCase());
    	
    	if (idBandoA == null || idBandoB == null)
    		throw new IllegalArgumentException("Hay un bando que no existe, revisar.");
    	
    	relaciones.add(new Relacion(idBandoA, idBandoB,tipoRelacion));
    	
        return this;
    }
    public Bando buildBando() {
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
        return new Bando(nombresOrdenados, matriz);
    }


    public int getId(String nombreBando) {
    	Integer id = bandosRegistrados.get(nombreBando.toUpperCase());
        if (id == null) {
            throw new IllegalArgumentException("El bando '" + nombreBando + "' no existe en esta partida.");
        }
        return id;
    }
}
