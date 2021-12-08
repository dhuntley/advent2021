package advent.day01;

import advent.common.util.InputReader;

public class DepthChecker {

    public static void main(String[] args) {
        Integer lastDepth = null;
        long depthIncreases = 0;
        
        for (Integer depth : InputReader.readIntegersFromInput("advent/day01/input1.txt")) {
            if (lastDepth != null && depth > lastDepth) {
                depthIncreases++;
            }
            lastDepth = depth;
        }

        System.out.println(depthIncreases);
    }
}