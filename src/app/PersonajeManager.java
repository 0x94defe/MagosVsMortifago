package app;
import hogwarts.FCatalogoDePersonajes;
import hogwarts.APersonaje;
import hogwarts.CPersonajeMago;
import hogwarts.CPersonajeMortifago;
import hogwarts.EFaccion;

import java.util.EnumMap;
import java.util.Map;


public class PersonajeManager {
    private final Map<FCatalogoDePersonajes, APersonaje> instancias = new EnumMap<>(FCatalogoDePersonajes.class);


    public CPersonajeMago crearMagoSiNoExiste(FCatalogoDePersonajes tipo) {
    	if (tipo.getFaccion() != EFaccion.MAGO) 
    		throw new IllegalArgumentException("No coincide el personaje con su faccion");
    	
    	APersonaje pm = tipo.construir();
    	instancias.put(tipo, pm);
    	return (CPersonajeMago) pm;
    }
    public CPersonajeMortifago crearMortifagoSiNoExiste(FCatalogoDePersonajes tipo) {
    	if (tipo.getFaccion() != EFaccion.MORTIFAGO) 
    		throw new IllegalArgumentException("No coincide el personaje con su faccion");
    	
    	APersonaje pm = tipo.construir();
    	instancias.put(tipo, pm);
    	return (CPersonajeMortifago) pm;
    }
    
    public APersonaje obtener(FCatalogoDePersonajes tipo) { return instancias.get(tipo); } 
    public void eliminar(FCatalogoDePersonajes tipo) { instancias.remove(tipo); }
    public Map<FCatalogoDePersonajes, APersonaje> getPersonajesInstanciados() { return Map.copyOf(instancias); }
}