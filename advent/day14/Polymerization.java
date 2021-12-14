package advent.day14;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import advent.common.util.InputReader;

public class Polymerization {

    public static Map<String, Long> expandPolymer(Map<String, Long> polymerPairCounts, Map<String, String> patternMap) {
        
        Map<String, Long> expandedPairCounts = new HashMap<>();

        for (var entry : polymerPairCounts.entrySet()) {
            String pair = entry.getKey();
            Long count = entry.getValue();
            String insertion = patternMap.get(pair);

            String firstPair = pair.substring(0, 1) + insertion;
            expandedPairCounts.putIfAbsent(firstPair, 0l);
            expandedPairCounts.put(firstPair, expandedPairCounts.get(firstPair) + count);
            
            String secondPair = insertion + pair.substring(1, 2);
            expandedPairCounts.putIfAbsent(secondPair, 0l);
            expandedPairCounts.put(secondPair, expandedPairCounts.get(secondPair) + count);
        }

        return expandedPairCounts;
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day14/input.txt");
        
        String polymer = lines.get(0);
        Map<String, String> patternMap = new HashMap<>();
        Map<String, Long> polymerPairCounts = new HashMap<>();
        Map<Character, Long> charCounts = new HashMap<>();
        
        lines.remove(0);
        lines.remove(0);

        for (String line : lines) {
            String[] tokens = line.split(" -> ");
            patternMap.put(tokens[0], tokens[1]);
        }

        for (int i=0; i<polymer.length()-1; i++) {
            String pair = polymer.substring(i, i+2);
            polymerPairCounts.putIfAbsent(pair, 0l);
            polymerPairCounts.put(pair, polymerPairCounts.get(pair) + 1);
        }

        for (int i=0; i<40; i++) {
            polymerPairCounts = expandPolymer(polymerPairCounts, patternMap);
        }

        // Convert pair counts to character counts
        for (var entry : polymerPairCounts.entrySet()) {
            Character startChar = entry.getKey().charAt(0);
            charCounts.putIfAbsent(startChar, 0l);
            charCounts.put(startChar, charCounts.get(startChar) + entry.getValue());
        }
        Character lastChar = polymer.charAt(polymer.length() - 1);
        charCounts.putIfAbsent(lastChar, 0l);
        charCounts.put(lastChar, charCounts.get(lastChar) + 1);

        Long maxFreq = Long.MIN_VALUE;
        Long minFreq = Long.MAX_VALUE;

        for (var entry : charCounts.entrySet()) {
            Long freq = entry.getValue();
            if (freq > maxFreq) {
                maxFreq = freq;
            }
            if (freq < minFreq) {
                minFreq = freq;
            }
        }

        System.out.println(maxFreq - minFreq);
    }
}
