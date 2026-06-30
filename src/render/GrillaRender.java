package render;
import sim.IObservable;
import sim.IGrillaConsulta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GrillaRender extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private int tamCelda = 60; 
    private int diamEntidad = 50;
    private static final int MARGEN_ENTIDAD = 4;

    private final IGrillaConsulta grilla;
    private IObservable entidadActual;
    private IObservable objetivo;
    private int celdaMovX = -1;
    private int celdaMovY = -1;
    private boolean movimientoValido = true;

    private Consumer<IObservable>        onObjetivoSeleccionado;
    private BiConsumer<Integer, Integer> onCeldaVaciaSeleccionada;
    private Consumer<IObservable>        onInspeccionado;
    

    public GrillaRender(IGrillaConsulta grilla) {
        this.grilla = grilla;
        setBackground(Color.BLACK);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cx = e.getX() / tamCelda;
                int cy = e.getY() / tamCelda;
                if (grilla.estaFueraDeGrilla(cx, cy)) return;

                IObservable clickeada = grilla.getEntidad(cx, cy);

                if (clickeada == null) {
                    celdaMovX = cx;
                    celdaMovY = cy;
                    objetivo = null;
                    movimientoValido = estaEnRango(cx, cy);
                    if (onCeldaVaciaSeleccionada != null) onCeldaVaciaSeleccionada.accept(cx, cy);
                } else {
                    limpiarSeleccionLocal();
                    objetivo = clickeada;
                    if (onObjetivoSeleccionado != null) onObjetivoSeleccionado.accept(clickeada);
                    if (onInspeccionado != null)        onInspeccionado.accept(clickeada);
                }
                repaint();
            }
        });
    }

    private boolean estaEnRango(int cx, int cy) {
        if (entidadActual == null) return false;
        int distance = grilla.calcularDistancia(entidadActual.getPosX(), entidadActual.getPosY(), cx, cy);
        return distance <= entidadActual.getMovimiento();
    }

    private void limpiarSeleccionLocal() {
        this.objetivo = null;
        this.celdaMovX = -1;
        this.celdaMovY = -1;
        this.movimientoValido = true;
    }

    private void dibujarFondo(Graphics g, IObservable e, int cellX, int cellY) {
        if (e != null && e == entidadActual) {
            g.setColor(Color.WHITE);
            g.fillRect(cellX, cellY, tamCelda, tamCelda);
        } else if (e != null && e == objetivo) {
            g.setColor(Color.YELLOW);
            g.fillRect(cellX, cellY, tamCelda, tamCelda);
        } else if ((cellX / tamCelda) == celdaMovX && (cellY / tamCelda) == celdaMovY) {
            g.setColor(movimientoValido ? new Color(50, 200, 80) : new Color(200, 50, 50));
            g.fillRect(cellX, cellY, tamCelda, tamCelda);
        }
        
        boolean esActual   = (e != null && e == entidadActual);
        boolean esObjetivo = (e != null && e == objetivo);
        if (esActual && esObjetivo) {
            g.setColor(Color.YELLOW);
            Graphics2D g2 = (Graphics2D) g;
            Stroke strokeOriginal = g2.getStroke();
            g2.setStroke(new BasicStroke(4));
            g2.drawRect(cellX + 2, cellY + 2, tamCelda - 4, tamCelda - 4);
            g2.setStroke(strokeOriginal);
        }
    }

    private void dibujarGrilla(Graphics g) {
        g.setColor(new Color(50, 50, 50));
        for (int x = 0; x <= grilla.getAncho(); x++)
            g.drawLine(x * tamCelda, 0, x * tamCelda, grilla.getAlto() * tamCelda);
        for (int y = 0; y <= grilla.getAlto(); y++)
            g.drawLine(0, y * tamCelda, grilla.getAncho() * tamCelda, y * tamCelda);
    }

    private void dibujarEntidad(Graphics g, IObservable e, int cellX, int cellY) {
        int px = cellX + MARGEN_ENTIDAD;
        int py = cellY + MARGEN_ENTIDAD;

        Color color = PersonajeRender.getColor(e);
        g.setColor(color);
        g.fillOval(px, py, diamEntidad, diamEntidad);
        g.setColor(color.darker());
        g.drawOval(px, py, diamEntidad, diamEntidad);
        g.setColor(Color.BLACK);
        
        g.drawString(
            String.valueOf(PersonajeRender.getSimbolo(e)),
            px + diamEntidad / 2 - 4,
            py + diamEntidad / 2 + 5
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int tamCeldaX = getWidth() / grilla.getAncho();
        int tamCeldaY = getHeight() / grilla.getAlto();
        
        tamCelda = Math.max(10, Math.min(tamCeldaX, tamCeldaY)); 
        diamEntidad = tamCelda - MARGEN_ENTIDAD * 2;
        
        for (int x = 0; x < grilla.getAncho(); x++) {
            for (int y = 0; y < grilla.getAlto(); y++) {
                int px = x * tamCelda;
                int py = y * tamCelda;
                IObservable e = grilla.getEntidad(x, y);

                dibujarFondo(g, e, px, py);
                if (e != null) dibujarEntidad(g, e, px, py);
            }
        }
        dibujarGrilla(g);
    }

    public void setEntidadActual(IObservable e) {
        entidadActual = e;
        limpiarSeleccionLocal();
        repaint();
    }
    public void limpiarSeleccion() {
        limpiarSeleccionLocal();
        repaint();
    }

    public boolean isMovimientoValido() { return movimientoValido; }
    public void setOnObjetivoSeleccionado(Consumer<IObservable> cb)          { this.onObjetivoSeleccionado = cb; }
    public void setOnCeldaVaciaSeleccionada(BiConsumer<Integer, Integer> cb) { this.onCeldaVaciaSeleccionada = cb; }
    public void setOnInspeccionado(Consumer<IObservable> cb)                 { this.onInspeccionado = cb; }
}