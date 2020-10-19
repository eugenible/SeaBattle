package game;

public enum Grid {
    SHIP("\uD83D\uDEE5", 0),
    BATTLESHIP("", 4),
    CRUISER("", 3),
    SUBMARINE("", 2),
    DESTROYER("", 1),
    EMPTY("⬜", 0),
    HIT("\uD83D\uDFE5", 0),
    MISS("◾", 0),
    GAP("🟦", 0);

    private String sign;
    private int length;

    Grid(String sign, int length) {
        this.sign = sign;
        this.length = length;
    }

    public String toString() {
        return sign;
    }

    public int getLength() {
        return this.length;
    }
}
