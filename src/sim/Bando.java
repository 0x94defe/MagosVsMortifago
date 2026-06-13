package sim;

import java.util.Arrays;


public class Bando {
	private String[] nombreBandos;
    private final int[][] matrizRelaciones; // [ID_Origen][ID_Destino] = Tipo de Relación (ej. 1=Amigo, -1=Enemigo, 0=Neutral)
	
    public Bando(String[] nombres, int[][] matrizRelaciones) {
    	this.nombreBandos = nombres;
        this.matrizRelaciones = matrizRelaciones;
    }
    
    //---------------
    public String[] getBandosDisponibles() { 
      return Arrays.copyOf(nombreBandos, nombreBandos.length); 
    }

    public RBandoToken getBandoToken(int pos) {
    	if (pos >= nombreBandos.length || pos < 0){
        throw new IllegalArgumentException("Fuera de rango");
      }
    	
    	return new RBandoToken(pos, nombreBandos[pos]);
    }
    
    //---------------
    public int getCantidadBandos() { 
      return nombreBandos.length; 
    }

    public String getBandoParticular(int pos) {
    	if (pos >= nombreBandos.length || pos < 0) {
        throw new IllegalArgumentException("Fuera de rango");
      }
    	
    	return nombreBandos[pos]; 
    }
    
    //---------------
    public ETipoRelacion getRelacion(int miBandoId, int bandoObjetivoId) {
    	if (miBandoId >= nombreBandos.length || miBandoId < 0 || bandoObjetivoId >= nombreBandos.length || bandoObjetivoId < 0) {
        throw new IllegalArgumentException("Fuera de rango");
      }    
    	
    	int ordinal = matrizRelaciones[miBandoId][bandoObjetivoId] + 1;
    	return ETipoRelacion.values()[ordinal];
    }

    public boolean esAmigo(int a, int b) { 
      return getRelacion(a, b) == ETipoRelacion.AMIGO; 
    }

    public boolean esEnemigo(int a, int b) { 
      return getRelacion(a, b) == ETipoRelacion.ENEMIGO; 
    }
}
