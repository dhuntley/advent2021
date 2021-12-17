package advent.day15;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.common.util.Coord2D;
import advent.common.util.InputReader;

public class CavePathFinder {

    private static void addIfExists(Coord2D node, Set<Coord2D> set, Set<Coord2D> openSet) {
        if (openSet.contains(node)) {
            set.add(node);
        }
    }

    private static Set<Coord2D> getOpenNeighbors(Coord2D origin, Set<Coord2D> openNodes) {
        Set<Coord2D> neighbors = new HashSet<>();
        addIfExists(new Coord2D(origin.x - 1, origin.y), neighbors, openNodes);
        addIfExists(new Coord2D(origin.x + 1, origin.y), neighbors, openNodes);
        addIfExists(new Coord2D(origin.x, origin.y - 1), neighbors, openNodes);
        addIfExists(new Coord2D(origin.x, origin.y + 1), neighbors, openNodes);
        return neighbors;
    }
    
    public static int getMinPathCost(Coord2D start, Coord2D end, Map<Coord2D, Integer> nodeCosts) {
        Set<Coord2D> openNodes = new HashSet<>();
        Map<Coord2D, Integer> pathCosts = new HashMap<>();

        for (var entry : nodeCosts.entrySet()) {
            openNodes.add(entry.getKey());
            pathCosts.put(entry.getKey(), Integer.MAX_VALUE);
        }
        pathCosts.put(start, 0);

        while (!openNodes.isEmpty()) {
            int minPathCost = Integer.MAX_VALUE;
            Coord2D minCostNode = null;

            for (Coord2D node : openNodes) {
                if (pathCosts.get(node) < minPathCost) {
                    minPathCost = pathCosts.get(node);
                    minCostNode = node;
                }
            }

            openNodes.remove(minCostNode);

            for (Coord2D neighbor : getOpenNeighbors(minCostNode, openNodes)) {
                pathCosts.put(neighbor, Integer.min(minPathCost + nodeCosts.get(neighbor), pathCosts.get(neighbor)));
            }
        }

        return pathCosts.get(end);
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day15/input.txt");
        
        Map<Coord2D, Integer> riskLevels = new HashMap<>();
        int y = 0;
        for (String line : lines) {
            for (int x = 0; x < line.length(); x++) {
                riskLevels.put(new Coord2D(x, y), Integer.parseInt(line.substring(x, x + 1)));
            }
            y++;
        }

        Coord2D start = new Coord2D(0, 0);
        Coord2D end = new Coord2D(lines.get(0).length() - 1, lines.size() - 1);

        System.out.println(getMinPathCost(start, end, riskLevels));
    }
}
