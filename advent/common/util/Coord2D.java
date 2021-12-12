package advent.common.util;

import java.util.Objects;

public class Coord2D {
    public int x;
    public int y;

    public Coord2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coord2D)) {
            return false;
        } else {
            Coord2D other = (Coord2D) o;
            return x == other.x && y == other.y;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }
}