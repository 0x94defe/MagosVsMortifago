package render;
import hogwarts.FLibroDeHechizos;
import hogwarts.APersonaje;
import sim.Escenario;
import sim.SimuladorTurnos;

import javax.swing.*;
import java.awt.*;


public class VentanaMenu extends JFrame implements InterfazUsuario {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel contenedorPrincipal;
    private Escenario escenario;
    private boolean modoLanzarJuego = false; // true cuando venimos de btnJugar sin escenario
    private JButton btnJugar; // referencia para habilitar/deshabilitar

    public VentanaMenu() {
        setTitle("Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);
        contenedorPrincipal.setBackground(new Color(20, 20, 20));

        contenedorPrincipal.add(crearPantallaMenu(),               "MENU");
        contenedorPrincipal.add(new JPanel(),                      "CLASES");
        StringBuilder sbHechizos = new StringBuilder();
        for (FLibroDeHechizos h : FLibroDeHechizos.values()) {
            sbHechizos.append("===================================\n");
            sbHechizos.append(h.getDescripcion()).append("\n");
        }
        contenedorPrincipal.add(new PantallaContenido(this, "Libro de Hechizos", sbHechizos.toString()), "HECHIZOS");
        contenedorPrincipal.add(new PantallaConfiguracion(this),   "CONFIGURACION");

        add(contenedorPrincipal);
    }

    @Override
    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(contenedorPrincipal, "MENU");
            setVisible(true);
        });
    }

    public void cambiarPantalla(String nombrePantalla) {
        if (nombrePantalla.equals("CLASES") && escenario == null) {
            TemasUI.mostrarAdvertenciaTema(this, "No hay escenario cargado. Configurá uno primero.", "Sin escenario");
            cambiarPantalla("CONFIGURACION");
            return;
        }
        cardLayout.show(contenedorPrincipal, nombrePantalla);
    }

    private void lanzarJuego() {
        LayoutPantalla.posicionarMenu(this);
        cambiarPantalla("HECHIZOS");
        btnJugar.setEnabled(false);

        SimuladorTurnos sim = new SimuladorTurnos(escenario, 10, 10);
        VentanaJuego ventana = new VentanaJuego(sim, escenario.getNombre());

        // Al cerrar VentanaJuego → cerrar VentanaInfo y rehabilitar btnJugar
        ventana.alCerrar(() -> {
            btnJugar.setEnabled(true);
            modoLanzarJuego = false;
        });

        ventana.mostrar();
    }

    private JPanel crearPantallaMenu() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        panel.setBackground(new Color(20, 20, 20));

        JLabel titulo = new JLabel("MORTIFAGOS vs MAGOS", SwingConstants.CENTER);
        TemasUI.pintarTituloPantalla(titulo);
        panel.add(titulo);

        btnJugar              = new JButton("Ejecutar Juego");
        JButton btnClases     = new JButton("Mostrar Personajes");
        JButton btnHechizos   = new JButton("Mostrar Hechizos");
        JButton btnConfigurar = new JButton("Configurar Escenario");
        JButton btnSalir      = new JButton("Salir");

        for (JButton b : new JButton[]{btnJugar, btnClases, btnHechizos, btnConfigurar, btnSalir})
            TemasUI.estilizarBoton(b);

        btnJugar.addActionListener(e -> {
            if (escenario == null) {
                modoLanzarJuego = true;
                cambiarPantalla("CONFIGURACION");
                return;
            }
            lanzarJuego();
        });

        btnClases.addActionListener(e     -> cambiarPantalla("CLASES"));
        btnHechizos.addActionListener(e   -> cambiarPantalla("HECHIZOS"));
        btnConfigurar.addActionListener(e -> { modoLanzarJuego = false; cambiarPantalla("CONFIGURACION"); });
        btnSalir.addActionListener(e      -> System.exit(0));

        panel.add(btnJugar);
        panel.add(btnClases);
        panel.add(btnHechizos);
        panel.add(btnConfigurar);
        panel.add(btnSalir);

        return panel;
    }
    
    public void asignarEscenarioYContinuar(Escenario nuevoEscenario) {
        this.escenario = nuevoEscenario;
        contenedorPrincipal.remove(1);
        
        StringBuilder sbPersonajes = new StringBuilder();
        for (APersonaje p : nuevoEscenario.getTodosLosPersonajes()) {
            sbPersonajes.append("===================================\n");
            sbPersonajes.append(p.toString()).append("\n");
        }
        contenedorPrincipal.add(new PantallaContenido(this, "Lista de Personajes Disponibles", sbPersonajes.toString()), "CLASES", 1);
        
        if (modoLanzarJuego) {
            lanzarJuego();
        } else {
            cambiarPantalla("MENU");
        }
    }

    public void cancelarConfiguracion() {
        this.modoLanzarJuego = false;
        cambiarPantalla("MENU");
    }
}