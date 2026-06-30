package render;

import javax.swing.*;
import java.awt.*;


public class TemasUI {
	// 1. Configura el fondo, layout y bordes del panel principal de la pantalla
	public static void configurarPanelPantalla(JPanel panel) {
	    panel.setLayout(new BorderLayout(10, 10));
	    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    panel.setBackground(new Color(20, 20, 20));
	}

	// 2. Crea y devuelve el JLabel de título con el color amarillo Hogwarts
	public static void pintarTituloPantalla(JLabel titulo) {
	    titulo.setFont(new Font("Arial", Font.BOLD, 24));
	    titulo.setForeground(new Color(255, 230, 50));
	}

	// 3. Crea y devuelve el JTextArea oscuro listo para recibir el texto
	public static void pintarAreaTexto(JTextArea areaTexto) {
	    areaTexto.setEditable(false);
	    areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 18));
	    areaTexto.setBackground(new Color(30, 30, 30));
	    areaTexto.setForeground(Color.LIGHT_GRAY);
	    areaTexto.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
	}

	// 4. Encapsula el botón volver dentro de su panel inferior oscuro
	public static void pintarPanelBotonInferior(JPanel panelBoton) {
	    panelBoton.setBackground(new Color(20, 20, 20));
	}
	
    // Método global para que CUALQUIER pantalla estilice sus botones con hover oscuro
    public static void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(new Color(45, 45, 45));       // Gris oscuro base
        boton.setForeground(Color.WHITE);                 // Texto blanco
        boton.setFocusPainted(false);                     // Quita recuadro azul nativo               
        boton.setOpaque(true);                            
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1), 
            BorderFactory.createEmptyBorder(6, 12, 6, 12)             
        ));

        // El Hover que se aplica de forma automática
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(60, 60, 60)); 
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(45, 45, 45)); 
            }
        });
    }

    // Método global para que CUALQUIER JScrollPane se vuelva oscuro y sin flechas blancas
    public static void aplicarEstiloOscuro(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 40), 1));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setBackground(new Color(25, 25, 25));
        verticalBar.setOpaque(true);

        verticalBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle bounds) {
                g.setColor(new Color(60, 60, 60)); 
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
                g.setColor(new Color(25, 25, 25)); 
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) { return crearBotonOculto(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return crearBotonOculto(); }

            private JButton crearBotonOculto() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }
    
    // Resetea las keys de UIManager que usamos para los diálogos temáticos  
    public static void mostrarAdvertenciaTema(java.awt.Component parent, String mensaje, String titulo) {
        UIManager.put("OptionPane.background",        new Color(20, 20, 20));
        UIManager.put("Panel.background",             new Color(20, 20, 20));
        UIManager.put("OptionPane.messageForeground", Color.LIGHT_GRAY);
        UIManager.put("Button.background",            new Color(45, 45, 45));
        UIManager.put("Button.foreground",            Color.WHITE);
        JOptionPane.showMessageDialog(parent, mensaje, titulo, JOptionPane.PLAIN_MESSAGE);
        TemasUI.resetUIManager();
    }
    
    private static void resetUIManager() {
        UIManager.put("OptionPane.background",        null);
        UIManager.put("Panel.background",             null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.background",            null);
        UIManager.put("Button.foreground",            null);
    }
    

    public static void estilizarBotonConfirmar(JButton b) {
        b.setBackground(new Color(50, 180, 80)); // Tu verde original
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false); // Evita bordes nativos feos según el OS
        b.setFocusPainted(false);
    }

    public static void estilizarBotonPeligro(JButton b) {
        b.setBackground(new Color(180, 50, 50)); // Tu rojo original
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
    }
    
    public static void estilizarBotonSaltar(JButton b) {
        b.setBackground(new Color(190, 150, 40)); // Tu rojo original
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
    }
}