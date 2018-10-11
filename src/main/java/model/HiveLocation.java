package model;

/**
 * Represents a location on the HiveBoard
 * Each instance of HiveLocation is equal
 * to another if aQ=Bq and aR=Br.
 */
public class HiveLocation {
    private final int q;
    private final int r;

    public HiveLocation(int q, int r) {
        this.q = q;
        this.r = r;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof HiveLocation)) {
            return false;
        }

        HiveLocation l = (HiveLocation) o;
        return (l.getR() == this.getR() && l.getQ() == this.getQ()); // Equal als aR is bR en aQ is bQ
    }
}
