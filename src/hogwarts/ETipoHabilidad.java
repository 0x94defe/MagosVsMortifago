package hogwarts;

public enum ETipoHabilidad {
    DESTRUCTIVO("ataque", true, false, false),
    CURATIVO("curacion", false, false, false),
    ROBAR_MANA("roboMana", true, false, true),
    ENCANTAMIENTO("turnos", true, true, true),
	BUFF("turnos", false, false, true),
	DEBUFF("turnos", true, false, true);
 
    private final String stat;
    private final boolean ofensivo;
    private final boolean curativo;
    private final boolean especial;

    ETipoHabilidad(String stat, boolean ofensivo, boolean curativo, boolean especial) {
        this.stat = stat;
        this.ofensivo = ofensivo;
        this.curativo = curativo;
        this.especial = especial;
    }

    public String getStat() { return this.stat; }
    public boolean esOfensivo() { return this.ofensivo; }
    public boolean esCurativo() { return this.curativo; }
    public boolean esEspecial() { return this.especial; }
}
