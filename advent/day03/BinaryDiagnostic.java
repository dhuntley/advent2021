package advent.day03;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import advent.common.util.InputReader;

public class BinaryDiagnostic {
    private static char getMostCommonBit(List<String> diagnostics, int index) {
        Map<Character, Integer> charCount = new HashMap<>();
        for (String diagnostic : diagnostics) {
            Character character = diagnostic.charAt(index);
            charCount.putIfAbsent(character, 0);
            charCount.put(character, charCount.get(character) + 1);
        }
        
        int maxCount = Integer.MIN_VALUE;
        Character maxChar = null;
        for (var entry : charCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxChar = entry.getKey();
            } else if (entry.getValue() == maxCount && entry.getKey() == '1') {
                maxChar = entry.getKey();
            }
        }

        return maxChar;
    }

    private static String getGammaRate(List<String> diagnostics) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<diagnostics.get(0).length(); i++) {
            builder.append(getMostCommonBit(diagnostics, i));
        }
        return builder.toString();
    }

    private static String getRecursiveRating(List<String> diagnostics, int index, boolean keepMostCommon) {
        char commonChar = getMostCommonBit(diagnostics, index);
        char flippedChar = commonChar == '0' ? '1' : '0';
        final char filterChar = keepMostCommon ? commonChar : flippedChar;        
        System.out.println(diagnostics.toString());

        List<String> filteredDiagnostics = diagnostics.stream().filter(diagnostic -> diagnostic.charAt(index) == filterChar).collect(Collectors.toList());
        if (filteredDiagnostics.size() == 1) {
            return filteredDiagnostics.get(0);
        } else {
            return getRecursiveRating(filteredDiagnostics, index + 1, keepMostCommon);
        }
    }

    private static String flipBits(String binary) {
        StringBuilder builder = new StringBuilder();
        for (Character c : binary.toCharArray()) {
            builder.append(c == '0' ? '1' : '0');
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        List<String> diagnostics = InputReader.readLinesFromInput("advent/day03/input.txt");
        // String gamma = getGammaRate(diagnostics);
        // String epsilon = flipBits(gamma);
        // System.out.println(Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2));

        String oxygenGeneratorRating = getRecursiveRating(diagnostics, 0, true);
        System.out.println(oxygenGeneratorRating);
        String scrubberRating = getRecursiveRating(diagnostics, 0, false);
        System.out.println(scrubberRating);
        System.out.println(Integer.parseInt(oxygenGeneratorRating, 2) * Integer.parseInt(scrubberRating, 2));
    }
}
