package advent.day08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import advent.common.util.InputReader;

public class DigitalDisplayDecoder {

    private static class Signal {
        private String segmentValues;

        public Signal(String values) {
            char[] charValues = values.toCharArray();
            Arrays.sort(charValues);
            segmentValues = new String(charValues);
        }

        public int getNumSegments() {
            return segmentValues.length();
        }

        public boolean isSupersetOf(Signal other) {
            for (char c : other.segmentValues.toCharArray()) {
                if (this.segmentValues.indexOf(c) == -1) {
                    return false;
                }
            }
            return true;
        }

        public boolean isSubsetOf(Signal other) {
            for (char c : this.segmentValues.toCharArray()) {
                if (other.segmentValues.indexOf(c) == -1) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Signal && this.segmentValues.equals(((Signal)o).segmentValues);
        }

        @Override
        public int hashCode() {
            return Objects.hash(segmentValues);
        }
    }

    private static class DigitalDisplay {
        private Signal[] signalMap = new Signal[10];

        private Signal[] display = new Signal[4];

        public DigitalDisplay(String inputLine) {
            String[] tokens = inputLine.split(" ");

            List<Signal> signals = new ArrayList<>(10);
            for (int i=0; i<10; i++) {
                signals.add(new Signal(tokens[i]));
            }
            initSignalMap(signals);

            for (int i=0; i<4; i++) {
                display[i] = new Signal(tokens[i+11]);
            }
        }

        public int getDisplayNumber() {
            int r = 1000;
            int number = 0;
            for (Signal signal : display) {
                for (int i=0; i<signalMap.length; i++) {
                    if (signal.equals(signalMap[i])) {
                        number += r * i;
                    }
                }
                r = r / 10;
            }
            return number;
        }

        private void initSignalMap(List<Signal> signals) {
            // 1, 4, 7, 8
            for (Signal signal : signals) {
                int numSegments = signal.getNumSegments();
                if (numSegments == 2) {
                    signalMap[1] = signal;
                } else if (numSegments == 3) {
                    signalMap[7] = signal;
                } else if (numSegments == 4) {
                    signalMap[4] = signal;
                } else if (numSegments == 7) {
                    signalMap[8] = signal;
                }
            }
            
            signals.remove(signalMap[1]);
            signals.remove(signalMap[7]);
            signals.remove(signalMap[4]);
            signals.remove(signalMap[8]);

            // 9
            for (Signal signal : signals) {
                if (signal.isSupersetOf(signalMap[4])) {
                    signalMap[9] = signal;
                }
            }
            signals.remove(signalMap[9]);

            // 0, 3
            for (Signal signal : signals) {
                if (signal.isSupersetOf(signalMap[7])) {
                    if (signal.getNumSegments() == 6) {
                        signalMap[0] = signal;
                    } else {
                        signalMap[3] = signal;
                    }
                }
            }
            signals.remove(signalMap[0]);
            signals.remove(signalMap[3]);

            // 6
            for (Signal signal : signals) {
                if (signal.getNumSegments() == 6) {
                    signalMap[6] = signal;
                }
            }
            signals.remove(signalMap[6]);

            // 2, 5
            for (Signal signal : signals) {
                if (signal.isSubsetOf(signalMap[9])) {
                    signalMap[5] = signal;
                } else {
                    signalMap[2] = signal;
                }
            }
            signals.remove(signalMap[2]);
            signals.remove(signalMap[5]);
        }
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day08/input.txt");
        
        long sum = 0;
        
        for (String line : lines) {
            DigitalDisplay display = new DigitalDisplay(line);
            sum += display.getDisplayNumber();
        }

        System.out.println(sum);
        
    }
}
