package hogwarts;

public enum ETipoHabilidad {
    CURATIVO("curacion", false, false, false),
    DESTRUCTIVO("ataque", true, false, false),
    UTILIDAD("especial", false, false, true),     //Teleport, Disipar
    ENCANTAR("especial", true, false, true),
    
    REGENERATIVO("turnos", false, true, false),
    DEGENERATIVO("turnos", true, true, false),
    BUFF("turnos", false, true, true),
    DEBUFF("turnos", true, true, true);
 
    private final String stat;
    private final boolean ofensivo;
    private final boolean turnado;
    private final boolean especial;

    ETipoHabilidad(String stat, boolean ofensivo, boolean turnado, boolean especial) {
        this.stat = stat;
        this.ofensivo = ofensivo;
        this.turnado = turnado;
        this.especial = especial;
    }

    public String getStat() { return this.stat; }
    public boolean esOfensivo() { return this.ofensivo; }
    public boolean esTurnado() { return this.turnado; }
    public boolean esEspecial() { return this.especial; }
}
