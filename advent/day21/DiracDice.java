package advent.day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DiracDice {
    
    private static final int WINNING_SCORE = 21;

    // Map from roll to frequency for all possible turn outcomes
    private static final Map<Integer, Integer> ROLL_MAP = new HashMap<>();
    static {
        ROLL_MAP.put(3, 1);
        ROLL_MAP.put(4, 3);
        ROLL_MAP.put(5, 6);
        ROLL_MAP.put(6, 7);
        ROLL_MAP.put(7, 6);
        ROLL_MAP.put(8, 3);
        ROLL_MAP.put(9, 1);
    }

    private static class GameState {
        int playerOnePosition;
        int playerTwoPosition;

        int playerOneScore;
        int playerTwoScore;

        boolean playerOneActive;

        public GameState(int pOnePos, int pOneScore, int pTwoPos, int pTwoScore, boolean pOneActive) {
            playerOnePosition = pOnePos;
            playerOneScore = pOneScore;
            playerTwoPosition = pTwoPos;
            playerTwoScore = pTwoScore;
            playerOneActive = pOneActive;
        }

        public GameState(GameState other) {
            playerOnePosition = other.playerOnePosition;
            playerOneScore = other.playerOneScore;
            playerTwoPosition = other.playerTwoPosition;
            playerTwoScore = other.playerTwoScore;
            playerOneActive = other.playerOneActive;
        }

        public boolean playerOneHasWon() {
            return playerOneScore >= WINNING_SCORE;
        }

        public boolean playerTwoHasWon() {
            return playerTwoScore >= WINNING_SCORE;
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerOneActive, playerOnePosition, playerOneScore, playerTwoPosition, playerTwoScore);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof GameState)) {
                return false;
            } else {
                GameState other = (GameState)o;
                return other.playerOneActive == playerOneActive &&
                    other.playerOnePosition == playerOnePosition &&
                    other.playerOneScore == playerOneScore &&
                    other.playerTwoPosition == playerTwoPosition &&
                    other.playerTwoScore == playerTwoScore;
            }
        }

        private static GameState advanceActivePlayerPosition(GameState initState, int roll) {
            GameState next = new GameState(initState);
    
            if (next.playerOneActive) {
                next.playerOnePosition = advancePosition(next.playerOnePosition, roll);
                next.playerOneScore+= next.playerOnePosition;
            } else {
                next.playerTwoPosition = advancePosition(next.playerTwoPosition, roll);
                next.playerTwoScore+= next.playerTwoPosition;
            }
    
            next.playerOneActive = !next.playerOneActive;
    
            return next;
        }
    }

    private static class GameStatistics {
        long numPlayerOneWins;
        long numPlayerTwoWins;

        public GameStatistics(long pOneWins, long pTwoWins) {
            numPlayerOneWins = pOneWins;
            numPlayerTwoWins = pTwoWins;
        }
    }

    private static class GameTree {
        // Possible GameTrees with the state after this turn
        private List<GameTree> children;

        // The game state before this turn is taken
        private GameState state;

        // Statistics for simulations run initiating from this game state
        private GameStatistics gameStatistics;

        public GameTree(GameState s) {
            state = s;
        }

        // Simulate all branches to completion
        public GameStatistics simulate(Map<GameState, GameTree> gameBook) {
            
            if (state.playerOneHasWon()) {
                gameStatistics = new GameStatistics(1, 0);
            } else if (state.playerTwoHasWon()) {
                gameStatistics = new GameStatistics(0, 1);
            } else {
                children = new ArrayList<>();
                long numPlayerOneWins = 0;
                long numPlayerTwoWins = 0;
                for (var entry : ROLL_MAP.entrySet()) {
                    long freq = entry.getValue();
                    GameState childState = GameState.advanceActivePlayerPosition(state, entry.getKey());
                    GameTree childTree;

                    if (gameBook.containsKey(childState)) {
                        childTree = gameBook.get(childState);
                    } else {
                        childTree = new GameTree(childState);
                        childTree.simulate(gameBook);
                        gameBook.put(childState, childTree);
                    }

                    children.add(childTree);
                    numPlayerOneWins += childTree.gameStatistics.numPlayerOneWins * freq;
                    numPlayerTwoWins += childTree.gameStatistics.numPlayerTwoWins * freq;
                }
                gameStatistics = new GameStatistics(numPlayerOneWins, numPlayerTwoWins);
            }

            return this.gameStatistics;
        }
    }

    private static int advancePosition(int position, int roll) {
        return ((position + roll -1) % 10) + 1;
    }

    public static void main(String[] args) {
        GameState initState = new GameState(9, 0, 4, 0, true);
        GameTree gameTree = new GameTree(initState);

        Map<GameState, GameTree> gameBook = new HashMap<>();

        GameStatistics gameStats = gameTree.simulate(gameBook);

        System.out.println("Done sim");
        System.out.println("P1 Wins: " + gameStats.numPlayerOneWins);
        System.out.println("P2 Wins: " + gameStats.numPlayerTwoWins);  
    }
}
