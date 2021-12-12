package advent.day06;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import advent.common.util.InputReader;

public class FishMonitorBetter {
    private static long[] simulateDay(long[] startFish) {
        long[] endFish = new long[9];
        for (int i=0; i<8; i++) {
            endFish[i] = startFish[i+1];
        }

        endFish[6] += startFish[0];
        endFish[8] = startFish[0];

        return endFish;
    }
    
    public static void main(String[] args) {
        String[] tokens = InputReader.readLinesFromInput("advent/day06/input.txt").get(0).split(",");
        long[] fish = new long[9];

        for (String token : tokens) {
            fish[Integer.parseInt(token)]++;
        }
        
        for (int i=0; i<256; i++) {
            fish = simulateDay(fish);
        }

        long sum = 0;
        for (int i=0; i<fish.length; i++) {
            sum += fish[i];
        }
        
        System.out.println(sum);
    }
}
