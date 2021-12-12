package advent.day05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.common.util.Coord2D;
import advent.common.util.InputReader;

public class HeatVents {

    private static class Line {
        public Coord2D start;
        public Coord2D end;

        public Line(String input) {
            String[] tokens = input.split("(\\s|,|->)+");
            start = new Coord2D(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            end = new Coord2D(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
        }
    }

    private static class VentMap {
        private Map<Coord2D, Integer> grid = new HashMap<>();

        public void plot(Line line) {
            if (line.start.x == line.end.x) {
                for (int i=Math.min(line.start.y, line.end.y); i<=Math.max(line.start.y, line.end.y); i++) {
                    Coord2D point = new Coord2D(line.start.x, i);
                    grid.putIfAbsent(point, 0);
                    grid.put(point, grid.get(point) + 1);
                }
            } else if (line.start.y == line.end.y) {
                for (int i=Math.min(line.start.x, line.end.x); i<=Math.max(line.start.x, line.end.x); i++) {
                    Coord2D point = new Coord2D(i, line.start.y);
                    grid.putIfAbsent(point, 0);
                    grid.put(point, grid.get(point) + 1);
                }
            } else {
                int startY = line.start.x < line.end.x ? line.start.y : line.end.y;
                int endY = line.start.x < line.end.x ? line.end.y : line.start.y;
                for (int i=Math.min(line.start.x, line.end.x); i<=Math.max(line.start.x, line.end.x); i++) {
                    int distance = i - Math.min(line.start.x, line.end.x);
                    int y = startY > endY ? startY - distance : startY + distance;
                    Coord2D point = new Coord2D(i, y);
                    grid.putIfAbsent(point, 0);
                    grid.put(point, grid.get(point) + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        List<String> inputs = InputReader.readLinesFromInput("advent/day05/input.txt");
        
        List<Line> lines = new ArrayList<>();
        for (String input : inputs) {
            lines.add(new Line(input));
        }

        VentMap ventMap = new VentMap();
        for (Line line : lines) {
            ventMap.plot(line);
        }
        
        int intersectionCount = 0;
        for (var entry : ventMap.grid.entrySet()) {
            if (entry.getValue() >= 2) {
                intersectionCount++;
            }
        }
        System.out.println(intersectionCount);
    }
}
