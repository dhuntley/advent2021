package advent.day13;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.common.util.Coord2D;
import advent.common.util.InputReader;

public class PaperFolder {

    public static Set<Coord2D> doFold(Set<Coord2D> coords, int axis, boolean foldOnX) {
        Set<Coord2D> foldedCoords = new HashSet<>();
        for (Coord2D point : coords) {
            int x = point.x;
            int y = point.y;
            if (foldOnX) {
                if (point.x > axis) {
                    x = axis - (point.x - axis);
                }
            } else {
                if (point.y > axis) {
                    y = axis - (point.y - axis);
                }
            }
            foldedCoords.add(new Coord2D(x, y));
        }

        return foldedCoords;
    }

    public static void printCoords(Set<Coord2D> coords, int maxX, int maxY) {
        for (int x=0; x<maxX; x++) {
            for (int y=0; y<maxY; y++) {
                if (coords.contains(new Coord2D(y,x))) {
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day13/input.txt");
        Set<Coord2D> coords = new HashSet<>();
        
        int index = 0;
        while (!lines.get(index).isEmpty()) {
            String[] tokens = lines.get(index).split(",");
            coords.add(new Coord2D(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
            index++;
        }

        index++;

        while (index < lines.size()) {
            String[] tokens = lines.get(index).split(" ")[2].split("=");
            boolean foldOnX = tokens[0].equals("x");
            Integer axis = Integer.parseInt(tokens[1]);

            coords = doFold(coords, axis, foldOnX);
            index++;
        }

        printCoords(coords, 6, 39);
    }
}
