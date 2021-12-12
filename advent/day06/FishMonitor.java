package advent.day06;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import advent.common.util.InputReader;

public class FishMonitor {
    private static List<Integer> simulateDay(List<Integer> startFish) {
        
        long numNewFish = startFish.stream().filter(fish -> fish == 0).count();

        List<Integer> updatedFish = startFish.stream().map(fish -> fish == 0 ? 6 : fish - 1).collect(Collectors.toList());
        Integer[] babyFish = new Integer[(int)numNewFish];
        Arrays.fill(babyFish, 8);

        updatedFish.addAll(Arrays.asList(babyFish));

        return updatedFish;
    }
    
    public static void main(String[] args) {
        String[] tokens = InputReader.readLinesFromInput("advent/day06/input.txt").get(0).split(",");
        List<Integer> fish = Arrays.asList(tokens).stream().map(Integer::parseInt).collect(Collectors.toList());
        
        for (int i=0; i<200; i++) {
            fish = simulateDay(fish);
        }
        
        System.out.println(fish.size());
    }
}
