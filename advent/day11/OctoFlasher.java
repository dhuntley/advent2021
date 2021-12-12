package advent.day11;

import java.util.List;

import advent.common.util.InputReader;

public class OctoFlasher {
    
    public static final int GRID_SIZE = 10;
    public static final int FLASH_POINT = 10;
    public static final int NUM_EPOCHS = 100;

    public static void incrementNeighbors(int[][] octopusGrid, int x, int y) {
        if (x > 0) {
            if (y > 0) {
                octopusGrid[x-1][y-1]++;
            }
            octopusGrid[x-1][y]++;
            if (y < GRID_SIZE - 1) {
                octopusGrid[x-1][y+1]++;
            }
        }

        if (y > 0) {
            octopusGrid[x][y-1]++;
        }
        if (y < GRID_SIZE - 1) {
            octopusGrid[x][y+1]++;
        }

        if (x < GRID_SIZE - 1) {
            if (y > 0) {
                octopusGrid[x+1][y-1]++;
            }
            octopusGrid[x+1][y]++;
            if (y < GRID_SIZE - 1) {
                octopusGrid[x+1][y+1]++;
            }
        }
    }

    public static int simulateEpoch(int[][] octopusGrid) {
        for (int i=0; i<GRID_SIZE; i++) {
            for (int j = 0; j<GRID_SIZE; j++) {
                octopusGrid[i][j]++;
            }
        }

        boolean[][] hasFlashed = new boolean[GRID_SIZE][GRID_SIZE];
        boolean didFlash;
        int numFlashes = 0;

        do {
            didFlash = false;

            for (int i=0; i<GRID_SIZE; i++) {
                for (int j = 0; j<GRID_SIZE; j++) {
                    if (octopusGrid[i][j] >= FLASH_POINT && !hasFlashed[i][j]) {
                        incrementNeighbors(octopusGrid, i, j);
                        hasFlashed[i][j] = true;
                        didFlash = true;
                        numFlashes++;
                    }
                }
            }
        } while (didFlash);

        for (int i=0; i<GRID_SIZE; i++) {
            for (int j = 0; j<GRID_SIZE; j++) {
                if (octopusGrid[i][j] >= FLASH_POINT) {
                    octopusGrid[i][j] = 0;
                }
            }
        }

        return numFlashes;
    }

    public static void main(String[] args) {
        int[][] octopusGrid = new int[GRID_SIZE][GRID_SIZE];

        List<String> lines = InputReader.readLinesFromInput("advent/day11/input.txt");
        for (int i=0; i<GRID_SIZE; i++) {
            for (int j = 0; j<GRID_SIZE; j++) {
                octopusGrid[i][j] = Integer.parseInt(lines.get(i).substring(j, j+1));
            }
        }

        int numFlashes = 0;
        for (int i=0; i<NUM_EPOCHS; i++) {
            numFlashes += simulateEpoch(octopusGrid);
        }
        System.out.println(numFlashes);
    }
}
