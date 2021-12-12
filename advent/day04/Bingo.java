package advent.day04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.common.util.InputReader;

public class Bingo {

    private static final int BOARD_SIZE = 5;

    private static class Board {
        boolean[][] markers = new boolean[BOARD_SIZE][BOARD_SIZE];
        String[][] numbers = new String[BOARD_SIZE][BOARD_SIZE];

        boolean hasWon = false;
        String winningNumber;

        public Board(List<String> inputs) {
            for (int i=0; i<BOARD_SIZE; i++) {
                numbers[i] = inputs.get(i).trim().split("\\s+");    
            }
        }

        public boolean markNumber(String number) {
            if (hasWon) {
                return false;
            }
            
            for (int i=0; i<BOARD_SIZE; i++) {
                for (int j=0; j<BOARD_SIZE; j++) {
                    if (numbers[i][j].equals(number)) {
                        markers[i][j] = true;
                    }
                }
            }

            if (getIsWinner()) {
                winningNumber = number;
                hasWon = true;
                return true;
            }

            return false;
        }

        public boolean getIsWinner() {
            for (int i=0; i<BOARD_SIZE; i++) {
                int rowCount = 0;
                int colCount = 0;
                for (int j=0; j<BOARD_SIZE; j++) {
                    rowCount += (markers[i][j] ? 1 : 0);
                    colCount += (markers[j][i] ? 1 : 0);
                }

                if (rowCount == 5 || colCount == 5) {
                    return true;
                }
            }

            return false;
        }

        public int getSheetScore() {
            int sum = 0;
            for (int i=0; i<BOARD_SIZE; i++) {
                for (int j=0; j<BOARD_SIZE; j++) {
                    if (!markers[i][j]) {
                        sum += Integer.parseInt(numbers[i][j]);
                    }
                }
            }
            return sum * Integer.parseInt(winningNumber);
        }
    }

    public static void main(String[] args) {
        List<String> inputs = InputReader.readLinesFromInput("advent/day04/input.txt");
        
        List<Board> boards = new ArrayList<>();
        List<String> callNumbers = Arrays.asList(inputs.get(0).split(","));
        
        for (int i=2; i+BOARD_SIZE<=inputs.size(); i = i+6) {
            boards.add(new Board(inputs.subList(i, i+BOARD_SIZE)));
        }

        Board lastWinner = null;
        for (String callNumber : callNumbers) {
            for (Board board : boards) {
                if (board.markNumber(callNumber)) {
                    lastWinner = board;
                }
                /*if (board.getIsWinner()) {
                    System.out.println("DING DING DING: " + board.getSheetScore());
                    System.exit(0);
                }*/
            }
        }
        
        System.out.println(lastWinner.getSheetScore());

    }
}
