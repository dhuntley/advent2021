package advent.day09;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import advent.common.util.Coord2D;
import advent.common.util.InputReader;

public class BasinMapper {
    
    public static boolean isLowPoint(int[][] caveMap, int i, int j) {
        return !(i > 0 && caveMap[i-1][j] <= caveMap[i][j] || 
            j > 0 && caveMap[i][j-1] <= caveMap[i][j] ||
            i < caveMap.length - 1 && caveMap[i+1][j] <= caveMap[i][j] ||
            j < caveMap[i].length - 1 && caveMap[i][j+1] <= caveMap[i][j]
        );
    }

    public static Coord2D getLowerNeighbor(int[][] caveMap, Coord2D origin) {
        if (origin.x > 0 && caveMap[origin.x-1][origin.y] < caveMap[origin.x][origin.y]) {
            return new Coord2D(origin.x-1, origin.y);
        } else if (origin.y > 0 && caveMap[origin.x][origin.y-1] < caveMap[origin.x][origin.y]) {
            return new Coord2D(origin.x, origin.y-1);
        } else if (origin.x < caveMap.length - 1 && caveMap[origin.x+1][origin.y] < caveMap[origin.x][origin.y]) {
            return new Coord2D(origin.x+1, origin.y);
        } else if (origin.y < caveMap[0].length - 1 && caveMap[origin.x][origin.y+1] < caveMap[origin.x][origin.y]) {
            return new Coord2D(origin.x, origin.y+1);
        }
        return null;
    }

    public static void walkToLowPoint(int[][] caveMap, Coord2D[][] basinLowPoints, Coord2D origin) {
        if (caveMap[origin.x][origin.y] == 9) {
            return;
        }

        Coord2D position = origin;
        Deque<Coord2D> path = new ArrayDeque<>();
        Coord2D localLow = null;

        while (!isLowPoint(caveMap, position.x, position.y)) {
            if (basinLowPoints[position.x][position.y] != null) {
                position = basinLowPoints[position.x][position.y];
                break;
            }

            // Roll downhill
            path.push(position);
            position = getLowerNeighbor(caveMap, position);
        }

        path.push(position);
        localLow = position;

        while (!path.isEmpty()) {
            position = path.pop();
            basinLowPoints[position.x][position.y] = localLow;
        }
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day09/input.txt");
        
        int[][] caveMap = new int[lines.size()][lines.get(0).length()];

        for (int i=0; i<lines.size(); i++) {
            for (int j=0; j<lines.get(i).length(); j++) {
                caveMap[i][j] = Integer.parseInt(lines.get(i).substring(j, j+1));
            }
        }

        // Tag each non-9 with basin
        Coord2D[][] basinLowPoints = new Coord2D[caveMap.length][caveMap[0].length];
        for (int i=0; i<caveMap.length; i++) {
            for (int j=0; j<caveMap[i].length; j++) {
                walkToLowPoint(caveMap, basinLowPoints, new Coord2D(i, j));
            }
        }
        
        Map<Coord2D, Integer> basinSizes = new HashMap<>();
        for (int i=0; i<caveMap.length; i++) {
            for (int j=0; j<caveMap[i].length; j++) {
                if (basinLowPoints[i][j] != null) {
                    basinSizes.putIfAbsent(basinLowPoints[i][j], 0);
                    basinSizes.put(basinLowPoints[i][j], basinSizes.get(basinLowPoints[i][j]) + 1);
                }
            }
        }

        List<Integer> sortedBasinSizes = basinSizes.values().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        System.out.println(sortedBasinSizes);
        System.out.println(sortedBasinSizes.get(0) * sortedBasinSizes.get(1) * sortedBasinSizes.get(2));
    }
}
