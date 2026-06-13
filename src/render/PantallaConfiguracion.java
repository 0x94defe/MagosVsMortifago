package render;
import app.EscenariosPrefab;
import sim.Escenario;

import javax.swing.*;
import java.awt.*;


public class PantallaConfiguracion extends JPanel {
    private static final long serialVersionUID = 1L;
    private VentanaMenu ventana; // Guardamos la referencia

    public PantallaConfiguracion(VentanaMenu ventana) {
        this.ventana = ventana;
        inicializarUI();
    }

    private void inicializarUI() {
        setLayout(new BorderLayout(10, 10));
        TemasUI.configurarPanelPantalla(this);

        JLabel titulo = new JLabel("Seleccionar Escenario", SwingConstants.CENTER);
        TemasUI.pintarTituloPantalla(titulo);
        add(titulo, BorderLayout.NORTH);

        JPanel panelPrefabs = new JPanel(new GridLayout(0, 1, 10, 10));
        panelPrefabs.setBackground(new Color(20, 20, 20));
        panelPrefabs.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Escenario prefabGuerra = EscenariosPrefab.guerraCompleta();
        Escenario prefabDuelo  = EscenariosPrefab.duelo();

        JButton btnGuerra = new JButton(prefabGuerra.getNombre());
        JButton btnDuelo  = new JButton(prefabDuelo.getNombre());
        TemasUI.estilizarBoton(btnGuerra);
        TemasUI.estilizarBoton(btnDuelo);

        // Al hacer clic, llamamos a un único método limpio en VentanaMenu
        btnGuerra.addActionListener(e -> ventana.asignarEscenarioYContinuar(EscenariosPrefab.guerraCompleta()));
        btnDuelo.addActionListener(e -> ventana.asignarEscenarioYContinuar(EscenariosPrefab.duelo()));

        panelPrefabs.add(btnGuerra);
        panelPrefabs.add(btnDuelo);
        add(panelPrefabs, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver al Menú");
        TemasUI.estilizarBoton(btnVolver);
        btnVolver.addActionListener(e -> ventana.cancelarConfiguracion());

        JPanel panelBoton = new JPanel();
        TemasUI.pintarPanelBotonInferior(panelBoton);
        panelBoton.add(btnVolver);
        add(panelBoton, BorderLayout.SOUTH);
    }
}
