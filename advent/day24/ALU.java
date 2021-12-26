package advent.day24;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import advent.common.util.InputReader;

public class ALU {

    

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
            this.execute(line);
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

        Map<String, Long> validSuffixes = new HashMap<>();
        for (long i=1; i<=9; i++) {
            for (long j=-1000; j<=1000; j++) {
                alu.runProgram(programs.get(13), i, j);
                if (alu.getValue("z") == 0) {
                    validSuffixes.put(Long.toString(i), j);
                }
            }
        }
        
        System.out.println(validSuffixes);

        for (int digit = 12; digit >= 1; digit--) {
            Map<String, Long> updatedSuffixes = new HashMap<>();
            for (var entry : validSuffixes.entrySet()) {
                String suffix = entry.getKey();
                long z = entry.getValue();
                //System.out.println(suffix);
                
                for (long i=1; i<=9; i++) {
                    for (long j=0; j<=10000; j++) {
                        
                        alu.runProgram(programs.get(digit), i, j);
                        if (alu.getValue("z") == z) {
                            updatedSuffixes.put(Long.toString(i) + suffix, j);
                        }
                    }
                }
            }
            validSuffixes = updatedSuffixes;
            System.out.println(validSuffixes);
        }

        Map<String, Long> updatedSuffixes = new HashMap<>();
        for (var entry : validSuffixes.entrySet()) {
            String suffix = entry.getKey();
            long z = entry.getValue();
            //System.out.println(suffix);
            
            for (long i=1; i<=9; i++) { 
                alu.runProgram(programs.get(0), i, 0);
                if (alu.getValue("z") == z) {
                    updatedSuffixes.put(Long.toString(i) + suffix, 0l);
                }
            }
        }
        validSuffixes = updatedSuffixes;



        /*for (var entry : validSuffixes.entrySet()) {
            String suffix = entry.getKey();
            long z = entry.getValue();
            alu.runProgram(programs.get(12), Long.parseLong(suffix.substring(0,1)), z);
            alu.runProgram(programs.get(13), Long.parseLong(suffix.substring(1,2)), alu.getValue("z"));
            System.out.println(alu.getValue("z"));
        }*/

        System.out.println(validSuffixes);



        // Long[] input = { 9l };
        // //alu.runProgram(program, input, 0, 0, 0, 0);
        // System.out.println(alu.getValue("z"));

        // long i = 0l;
        // /*while (i < 10000) {
        //     alu.runProgram(program, input, 0, 0, 0, 0);
        //     System.out.println()
        //     if (alu.getValue("z") == 0) {
        //         System.out.println(i);
        //     }
        //     i++;
        // }*/

        // long inputNumber = 13579246899999l;
        // /*while (true) {
        //     if (validateInputNumber(inputNumber)) {
        //         alu.runProgram(program, formatInputNumber(inputNumber));
        //         if (alu.getValue("z") == 0) {
        //             System.out.println("VALID ID: " + inputNumber);
        //             //break;
        //         }
        //     }
        //     inputNumber++;
        //     break;
        // }*/
    }
}
