package render;

import javax.swing.*;
import java.awt.*;

public class PantallaContenido extends JPanel {
    private static final long serialVersionUID = 1L;


    public PantallaContenido(VentanaMenu ventana, String textoTitulo, String contenidoTexto) {
        TemasUI.configurarPanelPantalla(this);


        JLabel titulo = new JLabel(textoTitulo, SwingConstants.CENTER);
        TemasUI.pintarTituloPantalla(titulo);
        add(titulo, BorderLayout.NORTH);


        JTextArea areaTexto = new JTextArea();
        TemasUI.pintarAreaTexto(areaTexto);   
        areaTexto.setText(contenidoTexto);
        areaTexto.setCaretPosition(0); 

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        TemasUI.aplicarEstiloOscuro(scrollPane);
        add(scrollPane, BorderLayout.CENTER);


        JButton btnVolver = new JButton("Volver al Menú");
        TemasUI.estilizarBoton(btnVolver);
        btnVolver.addActionListener(e -> ventana.cambiarPantalla("MENU"));

        JPanel panelBoton = new JPanel();
        TemasUI.pintarPanelBotonInferior(panelBoton);
        panelBoton.add(btnVolver);
        add(panelBoton, BorderLayout.SOUTH);
    }
}