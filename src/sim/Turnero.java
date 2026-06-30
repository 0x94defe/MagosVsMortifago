package sim;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Turnero {
    private final String nombreDelMapa;
    private final Grilla grilla;
    private final BandoManager bandos;
    private final GestorDeTurnos gestorTurnos;
    private final Map<Integer, List<Entidad>> bandoConEntidades;

    private List<String> secuenciaDeAcciones; //se pide en el tp
    private Map<Entidad, ICasteable> hechizosLanzadosPorPersonaje; //se pide en el tp
    private Map<Integer, Set<ICasteable>> hechizosLanzadosPorBando; //se pide en el tp

    private EEstadoJuego estadoJuego;
    private int rondaActual;

    public Turnero(String nombreDelMapa, Grilla grilla, BandoManager bandos, Map<Integer, List<Entidad>> bandoConEntidades) {
        this.nombreDelMapa = nombreDelMapa;
        this.grilla = grilla;
        this.bandos = bandos;
        this.bandoConEntidades = bandoConEntidades;

        // GestorDeTurnos resuelve el orden de turno, el randomizer de bando inicial, y deja la primera entidadActual asignada.
        this.gestorTurnos = new GestorDeTurnos(grilla, bandos, bandoConEntidades);

        this.secuenciaDeAcciones = new ArrayList<>();
        this.hechizosLanzadosPorPersonaje = new HashMap<>();
        this.hechizosLanzadosPorBando = new HashMap<>();

        this.estadoJuego = EEstadoJuego.EN_CURSO;
        this.rondaActual = 1;
    }

    
    // -------------------------------------------------------------------------
    // Logica principal
    // -------------------------------------------------------------------------
	    public void procesarTurno() {
	        gestorTurnos.procesarTurno(estadoJuego);
	    }
	
	    public void terminarRonda() {
	        gestorTurnos.limpiarMuertosYResetearIndice();
	        actualizarEstadoJuego();
	 
	        if (estadoJuego != EEstadoJuego.EN_CURSO) {
	            System.out.println("=================== Juego terminado ===================");
	            return;
	        }
	        
		    rondaActual++;
	        System.out.println("=================== Ronda #" + rondaActual + " ===================");
	        gestorTurnos.continuarTrasRonda();
	        hechizosLanzadosPorPersonaje.clear();
	        hechizosLanzadosPorBando.clear();
	        secuenciaDeAcciones.clear();
	    }

	
	    public void saltarTurno() {
	        String secuencia =  "saltó su turno.";
	        secuenciaDeAcciones.add(gestorTurnos.getEntidadActual().getNombre() + " " + secuencia);
	        System.out.println(secuencia);
	        gestorTurnos.saltarTurno();
	    }
	
	    public boolean moverActual(int x, int y) {
	        Entidad entidadActual = gestorTurnos.getEntidadActual();
	        if (entidadActual == null) return false;
	
	        boolean ok = grilla.moverEntidad(entidadActual, x, y);
	        if (ok) {
	            String secuencia = "Moviendo a (" + (x + 1) + ", " + (y + 1) + ")";
	            secuenciaDeAcciones.add(gestorTurnos.getEntidadActual().getNombre() + " " + secuencia);
	            System.out.println(secuencia);
	        }
	        return ok;
	    }
    // -------------------------------------------------------------------------
    // Habilidades
    // -------------------------------------------------------------------------
	    public List<ICasteable> getHabilidadesDisponiblesParaObjetivo(IObservable objetivo) {
	        Entidad entidadActual = gestorTurnos.getEntidadActual();
	        if (entidadActual == null) return List.of();

	        boolean esEnemigo = bandos.esEnemigo(entidadActual.getIdBando(), objetivo.getIdBando());
	        boolean esAutoTarget = entidadActual == objetivo;
	        Set<ICasteable> hechizosLanzados = hechizosLanzadosPorBando.getOrDefault(entidadActual.getIdBando(), Set.of());

	        List<ICasteable> resultado = new ArrayList<>();
	        for (ICasteable h : entidadActual.getHabilidadesDisponibles()) {
	            boolean esSoloPersonal = h.getDistanciaAtaque() == 0;

	            if (esSoloPersonal && !esAutoTarget) continue; // distancia 0, solo a uno mismo
	            if (!esSoloPersonal && h.esHabilidadOfensiva() != esEnemigo) continue; // filtro normal de bando

	            if (!hechizosLanzados.contains(h)) resultado.add(h);
	        }
	        return resultado;
	    }
	
	    public boolean estaEnRango(ICasteable h, IObservable objetivoObservable) {
	        Entidad entidadActual = gestorTurnos.getEntidadActual();
	        if (entidadActual == null) return false;
	        Entidad objetivo = grilla.getEntidad(objetivoObservable.getPosX(), objetivoObservable.getPosY());
	        return grilla.calcularDistancia(entidadActual, objetivo) <= h.getDistanciaAtaque();
	    }
	
	    public boolean lanzarHabilidad(ICasteable h, IObservable objetivoObservable) {
	        Entidad entidadActual = gestorTurnos.getEntidadActual();
	        if (entidadActual == null) return false;
	        Entidad objetivoIntentado = grilla.getEntidad(objetivoObservable.getPosX(), objetivoObservable.getPosY());
	
	        EEstadoEntidad estado = entidadActual.getEstado();
	        if (estado == EEstadoEntidad.SILENCIADO || estado == EEstadoEntidad.ATURDIDO) {
	            System.out.println(entidadActual.getNombre() + " no puede lanzar habilidades porque está " + estado);
	            return false;
	        }
	
	        Entidad objetivoImpactado = grilla.resolverLineaDeTiro(entidadActual, objetivoIntentado, h.getDistanciaAtaque());
	        if (objetivoImpactado != objetivoIntentado) {
	            System.out.println("LoS interceptado! Impacta en: " + objetivoImpactado.getNombre());
	        }
	
	        REntidadSnapshot fotoLanzadorAntes = entidadActual.crearSnapshot();
	        REntidadSnapshot fotoObjetivoAntes = objetivoImpactado.crearSnapshot();
	        
	        boolean fueLanzado = entidadActual.usarHabilidad(h, objetivoImpactado);
	        if (fueLanzado) {
	            String secuencia = h.getNombre() + " lanzado a: " + objetivoImpactado.getNombre();
	            secuenciaDeAcciones.add(gestorTurnos.getEntidadActual().getNombre() + " uso " + secuencia);
	            System.out.println(secuencia);
	
	            hechizosLanzadosPorPersonaje.put(entidadActual, h);
	            hechizosLanzadosPorBando.computeIfAbsent(entidadActual.getIdBando(), id -> new HashSet<>()).add(h);
	            
	            imprimirReporteDeBatalla(entidadActual, objetivoImpactado, h, fotoLanzadorAntes, fotoObjetivoAntes);
	        }
	
	        return fueLanzado;
	    }

	    private void imprimirReporteDeBatalla(Entidad lanzador, Entidad objetivo, ICasteable h, 
                                              REntidadSnapshot antesLanzador, REntidadSnapshot antesObjetivo)
	    {

			//int danioRecibido = antesObjetivo.salud() - objetivo.getPuntosSalud();
			//int manaGastado = antesLanzador.mana() - lanzador.getPuntosMana();
			//System.out.println("Daño real infligido: " + danioRecibido);
			
			int valorHechizo = lanzador.getHabilidadPersonal(h).getValorEspecifico();
			
			if (h.esHabilidadOfensiva() && valorHechizo >= 60) {
		        System.out.println("'" + lanzador.getNombre() + "' realizó un Ataque Crítico!");
		        
		        if (antesObjetivo.bloqueaDanio()) {
		            System.out.println("¡No surgió efecto al lanzar su habilidad!");
		        } else if (objetivo.getPuntosSalud() <= 5) {
		            System.out.println("'" + objetivo.getNombre() + "' quedó herido de gravedad.");
		        } else if (objetivo.getPuntosSalud() <= 25) {
		            System.out.println("'" + objetivo.getNombre() + "' quedó muy mal herido.");
		        }
		    }
	    }
    // -------------------------------------------------------------------------
    // Condición de victoria
    // -------------------------------------------------------------------------
	    private void actualizarEstadoJuego() {
	        List<Integer> bandosActivos = new ArrayList<>();
	        
	        for (Map.Entry<Integer, List<Entidad>> entry : bandoConEntidades.entrySet()) {
	            if (!entry.getValue().isEmpty()) bandosActivos.add(entry.getKey());
	        }

	        int gruposDistintos = contarGruposDeAlianza(bandosActivos);

	        estadoJuego = switch (gruposDistintos) {
	            case 0  -> EEstadoJuego.EMPATE;
	            case 1  -> EEstadoJuego.TERMINADO;
	            default -> EEstadoJuego.EN_CURSO;
	        };
	    }

	    private int contarGruposDeAlianza(List<Integer> bandosActivos) {
	        List<Set<Integer>> grupos = new ArrayList<>();

	        for (int idBando : bandosActivos) {
	            Set<Integer> grupoExistente = null;
	            
	            for (Set<Integer> grupo : grupos) {
	                if (grupo.stream().anyMatch(otro -> bandos.esAmigo(idBando, otro))) {
	                    grupoExistente = grupo;
	                    break;
	                }
	            }
	            
	            if (grupoExistente != null) {
	            	grupoExistente.add(idBando);
	        	} else {
	                Set<Integer> nuevo = new HashSet<>();
	                nuevo.add(idBando);
	                grupos.add(nuevo);
	            }
	        }
	        
	        return grupos.size();
	    }
	
	    private String resolverGanadores() {
	        StringBuilder sb = new StringBuilder();
	        boolean primero = true;
	
	        for (Map.Entry<Integer, List<Entidad>> entry : bandoConEntidades.entrySet()) {
	            List<Entidad> entidadesDelBando = entry.getValue();
	            if (!entidadesDelBando.isEmpty()) {
	                if (!primero) sb.append(", ");
	                sb.append(bandos.getBandoParticular(entry.getKey()));
	                primero = false;
	            }
	        }
	        
	        return sb.toString();
	    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------
	    public IGrillaConsulta getGrilla()     { return grilla; }
	    public IObservable getEntidadActual()  { return gestorTurnos.getEntidadActual(); }
	    public String getNombreMapa()          { return nombreDelMapa; }
	    public int getRondaActual()            { return rondaActual; }
	    public boolean esJuegoTerminado()      { return estadoJuego != EEstadoJuego.EN_CURSO; }
	    public boolean esFindeTurno()          { return gestorTurnos.esFindeTurno(); }
	
	
	    public String getReporteSecuenciaDeAcciones() {
	        if (secuenciaDeAcciones.isEmpty()) return "Sin acciones registradas todavía.";
	        StringBuilder sb = new StringBuilder();
	        for (String accion : secuenciaDeAcciones) {
	            sb.append("- ").append(accion).append("\n");
	        }
	        return sb.toString();
	    }

	    public String getReporteHechizosPorPersonaje() {
	        if (hechizosLanzadosPorPersonaje.isEmpty()) return "Nadie lanzó hechizos todavía esta ronda.";
	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<Entidad, ICasteable> entry : hechizosLanzadosPorPersonaje.entrySet()) {
	            sb.append(entry.getKey().getNombre()).append(" => ").append(entry.getValue().getNombre()).append("\n");
	        }
	        return sb.toString();
	    }

	    public String getReporteHechizosPorBando() {
	        if (hechizosLanzadosPorBando.isEmpty()) return "Ningún bando lanzó hechizos todavía esta ronda.";
	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<Integer, Set<ICasteable>> entry : hechizosLanzadosPorBando.entrySet()) {
	            sb.append(bandos.getBandoParticular(entry.getKey())).append(":\n");
	            for (ICasteable h : entry.getValue()) {
	                sb.append("   - ").append(h.getNombre()).append("\n");
	            }
	        }
	        return sb.toString();
	    }
	
	    public String getEstadoJuego() {
	        return switch (estadoJuego) {
	            case EN_CURSO  -> "La partida aún sigue en pie.";
	            case EMPATE    -> "¡Empate! Todos los bandos han caído en combate.";
	            case TERMINADO -> "¡Los " + resolverGanadores() + " han ganado la guerra!";
	        };
	    }
}
