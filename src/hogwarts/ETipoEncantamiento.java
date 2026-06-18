package hogwarts;
import sim.IAfectable;
import sim.ITurnable;

public enum ETipoEncantamiento implements ITurnable {
    ESCUDO {
        public void alIniciar(IAfectable objetivo) { objetivo.setBloqueaDanio(true); }
        public void alConsumirse(IAfectable objetivo, int turnosRestantes) { return; }
        public void alTerminar(IAfectable objetivo) { objetivo.setBloqueaDanio(false); }
        public boolean debeTerminar(IAfectable objetivo) { return !objetivo.getBloqueaDanio(); }
        public boolean restringeAccion() { return false; }
        public String getDescripcionEfecto() { return ""; }
        public ETipoHabilidad getTipoHabilidad() { return ETipoHabilidad.BUFF; }
    },
    ATURDIR {
        public void alIniciar(IAfectable objetivo) { objetivo.setBloqueaAccion(true); }
        public void alConsumirse(IAfectable objetivo, int turnosRestantes) { return; }
        public void alTerminar(IAfectable objetivo) { objetivo.setBloqueaAccion(false); }
        public boolean debeTerminar(IAfectable objetivo) { return false; }
        public boolean restringeAccion() { return true; }
        public String getDescripcionEfecto() { return "está aturdido y pierde el turno"; }
        public ETipoHabilidad getTipoHabilidad() { return ETipoHabilidad.DEBUFF; }
    },
    VENENO {
        public void alIniciar(IAfectable objetivo) { return; }
        public void alConsumirse(IAfectable objetivo, int turnosRestantes) { objetivo.recibirDanio(15); }
        public void alTerminar(IAfectable objetivo) { return; }
        public boolean debeTerminar(IAfectable objetivo) { return false; }
        public boolean restringeAccion() { return false; }
        public String getDescripcionEfecto() { return "sufre daño por veneno"; }
        public ETipoHabilidad getTipoHabilidad() { return ETipoHabilidad.DEGENERATIVO; }
    },
    RALENTIZAR {
        public void alIniciar(IAfectable objetivo) { objetivo.reducirMovimiento(2); }
        public void alConsumirse(IAfectable objetivo, int turnosRestantes) { return; }
        public void alTerminar(IAfectable objetivo) { objetivo.restaurarMovimiento(2); }
        public boolean debeTerminar(IAfectable objetivo) { return false; }
        public boolean restringeAccion() { return false; }
        public String getDescripcionEfecto() { return "está realentizado, se mueve mas despacio"; }
        public ETipoHabilidad getTipoHabilidad() { return ETipoHabilidad.DEBUFF; }
    },
    REGENERACION {
        public void alIniciar(IAfectable objetivo) { return; }
        public void alConsumirse(IAfectable objetivo, int turnosRestantes) { objetivo.recibirCuracion(10); }
        public void alTerminar(IAfectable objetivo) { return; }
        public boolean debeTerminar(IAfectable objetivo) { return false; }
        public boolean restringeAccion() { return false; }
        public String getDescripcionEfecto() { return "goza de salud regenerativa"; }
        public ETipoHabilidad getTipoHabilidad() { return ETipoHabilidad.REGENERATIVO; }
    };
	
	public abstract ETipoHabilidad getTipoHabilidad();
}
