package render;
import sim.ICasteable;
import sim.IObservable;
import sim.Turnero;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VentanaJuego extends JFrame {
	private static final long serialVersionUID = 1L;

    private final Turnero sim;
    private final GrillaRender mapRender;
    private final VentanaInfo ventanaInfo;

    private JLabel  lblTurno;
    private JPanel  panelAcciones;
    private JButton btnMover;
    private JButton btnSaltar;
    private JButton btnTerminarTurno;

    private int pendMovX = -1;
    private int pendMovY = -1;
    private boolean juegoTerminado = false;
    private IObservable objetivoActual = null;
    private int paginaHechizosActual = 0;

    
    public VentanaJuego(Turnero sim) {
        this.sim = sim;

        setTitle("Escenario: " + sim.getNombreMapa());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);

        mapRender   = new GrillaRender(sim.getGrilla());
        ventanaInfo = new VentanaInfo();

        add(mapRender,          BorderLayout.CENTER);
        add(crearBarraInferior(), BorderLayout.SOUTH);

        mapRender.setOnCeldaVaciaSeleccionada((x, y) -> {
            pendMovX = x;
            pendMovY = y;
            objetivoActual = null; // Limpiamos
            btnMover.setEnabled(mapRender.isMovimientoValido());
            limpiarAcciones();
        });

        mapRender.setOnObjetivoSeleccionado(objetivo -> {
            pendMovX = -1;
            pendMovY = -1;
            btnMover.setEnabled(false);

            this.objetivoActual = objetivo; 
            this.paginaHechizosActual = 0;
            
            mostrarHechizos();
        });
        mapRender.setOnInspeccionado(e -> {
        	ventanaInfo.setInspeccionada(e);
        });

        LayoutPantalla.posicionarJuego(this);
        ventanaInfo.mostrar();
        actualizarEntidadActual();
    }

    public void alCerrar(Runnable callback) {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
            	if (!juegoTerminado) System.out.println("\n################### Juego Abortado ###################");
                if (ventanaInfo != null) ventanaInfo.dispose();
                callback.run();
            }
        });
    }
    public void mostrar()   { SwingUtilities.invokeLater(() -> setVisible(true)); }


    private JPanel crearBarraInferior() {
        // La barra contenedora principal usa BoxLayout vertical para apilar las 2 filas
        JPanel barra = new JPanel();
        barra.setLayout(new BoxLayout(barra, BoxLayout.Y_AXIS));
        barra.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        barra.setBackground(new Color(30, 30, 30));
        
        // Le damos una altura fija y viable (72px aprox es ideal para dos filas limpias)
        barra.setPreferredSize(new Dimension(LayoutPantalla.getAncho(), 80));

        // ==================== FILA 1: INFO + COMANDOS ====================
        lblTurno = new JLabel("Turno de: —");
        lblTurno.setForeground(new Color(255, 230, 50));
        lblTurno.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel izquierdaFila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izquierdaFila1.setBackground(new Color(30, 30, 30));
        izquierdaFila1.add(lblTurno);

        // Inicializar todos los botones (Acciones y Reportes)
        btnMover = new JButton("Mover");
        btnMover.setEnabled(false);
        TemasUI.estilizarBotonConfirmar(btnMover);
        btnMover.addActionListener(e -> confirmarMovimiento());

        btnSaltar = new JButton("Saltar");
        TemasUI.estilizarBotonSaltar(btnSaltar);
        btnSaltar.addActionListener(e -> saltarTurno());

        btnTerminarTurno = new JButton("Terminar Turno");
        TemasUI.estilizarBotonPeligro(btnTerminarTurno);
        btnTerminarTurno.addActionListener(e -> terminarRonda());

        JButton btnHistorial = new JButton("Historial");
        TemasUI.estilizarBoton(btnHistorial);
        btnHistorial.addActionListener(e ->
            TemasUI.mostrarAdvertenciaTema(this, sim.getReporteSecuenciaDeAcciones(), "Secuencia de Acciones")
        );

        JButton btnHechizosPersonaje = new JButton("Hechizos x Personaje");
        TemasUI.estilizarBoton(btnHechizosPersonaje);
        btnHechizosPersonaje.addActionListener(e ->
            TemasUI.mostrarAdvertenciaTema(this, sim.getReporteHechizosPorPersonaje(), "Hechizos Lanzados por Personaje")
        );

        JButton btnHechizosBando = new JButton("Hechizos x Bando");
        TemasUI.estilizarBoton(btnHechizosBando);
        btnHechizosBando.addActionListener(e ->
            TemasUI.mostrarAdvertenciaTema(this, sim.getReporteHechizosPorBando(), "Hechizos Lanzados por Bando")
        );

        // Metemos TODOS los comandos juntos a la derecha
        JPanel derechaFila1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        derechaFila1.setBackground(new Color(30, 30, 30));
        derechaFila1.add(btnMover);
        derechaFila1.add(btnSaltar);
        derechaFila1.add(btnTerminarTurno);
        derechaFila1.add(btnHistorial);
        derechaFila1.add(btnHechizosPersonaje);
        derechaFila1.add(btnHechizosBando);

        // Ensamble de la Fila 1 (Estructura izquierda/derecha)
        JPanel fila1 = new JPanel(new BorderLayout());
        fila1.setBackground(new Color(30, 30, 30));
        fila1.add(izquierdaFila1, BorderLayout.CENTER);
        fila1.add(derechaFila1, BorderLayout.EAST);


        // ==================== FILA 2: HECHIZOS (SPAN COMPLETO) ====================
        // Un FlowLayout simple que ocupa todo el ancho de la pantalla abajo
        panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelAcciones.setBackground(new Color(30, 30, 30));
        
        JPanel fila2 = new JPanel(new BorderLayout());
        fila2.setBackground(new Color(30, 30, 30));
        fila2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Un pelín de espacio con la fila de arriba
        fila2.add(panelAcciones, BorderLayout.CENTER);


        // ==================== AGREGAR FILAS AL CONTENEDOR ====================
        barra.add(fila1);
        barra.add(fila2);

        return barra;
    }

    // -------------------------------------------------------------------------
    // Lógica de turno — delega en sim, render solo muestra
    // -------------------------------------------------------------------------
    private void actualizarEntidadActual() {
    	IObservable actual = sim.getEntidadActual();
        ventanaInfo.setActual(actual);
        mapRender.setEntidadActual(actual);
        limpiarAcciones();
        btnMover.setEnabled(false);
        pendMovX = -1;
        pendMovY = -1;

        // Si el simulador nos dice que no hay entidad actual, es porque la ronda terminó
        if (actual == null) {
            String textoFinRonda = "<html>"
                    + "Ronda terminada<br>"
                    + "presioná 'Terminar Turno'"
                    + "</html>";
            
            lblTurno.setText(textoFinRonda);
            btnSaltar.setEnabled(false);
            btnMover.setEnabled(false);
            btnTerminarTurno.setEnabled(true); // Habilitamos el botón de pasar de ronda
            return;
        }

        // Si hay un jugador válido, bloqueamos el pase de ronda y habilitamos sus acciones
        String textoTurno = "<html>"
                + "<b>Batallon:</b> " + actual.getNombreBando().toUpperCase() + "<br>"
                + "<b>Turno de:</b> " + actual.getNombre()
                + "</html>";
        
        lblTurno.setText(textoTurno);
        btnSaltar.setEnabled(true);
        btnMover.setEnabled(false);
        btnTerminarTurno.setEnabled(false); // No puede terminar ronda a mitad de los turnos
    }

    
    /**
     * Pide a sim los hechizos válidos para el objetivo y los muestra.
     * No tiene lógica de filtrado — eso ya lo hizo sim.
     */
    private void mostrarHechizos() {
        panelAcciones.removeAll(); 

        if (objetivoActual == null) return;

        List<ICasteable> habilidades = sim.getHabilidadesDisponiblesParaObjetivo(objetivoActual);

        if (habilidades.isEmpty()) {
            JLabel lbl = new JLabel("Sin Habilidades para ese objetivo");
            lbl.setForeground(new Color(180, 180, 180));
            lbl.setFont(new Font("Arial", Font.ITALIC, 13));
            panelAcciones.add(lbl);
        } else {
            final int TAMANIO_PAGINA = 6; 
            int totalHechizos = habilidades.size();
            
            int inicio = paginaHechizosActual * TAMANIO_PAGINA;
            int fin = Math.min(inicio + TAMANIO_PAGINA, totalHechizos);

            if (paginaHechizosActual > 0) {
                JButton btnAnt = new JButton("<");
                TemasUI.estilizarBoton(btnAnt);
                btnAnt.addActionListener(e -> {
                    paginaHechizosActual--;
                    mostrarHechizos();
                });
                panelAcciones.add(btnAnt);
            }

            for (int i = inicio; i < fin; i++) {
                ICasteable h = habilidades.get(i);
                boolean enAlcance = sim.estaEnRango(h, objetivoActual);
                String label = h.getNombre() + " (" + h.getCosteRecurso() + "MP)";
                JButton btn = new JButton(label);
                TemasUI.estilizarBoton(btn);
                btn.setEnabled(enAlcance);

                btn.addActionListener(e -> {
                    boolean ok = sim.lanzarHabilidad(h, objetivoActual);
                    if (!ok) {
                        TemasUI.mostrarAdvertenciaTema(this, "No hay suficiente maná.", "Sin maná");
                        return;
                    }
                    mapRender.limpiarSeleccion();
                    verificarVictoria();
                    if (!juegoTerminado) siguienteEntidad();
                });
                panelAcciones.add(btn);
            }

            if (fin < totalHechizos) {
                JButton btnSig = new JButton(">");
                TemasUI.estilizarBoton(btnSig);
                btnSig.addActionListener(e -> {
                    paginaHechizosActual++;
                    mostrarHechizos();
                });
                panelAcciones.add(btnSig);
            }
        }

        panelAcciones.revalidate();
        panelAcciones.repaint();
    }
    
    private void verificarVictoria() {
        if (juegoTerminado || !sim.esJuegoTerminado()) return;
        juegoTerminado = true;
        TemasUI.mostrarAdvertenciaTema(this, sim.getEstadoJuego(), "Fin del juego");
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
            TemasUI.mostrarAdvertenciaTema(this, "No se puede mover ahí.", "Movimiento inválido");
            return;
        }
        mapRender.limpiarSeleccion();
        siguienteEntidad();
    }
    private void limpiarAcciones() {
        paginaHechizosActual = 0;
        panelAcciones.removeAll();
        panelAcciones.revalidate();
        panelAcciones.repaint();
    }
    private void siguienteEntidad() { 
        sim.procesarTurno(); 
        actualizarEntidadActual();
    }
    private void saltarTurno() {
        sim.saltarTurno();
        actualizarEntidadActual();
        verificarVictoria();
    }
    private void terminarRonda() {
        if (juegoTerminado) return;
        sim.terminarRonda();
        verificarVictoria();
        if (!juegoTerminado) actualizarEntidadActual();
    }
}
