package sim;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * EXTRAÍDO de Turnero — toda la responsabilidad de "quién actúa ahora,
 * en qué orden, y qué pasa cuando alguien muere o tiene un efecto activo".
 *
 * Turnero delega aquí y solo coordina movimiento/habilidades/victoria.
 */
public class GestorDeTurnos {
    private static final boolean TURNO_BATALLON = true;
    private static final int REGEN_SALUD_POR_RONDA  = 3;
    private static final int REGEN_RECURSO_POR_RONDA = 12;
    
    private final Grilla grilla;
    private final Map<Integer, List<Entidad>> bandoConEntidades;
    private final List<Entidad> turnoEntidades;

    private Entidad entidadActual;
    private int indiceActual;

    public GestorDeTurnos(Grilla grilla, BandoManager bandos, Map<Integer, List<Entidad>> bandoConEntidades) {
        this.grilla = grilla;
        this.bandoConEntidades = bandoConEntidades;
        this.indiceActual = 0;

        if (TURNO_BATALLON) {
            System.out.println("¡El orden de turno será POR BATALLÓN — primero un bando completo, luego el otro!");
            this.turnoEntidades = new ArrayList<>(generarTurnoEntidadesPorBatallon());
        } else {
            System.out.println("¡El orden de turno será INTERCALADO — cada bando alterna!");
            this.turnoEntidades = new ArrayList<>(generarTurnoEntidadesIntercalado());
        }

        sortearBandoInicial(bandos);
        System.out.println("################### Nuevo Juego comenzado!! ###################");
        avanzarYProcesarEfectos();
    }

    
    // -------------------------------------------------------------------------
    // Logica principal
    // -------------------------------------------------------------------------
	    public void procesarTurno(EEstadoJuego estadoJuego) {
	        if (estadoJuego != EEstadoJuego.EN_CURSO) {
	            entidadActual = null;
	            return;
	        }
	        avanzarYProcesarEfectos();
	    }
	
	    public void limpiarMuertosYResetearIndice() {
	        System.out.println("=================== Ronda terminada!! ===================");
	        limpiarMuertos();
	        indiceActual = 0;
	        
	        for (Entidad e : turnoEntidades) regenerarPorRonda(e);
	    }
	 
	    public void continuarTrasRonda() {
	        avanzarYProcesarEfectos();
	    }

	
	    public void saltarTurno() {
	    	avanzarYProcesarEfectos();
	    }
	
    // -------------------------------------------------------------------------
    // Logica Secundaria
    // -------------------------------------------------------------------------
	    public void limpiarMuertos() {
	        int i = 0;
	        while (i < turnoEntidades.size()) {
	            Entidad e = turnoEntidades.get(i);
	
	            if (e.getEstado() == EEstadoEntidad.MUERTO) {
	                System.out.println(e.getNombre() + " ha sido eliminado.");
	
	                int idBando = e.getIdBando();
	                if (bandoConEntidades.containsKey(idBando)) {
	                    bandoConEntidades.get(idBando).remove(e);
	                }
	
	                grilla.quitarEntidad(e);
	
	                // FIX: ahora también se remueve de turnoEntidades —
	                // antes esta línea no existía y la lista solo crecía.
	                turnoEntidades.remove(i);
	
	                if (i < indiceActual) indiceActual--;
	                // ya no se reasigna listaTemporal — esa línea no hacía nada
	                // porque turnoEntidades nunca cambiaba de referencia.
	            } else {
	                i++;
	            }
	        }
	    }
	
	    private Entidad buscarSiguiente() {
	        while (indiceActual < turnoEntidades.size()) {
	            Entidad e = turnoEntidades.get(indiceActual);
	            indiceActual++;
	
	            if (e.getEstado() == EEstadoEntidad.INERTE || e.getEstado() == EEstadoEntidad.MUERTO) continue;
	            System.out.print("Le toca a '" + e.getNombre() + "' --> ");
	            
	            return e;
	        }
	        entidadActual = null;
	        return null;
	    }
	    
	    private void avanzarYProcesarEfectos() {
	        entidadActual = buscarSiguiente();
	        if (entidadActual != null) procesarEfectosTurnoJugable();
	    }
    // -------------------------------------------------------------------------
    // Procesar efectos
    // -------------------------------------------------------------------------
	    private void regenerarPorRonda(Entidad e) {
	        if (!e.estaVivo()) return;
	        e.recibirCuracion(REGEN_SALUD_POR_RONDA);
	        e.recibirMana(REGEN_RECURSO_POR_RONDA);
	        System.out.println("'" + e.getNombre() + "' Regenero --> " + REGEN_SALUD_POR_RONDA + " HP, " + REGEN_RECURSO_POR_RONDA + " MP");
	    }
	    private void procesarEfectosTurnoJugable() {
	        boolean restringido = entidadActual.tieneEfectoQueRestringeAccion();

	        List<String> mensajesAntes = new ArrayList<>();
	        for (EfectoActivo efec : entidadActual.getEfectosActivos()) {
	            mensajesAntes.add(efec.getMensaje());
	        }

	        List<String> mensajes = procesarEfectos(entidadActual);
	        if (!mensajes.isEmpty()) {
	            System.out.println();
	            if (restringido) for (String msg : mensajesAntes) System.out.println("    ... " + msg);
	            for (String msg : mensajes) System.out.println("    ... " + msg);
	            if (!restringido) System.out.print("          '" + entidadActual.getNombre() + "' --> ");
	        }

	        if (restringido) {
	            entidadActual = buscarSiguiente();
	            if (entidadActual != null) procesarEfectosTurnoJugable();
	        }
	    }
	    private List<String> procesarEfectos(Entidad ent) {
	        List<String> mensajes = new ArrayList<>();
	        Iterator<EfectoActivo> it = ent.getEfectosActivos().iterator();

	        while (it.hasNext()) {
	            EfectoActivo efec = it.next();
	            efec.tick(ent);
	            //System.out.print("efecto: " + efec.getNombreEfecto() + " durac: " + efec.getDuracionRestante());
	            if (efec.estaAgotado()) {
	                efec.terminar(ent);
	                mensajes.add("dejo de recibir el efecto " + efec.getNombreEfecto());
	                it.remove();
	            } else {
	                mensajes.add("todavia siente el efecto de " + efec.getNombreEfecto());
	            }
	        }
	        return mensajes;
	    }

    // -------------------------------------------------------------------------
    // Randomizer de bando inicial
    // -------------------------------------------------------------------------
	    private void sortearBandoInicial(BandoManager bandos) {
	        List<Integer> idsBandos = new ArrayList<>(bandoConEntidades.keySet());
	        if (idsBandos.isEmpty()) return;
	
	        int idBandoInicial = idsBandos.get(new Random().nextInt(idsBandos.size()));
	        System.out.println("¡El bando '" + bandos.getBandoParticular(idBandoInicial) + "' comienza primero!");
	
	        turnoEntidades.sort(Comparator.comparing(e -> e.getIdBando() == idBandoInicial ? 0 : 1));
	    }

	    private List<Entidad> generarTurnoEntidadesPorBatallon() {
	        List<Entidad> todas = new ArrayList<>();
	        for (int idBando = 0; idBando < bandoConEntidades.size(); idBando++) {
	            List<Entidad> entidadesDelBando = bandoConEntidades.get(idBando);
	            if (entidadesDelBando != null) {
	                todas.addAll(entidadesDelBando);
	            }
	        }
	        return todas;
	    }

	    private List<Entidad> generarTurnoEntidadesIntercalado() {
	        List<Entidad> todas = new ArrayList<>();

	        int maxEntidadesEnUnBando = 0;
	        for (List<Entidad> lista : bandoConEntidades.values()) {
	            if (lista.size() > maxEntidadesEnUnBando) {
	                maxEntidadesEnUnBando = lista.size();
	            }
	        }

	        for (int i = 0; i < maxEntidadesEnUnBando; i++) {
	            for (int idBando = 0; idBando < bandoConEntidades.size(); idBando++) {
	                List<Entidad> entidadesDelBando = bandoConEntidades.get(idBando);
	                if (entidadesDelBando != null && i < entidadesDelBando.size()) {
	                    todas.add(entidadesDelBando.get(i));
	                }
	            }
	        }
	        return todas;
	    }
	    
    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------
	    public Entidad getEntidadActual() { return entidadActual; }
	    public boolean esFindeTurno()     { return entidadActual == null; }
}
