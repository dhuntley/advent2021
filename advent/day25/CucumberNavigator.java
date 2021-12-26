package advent.day25;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import advent.common.util.Coord2D;
import advent.common.util.InputReader;

public class CucumberNavigator {

    private static enum Cucumber {
        EAST,
        SOUTH
    }

    private static class CucumberState {
        private int numCols;
        private int numRows;

        private Map<Coord2D, Cucumber> cucumbers = new HashMap<>();

        public CucumberState(int numCols, int numRows) {
            this.numCols = numCols;
            this.numRows = numRows;
        }

        public void addCucumber(Coord2D pos, Cucumber cuke) {
            this.cucumbers.put(pos, cuke);
        }

        private Coord2D getEastMove(Coord2D pos) {
            return new Coord2D(pos.x == numCols - 1 ? 0 : pos.x + 1, pos.y);
        }

        private Coord2D getSouthMove(Coord2D pos) {
            return new Coord2D(pos.x, pos.y == numRows - 1 ? 0 : pos.y + 1);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int y=0; y<numRows; y++) {
                for (int x=0; x<numCols; x++) {
                    Coord2D pos = new Coord2D(x, y);
                    if (cucumbers.containsKey(pos)) {
                        Cucumber cuke = cucumbers.get(pos);
                        builder.append(cuke.equals(Cucumber.EAST) ? '>' : 'v');
                    } else {
                        builder.append('.');
                    }
                }
                builder.append('\n');
            }
            return builder.toString();
        }

        public static CucumberState advanceOneStep(CucumberState orig) {
            CucumberState intermediate = new CucumberState(orig.numCols, orig.numRows);
            CucumberState updated = new CucumberState(orig.numCols, orig.numRows);

            for (var entry : orig.cucumbers.entrySet()) {
                Coord2D pos = entry.getKey();
                Cucumber cuke = entry.getValue();

                if (!cuke.equals(Cucumber.EAST)) {
                    intermediate.addCucumber(pos, cuke);
                } else {
                    Coord2D eastMove = orig.getEastMove(pos);
                    intermediate.addCucumber(orig.cucumbers.containsKey(eastMove) ? pos : eastMove, cuke);
                }
            }

            for (var entry : intermediate.cucumbers.entrySet()) {
                Coord2D pos = entry.getKey();
                Cucumber cuke = entry.getValue();

                if (!cuke.equals(Cucumber.SOUTH)) {
                    updated.addCucumber(pos, cuke);
                } else {
                    Coord2D southMove = intermediate.getSouthMove(pos);
                    updated.addCucumber(intermediate.cucumbers.containsKey(southMove) ? pos : southMove, cuke);
                }
            }

            return updated;
        }
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day25/input.txt");

        CucumberState cukeState = new CucumberState(lines.get(0).length(), lines.size());

        for (int y=0; y<lines.size(); y++) {
            String line = lines.get(y);
            for (int x=0; x<line.length(); x++) {
                if (line.charAt(x) != '.') {
                    Coord2D pos = new Coord2D(x, y);
                    cukeState.addCucumber(pos, line.charAt(x) == '>' ? Cucumber.EAST : Cucumber.SOUTH);
                }
            }
        }

        String oldState;
        String newState = cukeState.toString();
        int epochs = 0;

        do {
            oldState = newState;
            cukeState = CucumberState.advanceOneStep(cukeState);
            newState = cukeState.toString();
            epochs++;
        } while (!oldState.equals(newState));

        System.out.println(epochs);
    }
}
