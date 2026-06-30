package sim;


public interface ICasteable {  
    String getNombre();
    String getNombreHabilidad();
	int getCosteRecurso();
	int getDistanciaAtaque();
	int getValorEspecifico();
	
	ITurnable getComportamiento();
    void ejecutar(IAfectable lanzador, IAfectable objetivo);
	
	void setValorEspecifico(int nuevoValor);
	void setBonificacion(double proporcion);
	void setNuevoComportamiento(IEstrategiaHabilidad nuevaEstrategia);
	
    boolean esHabilidadOfensiva();
	boolean esHabilidadTurnable();
    boolean esHabilidadEspecial();
}
