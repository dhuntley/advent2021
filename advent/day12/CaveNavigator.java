package advent.day12;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.common.util.InputReader;

public class CaveNavigator {
    
    private static class Cave {
        protected List<Cave> edges = new ArrayList<>();
        private final String name;
        private final boolean isSmall;

        public Cave(String n) {
            name = n;
            isSmall = n.equals(n.toLowerCase()) && !"start".equals(n) && !"end".equals(n);
        }

        public String getName() {
            return name;
        }

        public boolean getIsSmall() {
            return isSmall;
        }
    }

    private static int getNumPaths(Map<String, Cave> caves, Deque<String> partialPath, boolean doubleVisitExpended) {
        Cave currentCave = caves.get(partialPath.peek());

        if (currentCave.getName().equals("end")) {
            return 1;
        }

        int numPaths = 0;
        
        for (Cave neighbor : currentCave.edges) {
            /*if (!(neighbor.getIsSmall() && partialPath.contains(neighbor.getName()))) {
                partialPath.push(neighbor.getName());
                numPaths += getNumPaths(caves, partialPath);
                partialPath.pop();
            }*/

            boolean usingDoubleVisit = false;
            if (neighbor.getIsSmall() && partialPath.contains(neighbor.getName())) {
                if (doubleVisitExpended) {
                    continue;
                } else {
                    usingDoubleVisit = true;
                }
            }

            partialPath.push(neighbor.getName());
            numPaths += getNumPaths(caves, partialPath, usingDoubleVisit || doubleVisitExpended);
            partialPath.pop();
        }

        return numPaths;
    }

    public static void main(String[] args) {
        Map<String, Cave> caves = new HashMap<>();
        
        List<String> lines = InputReader.readLinesFromInput("advent/day12/input.txt");
        for (String line : lines) {
            String[] edge = line.split("-");
            caves.putIfAbsent(edge[0], new Cave(edge[0]));
            caves.putIfAbsent(edge[1], new Cave(edge[1]));
            
            if (!edge[1].equals("start")) {
                caves.get(edge[0]).edges.add(caves.get(edge[1]));
            }

            if (!edge[0].equals("start")) {
                caves.get(edge[1]).edges.add(caves.get(edge[0]));
            }
        }

        Deque<String> partialPath = new ArrayDeque<>();
        partialPath.push("start");
        System.out.println(getNumPaths(caves, partialPath, false));
    }
}
