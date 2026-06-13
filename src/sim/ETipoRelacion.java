package sim;

public enum ETipoRelacion {
    ENEMIGO(-1),
    NEUTRAL(0),
    AMIGO(1);

    private final int valor;

    ETipoRelacion(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }
}
