package render;
import app.FEscenariosPrefab;
import sim.IEscenarioConsulta;

import java.util.List;
import javax.swing.*;
import java.awt.*;


public class PantallaConfiguracion extends JPanel {
    private static final long serialVersionUID = 1L;

    public PantallaConfiguracion(VentanaMenu ventana) {
        setLayout(new BorderLayout(10, 10));
        TemasUI.configurarPanelPantalla(this);

        JLabel titulo = new JLabel("Seleccionar Escenario", SwingConstants.CENTER);
        TemasUI.pintarTituloPantalla(titulo);
        add(titulo, BorderLayout.NORTH);

        JPanel panelPrefabs = new JPanel(new GridLayout(0, 1, 10, 10));
        panelPrefabs.setBackground(new Color(20, 20, 20));
        panelPrefabs.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        
        List<IEscenarioConsulta> escenarios = FEscenariosPrefab.obtenerEscenariosDisponibles();
        for (IEscenarioConsulta prefab : escenarios) {
            JButton btnEscenario = new JButton(prefab.getNombre());
            TemasUI.estilizarBoton(btnEscenario);
            
            btnEscenario.addActionListener(e -> ventana.asignarEscenarioYContinuar(prefab));
            
            panelPrefabs.add(btnEscenario);
        }

        add(panelPrefabs, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver al Menú");
        TemasUI.estilizarBoton(btnVolver);
        btnVolver.addActionListener(e -> ventana.cambiarPantalla(EPantalla.MENU));

        JPanel panelBoton = new JPanel();
        TemasUI.pintarPanelBotonInferior(panelBoton);
        panelBoton.add(btnVolver);
        add(panelBoton, BorderLayout.SOUTH);
    }
}
