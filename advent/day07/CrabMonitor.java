package advent.day07;

import java.util.HashMap;
import java.util.Map;

import advent.common.util.InputReader;

public class CrabMonitor {

    private static int getFuelCost(Map<Integer, Integer> crabMap, int targetPos) {
        int fuel = 0;

        for (var entry : crabMap.entrySet()) {
            int distance = Math.abs(entry.getKey() - targetPos);
            int numCrabs = entry.getValue();

            fuel += distance * numCrabs;
        }

        return fuel;
    }

    private static int getComplexFuelCost(Map<Integer, Integer> crabMap, int targetPos) {
        int totalFuel = 0;

        for (var entry : crabMap.entrySet()) {
            int distance = Math.abs(entry.getKey() - targetPos);
            int fuel = 0;
            for (int i=1; i<=distance; i++) {
                fuel = fuel + i;
            }
            int numCrabs = entry.getValue();

            totalFuel += fuel * numCrabs;
        }

        return totalFuel;
    }

    public static void main(String[] args) {
        String[] tokens = InputReader.readLinesFromInput("advent/day07/input.txt").get(0).split(",");
        
        Map<Integer, Integer> crabMap = new HashMap<>();

        int minPos = Integer.MAX_VALUE;
        int maxPos = Integer.MIN_VALUE;

        for (String token : tokens) {
            Integer position = Integer.parseInt(token);
            crabMap.putIfAbsent(position, 0);
            crabMap.put(position, crabMap.get(position) + 1);
            
            minPos = Math.min(position, minPos);
            maxPos = Math.max(position, maxPos);
        }

        int targetPos = -1;
        int minFuel = Integer.MAX_VALUE;

        for (int i=minPos; i<=maxPos; i++) {
            int fuel = getComplexFuelCost(crabMap, i);
            //System.out.println(i + " : " + fuel);
            if (fuel < minFuel) {
                targetPos = i;
                minFuel = fuel;
            }
        }

        System.out.println(targetPos + " : " + minFuel);
    }
}
