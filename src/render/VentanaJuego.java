package render;
import hogwarts.AHabilidad;
import sim.Entidad;
import sim.SimuladorTurnos;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VentanaJuego extends JFrame {
    private static final long serialVersionUID = 1L;

    private final SimuladorTurnos sim;
    private final GrillaRender mapRender;
    private final VentanaInfo ventanaInfo;
    private final JFrame frame;

    private JLabel  lblTurno;
    private JPanel  panelAcciones;
    private JButton btnMover;
    private JButton btnSaltar;
    private JButton btnTerminarTurno;

    private int pendMovX = -1;
    private int pendMovY = -1;
    private boolean juegoTerminado = false;

    public VentanaJuego(SimuladorTurnos sim, String nombreEscenario) {
        this.sim = sim;

        frame = new JFrame("Escenario: " + nombreEscenario);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        mapRender   = new GrillaRender(sim.getGrilla());
        ventanaInfo = new VentanaInfo();

        frame.add(mapRender,          BorderLayout.CENTER);
        frame.add(crearBarraInferior(), BorderLayout.SOUTH);

        mapRender.setOnCeldaVaciaSeleccionada((x, y) -> {
            pendMovX = x;
            pendMovY = y;
            btnMover.setEnabled(mapRender.isMovimientoValido());
            limpiarAcciones();
        });

        mapRender.setOnObjetivoSeleccionado(objetivo -> {
            pendMovX = -1;
            pendMovY = -1;
            btnMover.setEnabled(false);
            mostrarHechizos(objetivo);
        });

        mapRender.setOnInspeccionado(e -> ventanaInfo.setInspeccionada(e));

        LayoutPantalla.posicionarJuego(frame);
        ventanaInfo.mostrar();
        actualizarEntidadActual();
    }

    public void alCerrar(Runnable callback) {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (ventanaInfo != null) ventanaInfo.dispose();
                callback.run();
            }
        });
    }

    public void mostrar()   { SwingUtilities.invokeLater(() -> frame.setVisible(true)); }
    public void refrescar() { mapRender.repaint(); }


    private JPanel crearBarraInferior() {
        JPanel barra = new JPanel(new BorderLayout(8, 4));
        barra.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        barra.setBackground(new Color(30, 30, 30));

        lblTurno = new JLabel("Turno de: —");
        lblTurno.setForeground(new Color(255, 230, 50));
        lblTurno.setFont(new Font("Arial", Font.BOLD, 13));

        panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelAcciones.setBackground(new Color(30, 30, 30));

        JPanel izquierda = new JPanel(new BorderLayout(4, 4));
        izquierda.setBackground(new Color(30, 30, 30));
        izquierda.add(lblTurno,       BorderLayout.NORTH);
        izquierda.add(panelAcciones,  BorderLayout.CENTER);

        btnMover = new JButton("Mover");
        btnMover.setEnabled(false);
        btnMover.setBackground(new Color(50, 180, 80));
        btnMover.setForeground(Color.WHITE);
        btnMover.setOpaque(true);
        btnMover.setFocusPainted(false);
        btnMover.addActionListener(e -> confirmarMovimiento());

        btnSaltar = new JButton("Saltar");
        TemasUI.estilizarBoton(btnSaltar);
        btnSaltar.addActionListener(e -> saltarTurno());

        btnTerminarTurno = new JButton("Terminar Turno");
        btnTerminarTurno.setBackground(new Color(180, 50, 50));
        btnTerminarTurno.setForeground(Color.WHITE);
        btnTerminarTurno.setOpaque(true);
        btnTerminarTurno.setFocusPainted(false);
        btnTerminarTurno.addActionListener(e -> terminarRonda());

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        derecha.setBackground(new Color(30, 30, 30));
        derecha.add(btnMover);
        derecha.add(btnSaltar);
        derecha.add(btnTerminarTurno);

        barra.add(izquierda, BorderLayout.CENTER);
        barra.add(derecha,   BorderLayout.EAST);
        return barra;
    }

    // -------------------------------------------------------------------------
    // Lógica de turno — delega en sim, render solo muestra
    // -------------------------------------------------------------------------

    private void actualizarEntidadActual() {
        Entidad actual = sim.getEntidadActual();
        ventanaInfo.setActual(actual);
        mapRender.setEntidadActual(actual);
        limpiarAcciones();
        btnMover.setEnabled(false);
        pendMovX = -1;
        pendMovY = -1;

        // Si el simulador nos dice que no hay entidad actual, es porque la ronda terminó
        if (actual == null) {
            lblTurno.setText("Ronda terminada — presioná 'Terminar Turno'");
            btnSaltar.setEnabled(false);
            btnMover.setEnabled(false);
            btnTerminarTurno.setEnabled(true); // Habilitamos el botón de pasar de ronda
            return;
        }

        // Si hay un jugador válido, bloqueamos el pase de ronda y habilitamos sus acciones
        lblTurno.setText("Turno de: " + actual.getNombre() + "  [" + actual.getNombreBando() + "]");
        btnSaltar.setEnabled(true);
        btnMover.setEnabled(true);
        btnTerminarTurno.setEnabled(false); // No puede terminar ronda a mitad de los turnos
    }

    /**
     * Pide a sim los hechizos válidos para el objetivo y los muestra.
     * No tiene lógica de filtrado — eso ya lo hizo sim.
     */
    private void mostrarHechizos(Entidad objetivo) {
        limpiarAcciones();

        List<AHabilidad> habilidades = sim.getHabilidadesDisponiblesParaObjetivo(objetivo);

        if (habilidades.isEmpty()) {
            JLabel lbl = new JLabel("Sin Habilidades para ese objetivo");
            lbl.setForeground(new Color(180, 180, 180));
            lbl.setFont(new Font("Arial", Font.ITALIC, 13));
            panelAcciones.add(lbl);
        } else {
            for (AHabilidad h : habilidades) {
                boolean enAlcance = sim.estaEnRango(h, objetivo);
                String label = h.getNombre() + " (" + h.getCosteRecurso() + "MP)"
                             + (enAlcance ? "" : "  ✗");

                JButton btn = new JButton(label);
                TemasUI.estilizarBoton(btn);
                btn.setEnabled(enAlcance);

                btn.addActionListener(e -> {
                    boolean ok = sim.lanzarHabilidad(h, objetivo);
                    if (!ok) {
                        TemasUI.mostrarAdvertenciaTema(frame, "No hay suficiente maná.", "Sin maná");
                        return;
                    }
                    mapRender.limpiarSeleccion();
                    verificarVictoria();
                    if (!juegoTerminado) siguienteEntidad();
                });
                panelAcciones.add(btn);
            }
        }

        panelAcciones.revalidate();
        panelAcciones.repaint();
    }

    private void verificarVictoria() {
        if (juegoTerminado || !sim.esJuegoTerminado()) return;
        juegoTerminado = true;
        TemasUI.mostrarAdvertenciaTema(frame, sim.getEstadoJuego(), "Fin del juego");
        bloquearJuego();
    }

    private void bloquearJuego() {
        btnMover.setEnabled(false);
        btnSaltar.setEnabled(false);
        btnTerminarTurno.setEnabled(false);
        mapRender.setEntidadActual(null);
        limpiarAcciones();
        lblTurno.setText("Juego terminado.");
    }

    private void confirmarMovimiento() {
        if (pendMovX < 0 || pendMovY < 0) return;
        boolean ok = sim.moverActual(pendMovX, pendMovY);
        if (!ok) {
            TemasUI.mostrarAdvertenciaTema(frame, "No se puede mover ahí.", "Movimiento inválido");
            return;
        }
        mapRender.limpiarSeleccion();
        siguienteEntidad();
    }

    private void limpiarAcciones() {
        panelAcciones.removeAll();
        panelAcciones.revalidate();
        panelAcciones.repaint();
    }

    private void siguienteEntidad() { 
        sim.procesarTurno(); 
        actualizarEntidadActual();
        verificarVictoria();
    }
    
    private void saltarTurno() {
        sim.saltarTurno();
        actualizarEntidadActual();
        verificarVictoria();
    }

    private void terminarRonda() { 
        sim.terminarRonda();
        actualizarEntidadActual(); 
    }
}
