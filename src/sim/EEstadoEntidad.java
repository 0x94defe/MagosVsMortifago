package sim;

public enum EEstadoEntidad {
    JUGANDO, 
    ATURDIDO, 
    SILENCIADO, 
    INMOVILIZADO,
    INVULNERABLE,
    MUERTO,
    INERTE;

    public static EEstadoEntidad evaluarEstado(IAlterable obj) {
        if (!obj.puedeActuar()) 		return INERTE;
        if (!obj.estaVivo())            return MUERTO;
        if (obj.getBloqueaAccion())     return ATURDIDO;
        if (obj.getBloqueaMovimiento()) return INMOVILIZADO;
        if (obj.getBloqueaHechizos())   return SILENCIADO;
        return JUGANDO;
    }
}
