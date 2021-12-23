package advent.day15;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import advent.common.util.Coord2D;
import advent.common.util.InputReader;

public class BigCavePathFinder {

    public static class PrioritizedEntity<T> implements Comparable<PrioritizedEntity<T>> {
        private T t;
        private Integer priority;

        public PrioritizedEntity(T t, Integer priority) {
            this.t = t;
            this.priority = priority;
        }

        public Integer getPriority() {
            return priority;
        }

        public T getEntity() {
            return t;
        }

        @Override
        public int compareTo(PrioritizedEntity<T> other) {
            return priority.compareTo(other.priority);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PrioritizedEntity<?>)) {
                return false;
            }
            PrioritizedEntity<?> other = (PrioritizedEntity<?>)o;
            return this.t.equals(other.t);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t, priority);
        }
    }

    public static class PriorityMap<T> {
        private Map<T, Integer> priorities = new HashMap<>();
        private PriorityQueue<PrioritizedEntity<T>> entities = new PriorityQueue<>();

        public void put(T entity, Integer priority) {
            priorities.put(entity, priority);
            entities.add(new PrioritizedEntity<>(entity, priority));
        }

        public PrioritizedEntity<T> poll() {
            PrioritizedEntity<T> entry;

            do {
                entry = entities.poll();
            }
            while (!priorities.containsKey(entry.getEntity()));
            
            priorities.remove(entry.getEntity());
            return entry;
        }

        public boolean isEmpty() {
            return priorities.isEmpty();
        }
    }
    
    private static void addIfNotClosed(Coord2D node, Set<Coord2D> set, Map<Coord2D, Integer> nodeCosts, Set<Coord2D> closedSet) {
        if (nodeCosts.containsKey(node) && !closedSet.contains(node)) {
            set.add(node);
        }
    }

    private static Set<Coord2D> getOpenNeighbors(Coord2D origin, Map<Coord2D, Integer> nodeCosts, Set<Coord2D> closedSet) {
        Set<Coord2D> neighbors = new HashSet<>();
        addIfNotClosed(new Coord2D(origin.x - 1, origin.y), neighbors, nodeCosts, closedSet);
        addIfNotClosed(new Coord2D(origin.x + 1, origin.y), neighbors, nodeCosts, closedSet);
        addIfNotClosed(new Coord2D(origin.x, origin.y - 1), neighbors, nodeCosts, closedSet);
        addIfNotClosed(new Coord2D(origin.x, origin.y + 1), neighbors, nodeCosts, closedSet);
        return neighbors;
    }
    
    public static int getMinPathCost(Coord2D start, Coord2D end, Map<Coord2D, Integer> nodeCosts) {
        PriorityMap<Coord2D> openNodes = new PriorityMap<>();
        Set<Coord2D> closedNodes = new HashSet<>();
        Map<Coord2D, Integer> pathCosts = new HashMap<>();

        openNodes.put(start, 0);
        pathCosts.put(start, 0);

        while (!openNodes.isEmpty()) {
            PrioritizedEntity<Coord2D> entry = openNodes.poll();
            int minPathCost = entry.getPriority();
            Coord2D minCostNode = entry.getEntity();

            closedNodes.add(minCostNode);
            if (end.equals(minCostNode)) {
                break;
            }

            for (Coord2D neighbor : getOpenNeighbors(minCostNode, nodeCosts, closedNodes)) {
                pathCosts.putIfAbsent(neighbor, Integer.MAX_VALUE);
                int pathCost = Integer.min(minPathCost + nodeCosts.get(neighbor), pathCosts.get(neighbor));
                pathCosts.put(neighbor, pathCost);
                openNodes.put(neighbor, pathCost);
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
                for (int i=0; i<5; i++) {
                    for (int j=0; j<5; j++) {
                        int risk = ((Integer.parseInt(line.substring(x, x + 1)) + i + j) - 1) % 9 + 1;
                        riskLevels.put(new Coord2D(x + i * line.length(), y + j * lines.size()), risk);
                    }
                }
            }
            y++;
        }

        Coord2D start = new Coord2D(0, 0);
        Coord2D end = new Coord2D(lines.get(0).length() * 5 - 1, lines.size() * 5 - 1);

        System.out.println(getMinPathCost(start, end, riskLevels));
    }
}
