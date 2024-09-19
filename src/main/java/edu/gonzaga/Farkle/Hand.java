package edu.gonzaga.Farkle;

import java.util.Random;

/**
 * This class simulates a hand of dice with a specified number of dice. It provides methods to roll the dice
 * and retrieve their current values.
 */
class Hand {
    private final int[] dice;
    private final Random random;

    /**
     * Constructor to create a Hand object with a specified number of dice.
     * This constructor initializes the `dice` array with the provided `numberOfDice` and creates a new
     * `Random` object for rolling.
     *
     * @param numberOfDice The number of dice in the hand.
     */
    public Hand(int numberOfDice) {
        this.dice = new int[numberOfDice];
        this.random = new Random();
    }

    /**
     * Rolls all the dice in the hand.
     * This method iterates through the `dice` array and assigns a random value between 1 and 6 (inclusive)
     * to each element using the `random` object.
     */
    public void roll() {
        for (int i = 0; i < dice.length; i++) {
            dice[i] = random.nextInt(6) + 1;
        }
    }

    /**
     * Gets a copy of the current dice values in the hand.
     * This method returns a copy of the `dice` array to prevent modifications to the internal state of the object.
     *
     * @return int[] A copy of the array containing the current dice values.
     */
    public int[] getValues() {
        return dice;
    }
}