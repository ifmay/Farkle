package edu.gonzaga.Farkle;

 /**
 * Represents a player in the game.
 */
class Player {
    private final String name;
    private int totalScore;
    private int roundScore;
    private int currentMeldScore;

    /**
     * Constructs a player with the given name.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the player.
     *
     * @return String The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the total score of the player.
     *
     * @return int The total score of the player.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the total score of the player.
     *
     * @param totalScore The total score to set.
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}