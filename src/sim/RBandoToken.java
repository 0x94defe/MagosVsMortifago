package sim;

public record RBandoToken(int id, String nombre) {
    @Override
    public boolean equals(Object o) {
        return (o instanceof RBandoToken other)
            && this.id == other.id;
    }
}
