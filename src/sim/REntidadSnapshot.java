package sim;

public record REntidadSnapshot(
    	int posX,
    	int posY,
    	int salud,
    	int mana,
    	int movimiento,
        boolean bloqueaDanio,
        boolean bloqueaMovimiento,
        boolean bloqueaHechizos,
        boolean bloqueaAccion,
        EEstadoEntidad estado
	) {}