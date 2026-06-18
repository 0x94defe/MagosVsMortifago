package render;
import sim.Entidad;
import sim.Grilla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class GrillaRender extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int TAM_CELDA = 94;

    private final Grilla grilla;

    private Entidad entidadActual;
    private Entidad objetivo;
    private int celdaMovX = -1;
    private int celdaMovY = -1;
    private boolean movimientoValido = true; // verde=true, rojo=false

    private Consumer<Entidad>            onObjetivoSeleccionado;
    private BiConsumer<Integer, Integer> onCeldaVaciaSeleccionada;
    private Consumer<Entidad>            onInspeccionado;

    public GrillaRender(Grilla grilla) {
        this.grilla = grilla;
        setPreferredSize(new Dimension(grilla.getAncho() * TAM_CELDA, grilla.getAlto() * TAM_CELDA));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cx = e.getX() / TAM_CELDA;
                int cy = e.getY() / TAM_CELDA;
                if (grilla.estaFueraDeGrilla(cx, cy)) return;

                Entidad clickeada = grilla.getEntidad(cx, cy);

                if (clickeada == null) {
                    celdaMovX = cx;
                    celdaMovY = cy;
                    objetivo = null;
                    movimientoValido = estaEnRango(cx, cy);
                    if (onCeldaVaciaSeleccionada != null) onCeldaVaciaSeleccionada.accept(cx, cy);
                } else if (clickeada == entidadActual) {
                    celdaMovX = -1; celdaMovY = -1;
                    objetivo = null;
                    movimientoValido = true;
                } else {
                    objetivo = clickeada;
                    celdaMovX = -1; celdaMovY = -1;
                    movimientoValido = true;
                    if (onObjetivoSeleccionado != null) onObjetivoSeleccionado.accept(clickeada);
                    if (onInspeccionado != null)        onInspeccionado.accept(clickeada);
                }
                repaint();
            }
        });
    }

    private boolean estaEnRango(int cx, int cy) {
        if (entidadActual == null) return false;
        int distancia = Math.abs(cx - entidadActual.getPosX())
                      + Math.abs(cy - entidadActual.getPosY());
        return distancia <= entidadActual.getMovimiento();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarFondos(g);
        dibujarGrilla(g);
        dibujarEntidades(g);
    }

    private void dibujarFondos(Graphics g) {
        for (int x = 0; x < grilla.getAncho(); x++) {
            for (int y = 0; y < grilla.getAlto(); y++) {
                Entidad e = grilla.getEntidad(x, y);
                int px = x * TAM_CELDA;
                int py = y * TAM_CELDA;

                if (e != null && e == entidadActual) {
                    g.setColor(Color.WHITE);
                    g.fillRect(px, py, TAM_CELDA, TAM_CELDA);
                } else if (e != null && e == objetivo) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(px, py, TAM_CELDA, TAM_CELDA);
                } else if (x == celdaMovX && y == celdaMovY) {
                    g.setColor(movimientoValido
                        ? new Color(50, 200, 80)   // verde — en rango
                        : new Color(200, 50, 50));  // rojo  — fuera de rango
                    g.fillRect(px, py, TAM_CELDA, TAM_CELDA);
                }
            }
        }
    }

    private void dibujarGrilla(Graphics g) {
        g.setColor(new Color(50, 50, 50));
        for (int x = 0; x <= grilla.getAncho(); x++)
            g.drawLine(x * TAM_CELDA, 0, x * TAM_CELDA, grilla.getAlto() * TAM_CELDA);
        for (int y = 0; y <= grilla.getAlto(); y++)
            g.drawLine(0, y * TAM_CELDA, grilla.getAncho() * TAM_CELDA, y * TAM_CELDA);
    }

    private void dibujarEntidades(Graphics g) {
        int margen = 4;
        int diam = TAM_CELDA - margen * 2;

        for (int x = 0; x < grilla.getAncho(); x++) {
            for (int y = 0; y < grilla.getAlto(); y++) {
                Entidad e = grilla.getEntidad(x, y);
                if (e == null) continue;

                int px = x * TAM_CELDA + margen;
                int py = y * TAM_CELDA + margen;

                Color color = PersonajeRender.getColor(e);
                g.setColor(color);
                g.fillOval(px, py, diam, diam);
                g.setColor(color.darker());
                g.drawOval(px, py, diam, diam);
                g.setColor(Color.BLACK);
                g.drawString(
                    String.valueOf(PersonajeRender.getSimbolo(e)),
                    px + diam / 2 - 4,
                    py + diam / 2 + 5
                );
            }
        }
    }

    public void setEntidadActual(Entidad e) {
        this.entidadActual = e;
        this.objetivo = null;
        this.celdaMovX = -1;
        this.celdaMovY = -1;
        this.movimientoValido = true;
        repaint();
    }

    public void limpiarSeleccion() {
        objetivo = null;
        celdaMovX = -1;
        celdaMovY = -1;
        movimientoValido = true;
        repaint();
    }

    public boolean isMovimientoValido()                                    { return movimientoValido; }
    public int getCeldaMovX()                                              { return celdaMovX; }
    public int getCeldaMovY()                                              { return celdaMovY; }
    public Entidad getObjetivo()                                           { return objetivo; }

    public void setOnObjetivoSeleccionado(Consumer<Entidad> cb)            { this.onObjetivoSeleccionado = cb; }
    public void setOnCeldaVaciaSeleccionada(BiConsumer<Integer, Integer> cb){ this.onCeldaVaciaSeleccionada = cb; }
    public void setOnInspeccionado(Consumer<Entidad> cb)                   { this.onInspeccionado = cb; }
}