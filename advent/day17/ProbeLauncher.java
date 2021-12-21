package advent.day17;

import advent.common.util.Coord2D;

public class ProbeLauncher {

    public static boolean isInBounds(Coord2D position, Coord2D minBounds, Coord2D maxBounds) {
        return position.x >= minBounds.x && position.x <= maxBounds.x && position.y >= minBounds.y && position.y <= maxBounds.y;
    }

    public static boolean fire(Coord2D origin, int xVelocityInit, int yVelocityInit, Coord2D minBounds, Coord2D maxBounds) {
        Coord2D position = new Coord2D(origin.x, origin.y);
        int xVelocity = xVelocityInit;
        int yVelocity = yVelocityInit;

        while (position.y >= minBounds.y && position.x <= maxBounds.x) {
            position.x += xVelocity;
            /*if (isInBounds(position, minBounds, maxBounds)) {
                return true;
            }*/
            position.y += yVelocity;
            if (isInBounds(position, minBounds, maxBounds)) {
                return true;
            }

            xVelocity = Integer.max(0, xVelocity - 1);
            yVelocity -= 1;
        }

        return false;
    }

    public static int maxHeight(int yInit, int yVelocityInit) {
        int y = yInit;
        int yVelocity = yVelocityInit;

        while (yVelocity > 0) {
            y += yVelocity;
            yVelocity--;
        }

        return y;
    }

    

    public static void main(String[] args) {
        Coord2D minBounds = new Coord2D(14, -267);
        Coord2D maxBounds = new Coord2D(50, -225);
        Coord2D origin = new Coord2D(0,0);

        int numCombos = 0;

        for (int x=1; x<=maxBounds.x; x++) {
            for (int y=-300; y<1000; y++) {
                if (fire(origin, x, y, minBounds, maxBounds)) {
                    System.out.println(x + ", " + y);
                    numCombos++;
                }
            }
        }

        System.out.println(numCombos);
    }
}
