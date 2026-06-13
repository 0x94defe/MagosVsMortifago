package render;
import sim.Entidad;

import javax.swing.*;
import java.awt.*;


public class VentanaInfo extends JFrame {
    private static final long serialVersionUID = 1L;

    private final PanelEntidad panelActual;
    private final PanelEntidad panelInspeccionada;
    private Entidad entidadActual;
    private Entidad entidadInspeccionada;

    public VentanaInfo() {
        setTitle("Información");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 0, 4));
        getContentPane().setBackground(new Color(20, 20, 20));

        panelActual        = new PanelEntidad("Personaje Actual", new Color(255, 230, 50));
        panelInspeccionada = new PanelEntidad("Objetivo", new Color(255, 230, 50));

        add(panelActual);
        add(panelInspeccionada);


        new javax.swing.Timer(500, e -> {
            panelActual.setEntidad(entidadActual);
            panelInspeccionada.setEntidad(entidadInspeccionada);
        }).start();
        
        LayoutPantalla.posicionarInfo(this);
    }

    public void setActual(Entidad e) { 
        entidadActual = e; 
    }

    public void setInspeccionada(Entidad e) { 
        entidadInspeccionada = e; 
    }

    public void mostrar() { 
        SwingUtilities.invokeLater(() -> setVisible(true)); 
    }

    private static class PanelEntidad extends JPanel {
        private static final long serialVersionUID = 1L;
        private final JTextArea area;

        PanelEntidad(String titulo, Color colorTitulo) {
            setLayout(new BorderLayout(4, 4));
            setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            setBackground(new Color(20, 20, 20));

            JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
            TemasUI.pintarTituloPantalla(lbl);
            add(lbl, BorderLayout.NORTH);


            area = new JTextArea("—");
            TemasUI.pintarAreaTexto(area);

            JScrollPane scrollPane = new JScrollPane(area);
            TemasUI.aplicarEstiloOscuro(scrollPane);
            add(scrollPane, BorderLayout.CENTER);
        }

        void setEntidad(Entidad e) {
            area.setText(e == null? "—" : e.toString());
        }
    }
}
