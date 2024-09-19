package edu.gonzaga.Farkle;

/**
 * Class representing the dice held by a player.
 */
public class PlayerDice extends Die {
    private final Die[] freeDie;

    /**
     * Constructor for the PlayerDice class, initializes arrays of melded and free dice.
     */
    public PlayerDice(int numFreeDie) {
        freeDie = new Die[numFreeDie];
        for (int i = 0; i < numFreeDie; i++) {
            freeDie[i] = new Die();
        }
    }

    /**
     * Default constructor with no dice initially
     */
    public PlayerDice() {
        this(0);
    }
}