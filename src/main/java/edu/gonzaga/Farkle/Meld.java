package edu.gonzaga.Farkle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for handling the meld and scoring logic in the Farkle game.
 */
public class Meld extends PlayerDice {
    private final ArrayList<Integer> meldDice;

    public Meld() {
        meldDice = new ArrayList<>();
    }

    public void addToMeld(int diceValue) {
        meldDice.add(diceValue);
    }

    public boolean isInMeld(int diceValue) {
        return meldDice.contains(diceValue);
    }

    private static final boolean[] diceScored = new boolean[7];

    /**
     * Recursive helper method to generate all possible meld combinations.
     *
     * @param melds   The list to store the generated meld combinations.
     * @param meldDie The array representing the values of dice already in the meld.
     * @param index   The index used for recursion.
     */
    private static void list(ArrayList<int[]> melds, int[] meldDie, int index) {
        if (index == meldDie.length) {
            melds.add(Arrays.copyOf(meldDie, meldDie.length));
            return;
        }

        for (int i = index; i < meldDie.length; i++) {
            swap(meldDie, i, index);
            list(melds, meldDie, index + 1);
            swap(meldDie, i, index);
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array The array in which elements will be swapped.
     * @param i     The index of the first element to be swapped.
     * @param j     The index of the second element to be swapped.
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Calculates the score of the new dice added to the meld without considering the previous score.
     *
     * @param currentRoll The array representing the values of dice.
     * @return int The score of the new dice added to the meld.
     */
    public static int calculateMeldScore(int[] currentRoll) {
        // Reset the diceScored array for each calculation
        Arrays.fill(diceScored, false);

        // Populate meldDiceSizesCount.
        int[] meldDiceSizesCount = new int[7];
        for (int value : currentRoll) {
            meldDiceSizesCount[value]++;
        }

        // First check for straight & full house
        boolean straight = isStraight(currentRoll);
        boolean fullHouse = isFullHouse(meldDiceSizesCount);
        if (straight && fullHouse) {
            return 1500; // Both straight and full house, return highest score
        } else if (straight || fullHouse) {
            return 1500; // Either straight or full house
        } else {
            // Check for three pairs.
            int pairCount = 0;
            for (int i = 1; i <= 6; i++) {
                if (meldDiceSizesCount[i] == 2) {
                    pairCount++;
                }
            }
            if (pairCount == 3) {
                return 750; // Three pairs
            }

            // Calculate score for triple sets or more.
            return isTripleSet(meldDiceSizesCount);
        }
    }

    /**
     * Checks if there are any triple sets in the given dice values and calculates the score.
     *
     * @param meldDiceSizesCount The count of each dice value in the meld.
     * @return int The score obtained from the triple sets and individual 1s and 5s.
     */
    private static int isTripleSet(int[] meldDiceSizesCount) {
        int newDiceScore = 0;
        for (int i = 1; i <= 6; i++) {
            if (meldDiceSizesCount[i] >= 3) {
                int tripleSetPoints = (i == 1) ? 1000 : i * 100;
                if (meldDiceSizesCount[i] > 3) {
                    tripleSetPoints += (meldDiceSizesCount[i] - 3) * 100 * i;
                }
                newDiceScore += tripleSetPoints;
                // Mark the scored dice
                for (int j = 0; j < 3; j++) {
                    markDiceAsScored(i);
                }
            }
        }

        // Check for individual 1s.
        if (meldDiceSizesCount[1] < 3) {
            newDiceScore += meldDiceSizesCount[1] * 100;
            markDiceAsScored(1, meldDiceSizesCount[1]);
        }

        // Check for individual 5s.
        if (meldDiceSizesCount[5] < 3) {
            newDiceScore += meldDiceSizesCount[5] * 50;
            markDiceAsScored(5, meldDiceSizesCount[5]);
        }
        return newDiceScore;
    }

    /**
     * Marks the specified dice value as scored a certain number of times.
     *
     * @param diceValue The value of the dice to be marked as scored.
     * @param count The number of times the dice with the specified value should be marked as scored.
     */
    private static void markDiceAsScored(int diceValue, int count) {
        for (int i = 0; i < count; i++) {
            diceScored[diceValue] = true;
        }
    }

    /**
     * Marks the specified dice value as scored one time.
     *
     * @param diceValue The value of the dice to be marked as scored.
     */
    private static void markDiceAsScored(int diceValue) {
        diceScored[diceValue] = true;
    }


    /**
     * Checks if the given dice values form a straight (consecutive sequence 1, 2, 3, 4, 5, 6).
     *
     * @param diceValues The array of dice values to be checked for a straight.
     * @return boolean True if the dice values form a straight, false otherwise.
     */
    private static boolean isStraight(int[] diceValues) {
        return Arrays.equals(diceValues, new int[]{1, 2, 3, 4, 5, 6});
    }

    /**
     * Checks if the given set of dice values forms a Full House.
     *
     * @param diceSizesCount The count of each dice value.
     * @return boolean True if the hand is a Full House, otherwise false.
     */
    protected static boolean isFullHouse(int[] diceSizesCount) {
        boolean foundDouble = false;
        boolean foundTriple = false;
        int doubleValue = 0;
        int tripleValue = 0;

        for (int i = 1; i <= 6; i++) {
            if (diceSizesCount[i] == 3) {
                foundTriple = true;
                tripleValue = i;
            } else if (diceSizesCount[i] == 2) {
                foundDouble = true;
                doubleValue = i;
            }
        }
        return foundDouble && foundTriple && doubleValue != tripleValue;
    }
}