package app;

import render.InterfazUsuario;
import render.VentanaMenu;

public class Process {

    public static void main(String[] args) throws InterruptedException {
        InterfazUsuario ui = new VentanaMenu();
        ui.iniciar();
    }
}
