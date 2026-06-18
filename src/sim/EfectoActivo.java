package sim;

public class EfectoActivo {
	 private final ITurnable comportamiento;
	 private int duracionRestante;

	 public EfectoActivo(ITurnable comportamiento, int duracion) {
	     this.comportamiento = comportamiento;
	     this.duracionRestante = duracion;
	 }
	
	 @Override
	 public String toString() { return comportamiento.toString() + "-" + duracionRestante; }
	 
	 public void iniciar(IAfectable objetivo) {
	     comportamiento.alIniciar(objetivo);
	 }
	
	 public void tick(IAfectable objetivo) {
	    if (estaAgotado()) return;
	    
	    if (comportamiento.debeTerminar(objetivo)) {
	        duracionRestante = 0;
	        return;
	    }
	    
	    comportamiento.alConsumirse(objetivo, duracionRestante);
	    
	    if (duracionRestante > 0) duracionRestante--;
	}

	public void terminar(IAfectable objetivo) {
	    comportamiento.alTerminar(objetivo);
	}
	
	 public boolean restringeAccion()  { return comportamiento.restringeAccion(); }
	 public boolean estaAgotado()      { return duracionRestante == 0; }
	 public int getDuracionRestante()  { return duracionRestante; }
	 public String getNombreEfecto() { return comportamiento.toString(); }
	 public String getMensaje() { return comportamiento.getDescripcionEfecto(); }
}
