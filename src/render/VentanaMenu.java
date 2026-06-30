package render;
import app.FLibroDeHechizos;
import sim.IEscenarioConsulta;
import sim.Turnero;

import javax.swing.*;
import java.awt.*;


public class VentanaMenu extends JFrame implements InterfazUsuario {
    private static final long serialVersionUID = 1L;
    private boolean primeraConfiguracion = true;
    private CardLayout cardLayout;
    private JPanel contenedorPrincipal;
    private IEscenarioConsulta escenario;
    private JButton btnJugar; // referencia para habilitar/deshabilitar


    public VentanaMenu() {
        setTitle("Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        StringBuilder sbHechizos = new StringBuilder();
        for (FLibroDeHechizos h : FLibroDeHechizos.values()) {
            sbHechizos.append("===================================\n");
            sbHechizos.append(h.getDescripcion()).append("\n");
        }
        
        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);
        contenedorPrincipal.setBackground(new Color(20, 20, 20));

        contenedorPrincipal.add(crearPantallaMenu(),
        						EPantalla.MENU.name());
        
        contenedorPrincipal.add(new JPanel(),
        						EPantalla.CLASES.name());
        
        contenedorPrincipal.add(new PantallaContenido(this, "Libro de Hechizos", sbHechizos.toString()),
        						EPantalla.HECHIZOS.name());
        
        contenedorPrincipal.add(new PantallaConfiguracion(this),
        						EPantalla.CONFIGURACION.name());

        add(contenedorPrincipal);
    }

    @Override
    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(contenedorPrincipal, EPantalla.MENU.name());
            setVisible(true);
        });
    }

    public void cambiarPantalla(EPantalla pantalla) {
        if (pantalla == EPantalla.CLASES && escenario == null) {
            TemasUI.mostrarAdvertenciaTema(this, "No hay escenario cargado. Configurá uno primero.", "Sin escenario");
            cambiarPantalla(EPantalla.CONFIGURACION);
            return;
        }
        cardLayout.show(contenedorPrincipal, pantalla.name());
    }

    private void lanzarJuego() {	
        LayoutPantalla.posicionarMenu(this);
        cambiarPantalla(EPantalla.HECHIZOS);
        btnJugar.setEnabled(false);

        Turnero sim = escenario.crearSimulacion();
        VentanaJuego juego = new VentanaJuego(sim);
        
        // Al cerrar VentanaJuego → cerrar VentanaInfo y rehabilitar btnJugar
        juego.alCerrar(() -> { btnJugar.setEnabled(true); });
        juego.mostrar();
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

        btnJugar.addActionListener(e -> {
            if (escenario == null) {
                cambiarPantalla(EPantalla.CONFIGURACION);
                return;
            }
            lanzarJuego();
        });
        btnClases.addActionListener(e     -> cambiarPantalla(EPantalla.CLASES));
        btnHechizos.addActionListener(e   -> cambiarPantalla(EPantalla.HECHIZOS));
        btnConfigurar.addActionListener(e -> cambiarPantalla(EPantalla.CONFIGURACION));
        btnSalir.addActionListener(e      -> System.exit(0));
        
        JButton[] botones = {btnJugar, btnClases, btnHechizos, btnConfigurar, btnSalir};
        for (JButton b : botones) {
        	TemasUI.estilizarBoton(b);
        	panel.add(b);
        }

        return panel;
    }
    
    public void asignarEscenarioYContinuar(IEscenarioConsulta nuevoEscenario) {
        this.escenario = nuevoEscenario;
        contenedorPrincipal.remove(EPantalla.CLASES.ordinal());
        
        contenedorPrincipal.add(
        		new PantallaContenido(this, "Lista de Personajes Disponibles", nuevoEscenario.getDescripcionDeTodosLosPersonajes()),
        		EPantalla.CLASES.name(),
        		EPantalla.CLASES.ordinal()
		);
        
        if (primeraConfiguracion) {
        	primeraConfiguracion = false;
            lanzarJuego();
        } else {
            cambiarPantalla(EPantalla.MENU);
        }
    }
}
