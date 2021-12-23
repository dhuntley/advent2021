package advent.day21;

public class SimpleDice {
    
    public abstract static  class Die {
        private int rollCount = 0;
        
        public abstract int roll();

        protected void incrementRollCount() {
            rollCount++;
        }

        public int getRollCount() {
            return rollCount;
        }
    }

    private static class DeterministicD100 extends Die {
        @Override
        public int roll() {
            int value = getRollCount() % 100 + 1;
            incrementRollCount();
            return value;
        }
    }

    private static int advancePosition(int position, int roll) {
        return ((position + roll -1) % 10) + 1;
    }

    public static void main(String[] args) {
        int playerOnePosition = 9;
        int playerTwoPosition = 4;

        int playerOneScore = 0;
        int playerTwoScore = 0;

        boolean playerOneActive = true;

        Die die = new DeterministicD100();

        while (playerOneScore < 1000 && playerTwoScore < 1000) {
            int roll = die.roll() + die.roll() + die.roll();

            if (playerOneActive) {
                playerOnePosition = advancePosition(playerOnePosition, roll);
                playerOneScore+= playerOnePosition;
            } else {
                playerTwoPosition = advancePosition(playerTwoPosition, roll);
                playerTwoScore+= playerTwoPosition;
            }
            playerOneActive = !playerOneActive;
        }

        System.out.println(playerOneScore);
        System.out.println(playerTwoScore);
        System.out.println(die.getRollCount());
        System.out.println(Math.min(playerOneScore, playerTwoScore) * die.getRollCount());
    }
}
