package advent.day10;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import advent.common.util.InputReader;

public class ChunkValidator {

    private static final Map<Character, Character> openerMap = new HashMap<>();
    static {
        openerMap.put(')', '(');
        openerMap.put(']', '[');
        openerMap.put('}', '{');
        openerMap.put('>', '<');
    }

    private static final Map<Character, Character> closerMap = new HashMap<>();
    static {
        closerMap.put('(', ')');
        closerMap.put('[', ']');
        closerMap.put('{', '}');
        closerMap.put('<', '>');
    }

    private static final Map<Character, Integer> illegalCharacterScoreMap = new HashMap<>();
    static {
        illegalCharacterScoreMap.put(')', 3);
        illegalCharacterScoreMap.put(']', 57);
        illegalCharacterScoreMap.put('}', 1197);
        illegalCharacterScoreMap.put('>', 25137);
    }

    private static final Map<Character, Long> completionScoreMap = new HashMap<>();
    static {
        completionScoreMap.put(')', 1l);
        completionScoreMap.put(']', 2l);
        completionScoreMap.put('}', 3l);
        completionScoreMap.put('>', 4l);
    }

    public static int getSyntaxErrorScore(String line) {

        // Corruption check
        Deque<Character> chunkOpeners = new ArrayDeque<>();
        for (char c : line.toCharArray()) {
            if (closerMap.keySet().contains(c)) {
                // Starting a new chunk
                chunkOpeners.push(c);
            } else if (openerMap.keySet().contains(c)) {
                // Closing a chunk
                Character opener = chunkOpeners.pop();
                if (!opener.equals(openerMap.get(c))) {
                    return illegalCharacterScoreMap.get(c);
                }
            } else {
                System.err.println("Unrecognized character: " + c);
                System.exit(1);
            }
        }

        return 0;
    }

    public static String getCompletionSequence(String line) {
        Deque<Character> chunkOpeners = new ArrayDeque<>();
        for (char c : line.toCharArray()) {
            if (closerMap.keySet().contains(c)) {
                // Starting a new chunk
                chunkOpeners.push(c);
            } else if (openerMap.keySet().contains(c)) {
                // Closing a chunk
                chunkOpeners.pop();
            } else {
                System.err.println("Unrecognized character: " + c);
                System.exit(1);
            }
        }
        
        StringBuilder builder = new StringBuilder();
        while(!chunkOpeners.isEmpty()) {
            builder.append(closerMap.get(chunkOpeners.pop()));
        }

        return builder.toString();
    }

    public static long getCompletionSequenceScore(String sequence) {
        long score = 0;
        for (char c : sequence.toCharArray()) {
            score = score * 5l + completionScoreMap.get(c);
        }
        return score;
    }

    public static void main(String[] args) {
        Stream<String> completionSequenceStream = InputReader.readLinesFromInput("advent/day10/input.txt").stream().filter(line -> getSyntaxErrorScore(line) == 0).map(ChunkValidator::getCompletionSequence);
        long[] sortedScores = completionSequenceStream.mapToLong(ChunkValidator::getCompletionSequenceScore).sorted().toArray();
        System.out.println(sortedScores[sortedScores.length / 2]);
    }
}
