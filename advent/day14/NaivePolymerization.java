package advent.day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.common.util.InputReader;

public class NaivePolymerization {

    public static String expandPolymer(String template, Map<String, String> patternMap) {
        StringBuilder polymerBuilder = new StringBuilder();

        for (int i=0; i<template.length()-1; i++) {
            polymerBuilder.append(template.charAt(i));
            polymerBuilder.append(patternMap.get(template.substring(i, i+2)));
        }
        polymerBuilder.append(template.charAt(template.length()-1));

        return polymerBuilder.toString();
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day14/input.txt");
        
        String polymer = lines.get(0);
        Map<String, String> patternMap = new HashMap<>();
        Map<Character, Long> charCounts = new HashMap<>();
        
        lines.remove(0);
        lines.remove(0);

        for (String line : lines) {
            String[] tokens = line.split(" -> ");
            patternMap.put(tokens[0], tokens[1]);
        }

        for (int i=0; i<10; i++) {
            polymer = expandPolymer(polymer, patternMap);
        }

        for (Character c : polymer.toCharArray()) {
            charCounts.putIfAbsent(c, 0l);
            charCounts.put(c, charCounts.get(c) + 1);
        }

        Character mostCommonChar;
        Character leastCommonChar;

        Long maxFreq = Long.MIN_VALUE;
        Long minFreq = Long.MAX_VALUE;

        for (var entry : charCounts.entrySet()) {
            Long freq = entry.getValue();
            Character c = entry.getKey();
            if (freq > maxFreq) {
                maxFreq = freq;
                mostCommonChar = c;
            }
            if (freq < minFreq) {
                minFreq = freq;
                mostCommonChar = c;
            }
        }

        System.out.println(maxFreq - minFreq);
    }
}
