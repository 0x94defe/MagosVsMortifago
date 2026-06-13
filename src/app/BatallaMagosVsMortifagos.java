package app;

import hogwarts.AHabilidad;
import sim.Entidad;
import sim.Escenario;
import sim.SimuladorTurnos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BatallaMagosVsMortifagos {

    public static void main(String[] args) {
        Escenario escenario = EscenariosPrefab.guerraCompleta();
        SimuladorTurnos sim = new SimuladorTurnos(escenario, 10, 10);
        Random random = new Random();

        int maxIteraciones = 500;
        int iteracion = 0;

        while (!sim.esJuegoTerminado() && iteracion < maxIteraciones) {
            if (sim.esFindeTurno()) {
                sim.terminarRonda();
                iteracion++;
                continue;
            }

            Entidad actual = sim.getEntidadActual();
            if (actual == null) {
                sim.terminarRonda();
                iteracion++;
                continue;
            }

            MovimientoDecision decision = buscarDecisionDeAtaque(sim, actual, random);
            if (decision != null) {
                sim.lanzarHabilidad(decision.habilidad(), decision.objetivo());
            }

            sim.saltarTurno();
            iteracion++;
        }

        if (sim.esJuegoTerminado()) {
            System.out.println(sim.getEstadoJuego());
        } else {
            System.out.println("La simulacion se detuvo por limite de iteraciones.");
            System.out.println(sim.getEstadoJuego());
        }
    }

    private static MovimientoDecision buscarDecisionDeAtaque(SimuladorTurnos sim, Entidad actual, Random random) {
        List<MovimientoDecision> opciones = new ArrayList<>();

        for (Entidad posibleObjetivo : sim.getEntidades()) {
            if (posibleObjetivo == actual || !posibleObjetivo.estaVivo()) continue;

            List<AHabilidad> habilidades = sim.getHabilidadesDisponiblesParaObjetivo(posibleObjetivo);
            for (AHabilidad habilidad : habilidades) {
                if (sim.estaEnRango(habilidad, posibleObjetivo)) {
                    opciones.add(new MovimientoDecision(habilidad, posibleObjetivo));
                }
            }
        }

        if (opciones.isEmpty()) return null;
        return opciones.get(random.nextInt(opciones.size()));
    }

    private record MovimientoDecision(AHabilidad habilidad, Entidad objetivo) {}
}
