package sim;

import java.util.function.BiConsumer;

public interface ICasteable {  
    public String getNombre();
    public String getTipoHabilidad();
	public int getCosteRecurso();
	public int getDistanciaAtaque();
	public int getValorEspecifico();
	public ITurnable getComportamiento();
	
	public void setValorEspecifico(int nuevoValor);
	public void setNuevoComportamiento(BiConsumer<IAfectable, IAfectable> nuevaEstrategia);
	
    public void ejecutar(IAfectable lanzador, IAfectable objetivo);
    public boolean esHabilidadOfensiva();
	public boolean esHabilidadTurnable();
    public boolean esHabilidadEspecial();
}
