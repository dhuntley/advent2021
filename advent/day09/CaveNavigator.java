package advent.day09;

import java.util.List;

import advent.common.util.InputReader;

public class CaveNavigator {
    
    public static boolean isLowPoint(int[][] caveMap, int i, int j) {
        return !(i > 0 && caveMap[i-1][j] <= caveMap[i][j] || 
            j > 0 && caveMap[i][j-1] <= caveMap[i][j] ||
            i < caveMap.length - 1 && caveMap[i+1][j] <= caveMap[i][j] ||
            j < caveMap[i].length - 1 && caveMap[i][j+1] <= caveMap[i][j]
        );
    } 

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day09/input.txt");
        
        int[][] caveMap = new int[lines.size()][lines.get(0).length()];

        for (int i=0; i<lines.size(); i++) {
            for (int j=0; j<lines.get(i).length(); j++) {
                caveMap[i][j] = Integer.parseInt(lines.get(i).substring(j, j+1));
            }
        }

        int lowCount = 0;
        int riskSum = 0;

        for (int i=0; i<caveMap.length; i++) {
            for (int j=0; j<caveMap[i].length; j++) {
                if (isLowPoint(caveMap, i, j)) {
                    
                    System.out.println("Low Point: (" + i + ", " +  j + ")");
                    System.out.println("Risk: " + (caveMap[i][j] + 1));
                    lowCount++;
                    riskSum += (caveMap[i][j] + 1);
                }
            }
        }

        System.out.println("LowCount: " + lowCount);
        System.out.println("RiskSum: " + riskSum);
    }
}
