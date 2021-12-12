package advent.day02;

import java.util.List;
import java.util.stream.Collectors;

import advent.common.util.InputReader;

public class Navigator {

    private static enum Direction {
        UP,
        DOWN,
        FORWARD
    }

    private static class Instruction {
        Direction direction;
        int magnitude;
        
        public Instruction(String line) {
            String[] tokens = line.split(" ");

            this.direction = Direction.valueOf(tokens[0].toUpperCase());
            this.magnitude = Integer.parseInt(tokens[1]);
        }
    }

    private static class Position {
        long x;
        long y;

        public Position(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    private static Position applyInstruction(Position start, Instruction instruction) {
        if (instruction.direction == Direction.UP) {
            return new Position(start.x, start.y - instruction.magnitude);
        } else if (instruction.direction == Direction.DOWN) {
            return new Position(start.x, start.y + instruction.magnitude);
        } else if (instruction.direction == Direction.FORWARD) {
            return new Position(start.x + instruction.magnitude, start.y);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        List<Instruction> instructions = InputReader.readLinesFromInput("advent/day02/input.txt").stream().map(Instruction::new).collect(Collectors.toList());
        
        Position start = new Position(0, 0);
        for (Instruction instruction : instructions) {
            start = applyInstruction(start, instruction);
        }

        System.out.println(start.x * start.y);
    }
}
