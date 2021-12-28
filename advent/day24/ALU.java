package advent.day24;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import advent.common.util.InputReader;

public class ALU {

    private class DigitState {
        public int digit;
        public long z;
    
        public DigitState(int digit, long z) {
            this.digit = digit;
            this.z = z;
        }
    
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof DigitState)) {
                return false;
            } else {
                DigitState other = (DigitState) o;
                return digit == other.digit && z == other.z;
            }
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(digit,z);
        }
    
        @Override
        public String toString() {
            return digit + ": " + z;
        }
    }

    private Map<String, Long> registers = new HashMap<>();

    private Deque<Long> inputQueue = new ArrayDeque<>();

    public ALU() {
        reset();
    }

    public void reset() {
        registers.put("w", 0l);
        registers.put("x", 0l);
        registers.put("y", 0l);
        registers.put("z", 0l);
    }

    public void setInputs(Long[] inputs) {
        this.inputQueue.clear();
        this.inputQueue.addAll(Arrays.asList(inputs));
    }

    public void setInput(Long input) {
        this.inputQueue.clear();
        this.inputQueue.add(input);
    }

    public long getValue(String registerKey) {
        return registers.get(registerKey);
    }

    public void runProgram(Collection<String> program, Long[] inputs) {
        reset();
        setInputs(inputs);
        for (String line : program) {
            if (!this.execute(line)) {
                System.err.println("early termination");
                // Execution terminates early if we hit an INP instruction with no input available
                break;
            }
        }
    }

    public void runProgram(Collection<String> program, Long[] inputs, long w, long x, long y, long z) {
        registers.put("w", w);
        registers.put("x", x);
        registers.put("y", y);
        registers.put("z", z);
        setInputs(inputs);
        for (String line : program) {
            if (!this.execute(line)) {
                System.err.println("early termination");
                // Execution terminates early if we hit an INP instruction with no input available
                break;
            }
        }
    }

    public void runProgram(Collection<String> program, Long input, long z) {
        reset();
        registers.put("z", z);
        setInput(input);
        for (String line : program) {
            if (!this.execute(line)) {
                System.err.println("early termination");
                // Execution terminates early if we hit an INP instruction with no input available
                break;
            }
        }
    }

    public boolean execute(String line) {
        String[] tokens = line.split(" ");
        String instruction = tokens[0];
        String arg1 = tokens[1];

        if (instruction.equals("inp")) {
            // Terminate early if we have exhausted the input queue
            if (inputQueue.isEmpty()) {
                return false;
            }
            registers.put(arg1, inputQueue.poll());
            return true;
        }

        String arg2 = tokens[2];

        Long arg1Value = registers.get(arg1);
        Long arg2Value;
        if (arg2.matches("-?\\d*")) {
            arg2Value = Long.parseLong(arg2);
        } else {
            arg2Value = registers.get(arg2);
        }

        if (instruction.equals("add")) {
            registers.put(arg1, arg1Value + arg2Value);
        } else if (instruction.equals("mul")) {
            registers.put(arg1, arg1Value * arg2Value);
        } else if (instruction.equals("div")) {
            registers.put(arg1, arg1Value / arg2Value);
        } else if (instruction.equals("mod")) {
            registers.put(arg1, arg1Value % arg2Value);
        } else if (instruction.equals("eql")) {
            registers.put(arg1, arg1Value.equals(arg2Value) ? 1l : 0l);
        }
        return true;
    } 

    public static Long[] formatInputNumber(long num) {
        Long[] inputs = new Long[14];
        String modelNumberString = Long.toString(num);
        for (int i=0; i<14; i++) {
            inputs[i] = Long.parseLong(modelNumberString.substring(i, i+1));
        }
        return inputs;
    }

    public static boolean validateInputNumber(long num) {
        String modelNumberString = Long.toString(num);
        return modelNumberString.length() == 14 && modelNumberString.indexOf("0") == -1;
    }

    public String findLargestSerialNumberSuffix(List<List<String>> programs, int digit, long inZ, Set<DigitState> digitStates) {

        if (digit == programs.size()) {
            return inZ == 0 ? "" : null;
        }

        for (long inW=1; inW<=9; inW++) {
            runProgram(programs.get(digit), inW, inZ);
            long outZ = registers.get("z");

            DigitState digitState = new DigitState(digit, outZ);
            if (digitStates.contains(digitState)) {
                continue;
            }

            String result = findLargestSerialNumberSuffix(programs, digit + 1, outZ, digitStates);
            if (result == null) {
                digitStates.add(digitState);
                continue;
            } else {
                return inW + result;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day24/input.txt");

        List<List<String>> programs = new ArrayList<>();
        
        List<String> currProgram = new ArrayList<>();
        programs.add(currProgram);
        for (String line : lines) {
            if (line.isEmpty() || line.isBlank()) {
                currProgram = new ArrayList<>();
                programs.add(currProgram);
            } else {
                currProgram.add(line);
            }
        }

        ALU alu = new ALU();
        Set<DigitState> digitStates = new HashSet<>();
        System.out.println(alu.findLargestSerialNumberSuffix(programs, 0, 0, digitStates));
    }
}
