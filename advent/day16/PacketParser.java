package advent.day16;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import advent.common.util.InputReader;

public class PacketParser {
    
    private static String consumeBits(Deque<Character> transmission, int numBits) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<numBits; i++) {
            builder.append(transmission.poll());    
        }
        return builder.toString();
    }

    private static long consumeBitsAsLong(Deque<Character> transmission, int numBits) {
        return Long.parseLong(consumeBits(transmission, numBits), 2);
    }

    private static long consumeLiteralBody(Deque<Character> transmission) {
        StringBuilder literal = new StringBuilder();
        boolean hasMoreBits = true;
        while (hasMoreBits) {
            String chunk = consumeBits(transmission, 5);
            literal.append(chunk.substring(1, 5));
            if (chunk.charAt(0) == '0') {
                hasMoreBits = false;
            }
        }
        return Long.parseLong(literal.toString(), 2);
    }

    private static long consumePacket(Deque<Character> transmission) {
        long version = consumeBitsAsLong(transmission, 3);
        long typeId = consumeBitsAsLong(transmission, 3);

        if (typeId == 4) {
            return consumeLiteralBody(transmission);
        } else {
            long lengthTypeId = consumeBitsAsLong(transmission, 1);
            LinkedList<Long> subPacketValues = new LinkedList<>();
            
            if (lengthTypeId == 0) {
                long subPacketLength = consumeBitsAsLong(transmission, 15);
                // Consume subPackets
                long startSize = transmission.size();
                while (startSize - transmission.size() < subPacketLength) {
                    subPacketValues.add(consumePacket(transmission));
                }
            } else {
                long numSubPackets = consumeBitsAsLong(transmission, 11);
                // Consume subPackets
                for (int i=0; i<numSubPackets; i++) {
                    subPacketValues.add(consumePacket(transmission));
                }
            }

            long value = 0;
            if (typeId == 0) {
                //Sum
                for (Long v : subPacketValues) {
                    value += v;
                }
            } else if (typeId == 1) {
                //Prod
                value = 1;
                for (Long v : subPacketValues) {
                    value *= v;
                }
            } else if (typeId == 2) {
                //Min
                value = Long.MAX_VALUE;
                for (Long v : subPacketValues) {
                    value = Math.min(value, v);
                }
            } else if (typeId == 3) {
                //Max
                value = Long.MIN_VALUE;
                for (Long v : subPacketValues) {
                    value = Math.max(value, v);
                }
            } else if (typeId == 5) {
                //GT
                value = subPacketValues.getFirst() > subPacketValues.getLast() ? 1 : 0;
            } else if (typeId == 6) {
                //LT
                value = subPacketValues.getFirst() < subPacketValues.getLast() ? 1 : 0;
            } else if (typeId == 7) {
                //EQ
                value = subPacketValues.getFirst().equals(subPacketValues.getLast()) ? 1 : 0;
            }

            return value;
        }
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day16/input.txt");
        Deque<Character> transmission = new ArrayDeque<>();
        
        for (Character hexChar : lines.get(0).toLowerCase().toCharArray()) {
            String binString = Integer.toBinaryString(Integer.parseInt("" + hexChar, 16));
            String paddedBinString = String.format("%4s", binString).replace(" ", "0");
            for (char binChar : paddedBinString.toCharArray()) {
                transmission.add(binChar);
            }
        }

        System.out.println(consumePacket(transmission));
    }
}
