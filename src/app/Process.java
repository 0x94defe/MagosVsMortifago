package app;

import render.InterfazUsuario;
import render.VentanaMenu;

public class Process {
    public static void main(String[] args) {
        InterfazUsuario ui = new VentanaMenu();
        ui.iniciar();
    }
}
