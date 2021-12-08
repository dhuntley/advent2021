package advent.day01;

import java.util.List;

import advent.common.util.InputReader;

public class RangeDepthChecker {

    public static void main(String[] args) {
        
        long depthIncreases = 0;

        List<Integer> depths = InputReader.readIntegersFromInput("advent/day01/input1.txt");
        for (int i=0; i+3<depths.size(); i++) {
            long range1 = depths.get(i) + depths.get(i+1) + depths.get(i+2);
            long range2 = depths.get(i+1) + depths.get(i+2) + depths.get(i+3);

            if (range1 < range2) { depthIncreases++; }
        }

        System.out.println(depthIncreases);
    }
}