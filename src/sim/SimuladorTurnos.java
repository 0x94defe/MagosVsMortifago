package sim;
import hogwarts.AHabilidad;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SimuladorTurnos {
    List<Entidad> entidades;
    Map<Integer, Integer> bandosConEntidadesVivas;
    private final Grilla grilla;
    private final Bando bando;
    private Entidad entidadActual;
    private EEstadoJuego estadoJuego;
    private int rondaActual;
    private int indiceActual;

    public SimuladorTurnos(Escenario escena, int ancho, int alto) {
        if (ancho <= 0 || ancho >= 50 || alto <= 0 || alto >= 50)
            throw new IllegalArgumentException("Valores de grilla no permitidos");

        this.entidades = new ArrayList<>();
        this.bandosConEntidadesVivas = new LinkedHashMap<>();
        this.grilla = new Grilla(ancho, alto);
        this.bando = escena.getBandosDisponibles();
        this.estadoJuego = EEstadoJuego.EN_CURSO;
        this.rondaActual = 1;
        this.indiceActual = 0;

        for (Map.Entry<RCoordenada, RInstanciableConBando> entry : escena.getSpawnPoints().entrySet()) {
            RCoordenada c = entry.getKey();
            RInstanciableConBando r = entry.getValue();

            Entidad e = grilla.agregarEntidad(r.instanciable(), bando.getBandoToken(r.idBando()), c);
            if (e == null) throw new IllegalStateException("Mismatch en spawn point (" + c.posX() + "," + c.posY() + ")");

            entidades.add(e);
            if (e.puedeActuar())
                bandosConEntidadesVivas.put(r.idBando(), bandosConEntidadesVivas.getOrDefault(r.idBando(), 0) + 1);
        }

        procesarTurno();
    }

    // -------------------------------------------------------------------------
    // Gestión de turnos
    // -------------------------------------------------------------------------

    public void procesarTurno() {
        limpiarMuertos();
        if (estadoJuego != EEstadoJuego.EN_CURSO) {
            entidadActual = null;
            return;
        }
        entidadActual = buscarSiguiente();
        if (entidadActual != null) procesarEfectosInicioTurno();
    }

    public void terminarRonda() {
        System.out.println("Ronda #" + rondaActual + " terminada!!");
        rondaActual++;
        indiceActual = 0;
        procesarTurno();
    }
    
    public void saltarTurno() {
        System.out.println(entidadActual.getNombre() + " saltó su turno.");
        procesarTurno();
    }

    // Elimina de la lista y la grilla a todos los que murieron desde el último turno
    private void limpiarMuertos() {
        int i = 0;
        while (i < entidades.size()) {
            Entidad e = entidades.get(i);
            if (e.puedeActuar() && !e.estaVivo()) {
                System.out.println(e.getNombre() + " ha sido eliminado.");
                actualizarEstadoBandos(e.getIdBando());
                grilla.quitarEntidad(e);
                entidades.remove(i);
                // ajustar índice si el muerto era anterior al actual
                if (i < indiceActual) indiceActual--;
            } else {
                i++;
            }
        }
    }

    // Recorre desde indiceActual buscando la próxima entidad viva que puede actuar
    private Entidad buscarSiguiente() {
        while (indiceActual < entidades.size()) {
            Entidad e = entidades.get(indiceActual);
            indiceActual++;

            if (!e.puedeActuar()) continue; // obstáculos, objetos inertes
            if (!e.estaVivo())    continue; // no debería quedar ninguno tras limpiarMuertos

            System.out.println("Le toca a: " + e.getNombre());
            return e;
        }
        // Llegamos al final de la ronda sin encontrar nadie
        entidadActual = null;
        return null;
    }

    // Aplica efectos que ocurren al inicio del turno (aturdido, etc.)
    private void procesarEfectosInicioTurno() {
        if (entidadActual.estaAturdido()) {
            System.out.println(entidadActual.getNombre() + " está aturdido y pierde el turno.");
            entidadActual.consumirAturdido();
            // El aturdido consume el turno — avanzamos al siguiente
            entidadActual = buscarSiguiente();
            if (entidadActual != null) procesarEfectosInicioTurno();
        }
    }

    // -------------------------------------------------------------------------
    // Movimiento
    // -------------------------------------------------------------------------

    public boolean moverActual(int x, int y) {
        if (entidadActual == null) return false;
        boolean ok = grilla.moverEntidad(entidadActual, x, y);
        if (ok) System.out.println("Moviendo a: (" + (x+1) + ", " + (y+1) + ")");
        return ok;
    }

    // -------------------------------------------------------------------------
    // Habilidades
    // -------------------------------------------------------------------------

    public List<AHabilidad> getHabilidadesDisponiblesParaObjetivo(Entidad objetivo) {
        if (entidadActual == null) return List.of();

        boolean esEnemigo = bando.esEnemigo(entidadActual.getIdBando(), objetivo.getIdBando());

        List<AHabilidad> resultado = new ArrayList<>();
        for (AHabilidad h : entidadActual.getHabilidadesDisponibles()) {
            if (h.esHabilidadOfensiva() == esEnemigo) resultado.add(h);
        }
        return resultado;
    }

    public boolean estaEnRango(AHabilidad h, Entidad objetivo) {
        if (entidadActual == null) return false;
        return distancia(entidadActual, objetivo) <= h.getDistanciaAtaque();
    }

    public boolean lanzarHabilidad(AHabilidad h, Entidad objetivoIntentado) {
        if (entidadActual == null || !entidadActual.puedeActuar()) return false;

        Entidad objetivo = resolverLineaDeTiro(entidadActual, objetivoIntentado, h.getDistanciaAtaque());
        if (objetivo == null) return false;

        if (objetivo != objetivoIntentado)
            System.out.println("LoS interceptado! Impacta en: " + objetivo.getNombre());

        boolean fueLanzado = entidadActual.usarHabilidad(h, objetivo);
        if (fueLanzado) {
            System.out.println(h.getNombre() + " lanzado a: " + objetivo.getNombre());
            // Verificar victoria inmediatamente tras el ataque
            limpiarMuertos();
        } else {
            System.out.println("Sin recursos para: " + h.getNombre());
        }

        return fueLanzado;
    }

    // -------------------------------------------------------------------------
    // Condición de victoria
    // -------------------------------------------------------------------------

    private void actualizarEstadoBandos(int idBandoEliminado) {
        int restantes = bandosConEntidadesVivas.getOrDefault(idBandoEliminado, 0) - 1;
        bandosConEntidadesVivas.put(idBandoEliminado, Math.max(0, restantes));

        long bandosSinPersonajes = bandosConEntidadesVivas.values().stream()
                .filter(v -> v == 0).count();

        if (bandosSinPersonajes > 1) {
            estadoJuego = EEstadoJuego.EMPATE;
        } else if (bandosSinPersonajes == 1) {
            estadoJuego = EEstadoJuego.TERMINADO;
        } else {
            estadoJuego = EEstadoJuego.EN_CURSO;
        }
    }

    private String resolverGanadores() {
        StringBuilder sb = new StringBuilder();
        boolean primero = true;
        for (Map.Entry<Integer, Integer> entry : bandosConEntidadesVivas.entrySet()) {
            if (entry.getValue() > 0) {
                if (!primero) sb.append(", ");
                sb.append(bando.getBandoParticular(entry.getKey()));
                primero = false;
            }
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    private Entidad resolverLineaDeTiro(Entidad atacante, Entidad objetivo, int alcance) {
        if (distancia(atacante, objetivo) > alcance) return null;

        List<int[]> path = bresenham(
            atacante.getPosX(), atacante.getPosY(),
            objetivo.getPosX(), objetivo.getPosY()
        );

        for (int[] celda : path) {
            int cx = celda[0], cy = celda[1];
            if (cx == atacante.getPosX() && cy == atacante.getPosY()) continue;
            Entidad en = grilla.getEntidad(cx, cy);
            if (en != null) return en;
        }
        return objetivo;
    }

    private int distancia(Entidad a, Entidad b) { //distanciaChebyshev 
        return Math.max(
            Math.abs(a.getPosX() - b.getPosX()),
            Math.abs(a.getPosY() - b.getPosY())
        );
    }

    private List<int[]> bresenham(int x0, int y0, int x1, int y1) {
        List<int[]> path = new ArrayList<>();
        int dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int x = x0, y = y0;
        while (true) {
            path.add(new int[]{x, y});
            if (x == x1 && y == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 <  dx) { err += dx; y += sy; }
        }
        return path;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public Grilla getGrilla()           { return grilla; }
    public Entidad getEntidadActual()   { return entidadActual; }
    public List<Entidad> getEntidades() { return List.copyOf(entidades); }
    public int getRondaActual()         { return rondaActual; }
    public boolean esJuegoTerminado()   { return estadoJuego != EEstadoJuego.EN_CURSO; }
    public boolean esFindeTurno()       { return entidadActual == null; }
    public String getEstadoJuego() {
        return switch (estadoJuego) {
            case EN_CURSO   -> "La partida aún sigue en pie.";
            case EMPATE     -> "¡Empate! Todos los bandos han caído en combate.";
            case TERMINADO  -> "¡Los " + resolverGanadores() + " han ganado la guerra!";
        };
    }
}