package edu.gonzaga.Farkle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// import com.github.dnsev.identicon.Identicon;

// Java Swing based Farkle frontend
class Farkle {
    Meld meld;
    Hand hand;

    // Main game GUI window and two main panels (left & right)
    JFrame mainWindowFrame;

    // Dice view, user input, reroll status, and reroll button
    JTextField playerNameTextField = new JTextField();

    // Buttons for showing dice and checkboxes for meld include/exclude
    ArrayList<JButton> diceButtons = new ArrayList<>();
    ArrayList<JCheckBox> meldCheckboxes = new ArrayList<>();
    ArrayList<Integer> imageIndices;

    JButton rerollButton = new JButton("Reroll");
    JTextField diceDebugLabel = new JTextField();
    JLabel meldScoreTextLabel = new JLabel();
    JLabel roundScoreTextLabel = new JLabel();
    JLabel totalScoreTextLabel = new JLabel();

    JButton rollButton = new JButton();
    JButton bankButton = new JButton("Bank");
    JButton newRoundButton = new JButton("New Round");

    JPanel playerInfoPanel = new JPanel();
    JPanel diceControlPanel = new JPanel();
    JPanel meldControlPanel = new JPanel();
    DiceImages diceImages = new DiceImages("media/");
    private int totalScore = 0;
    private int roundScore = 0;

    // Constructor for the actual Farkle object
    public Farkle() {
        // Create any object you'll need for storing the game:
        hand = new Hand(6);
        meld = new Meld();
        imageIndices = new ArrayList<>();
    }

    // Main method to run the GUI
    public static void main(String[] args) {
        Farkle app = new Farkle();
        app.runGUI();
    }

    // Sets up the full Swing GUI, but does not do any callback code
    void setupGUI() {
        // Make and configure the window itself
        this.mainWindowFrame = new JFrame("Izzy's GUI Farkle");
        this.mainWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainWindowFrame.setLocation(100, 100);
        this.mainWindowFrame.setPreferredSize(new Dimension(1000, 400));


        // Player info and roll button panel
        this.playerInfoPanel = genPlayerInfoPanel();

        // Dice status and checkboxes to show the hand and which to include in the meld
        this.diceControlPanel = genDiceControlPanel();

        // The bottom Meld control panel
        this.meldControlPanel = genMeldControlPanel();

        mainWindowFrame.getContentPane().add(BorderLayout.NORTH, this.playerInfoPanel);
        mainWindowFrame.getContentPane().add(BorderLayout.CENTER, this.diceControlPanel);
        mainWindowFrame.getContentPane().add(BorderLayout.SOUTH, this.meldControlPanel);
        mainWindowFrame.pack();
    }

    /**
     * Generates and returns a JPanel containing components for meld control.
     * <p>
     * This method creates a JPanel with a flow layout. It includes components such as a label
     * for meld score, a button to calculate meld, and a label to display the meld score.
     *
     * @return A JPanel containing components for meld control.
     */
    private JPanel genMeldControlPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());

        JLabel meldScoreLabel = new JLabel("Meld Score:");
        this.meldScoreTextLabel.setText("0");

        JLabel roundScoreTextLabel = new JLabel("Round Score:");
        this.roundScoreTextLabel.setText("0");

        JLabel totalScoreLabel = new JLabel("Total Score:");
        this.totalScoreTextLabel.setText("0");

        newPanel.add(rerollButton);
        newPanel.add(bankButton);
        newPanel.add(newRoundButton);

        newPanel.add(meldScoreLabel);
        newPanel.add(this.meldScoreTextLabel);

        newPanel.add(roundScoreTextLabel);
        newPanel.add(this.roundScoreTextLabel);

        newPanel.add(totalScoreLabel);
        newPanel.add(this.totalScoreTextLabel);

        return newPanel;
    }

    /**
     * Generates and returns a JPanel containing components for dice control.
     * <p>
     * This method creates a JPanel with a black border and a grid layout (3 rows, 7 columns).
     * It includes components such as labels for dice values and meld options, buttons for each
     * dice, and checkboxes for melding. The dice buttons and meld checkboxes are added to
     * corresponding lists for further manipulation.
     *
     */
    public void displayDiceOnButtons() {
        // Get the current roll of dice values from the hand
        int[] currentRoll = hand.getValues();
        // Iterate through the dice buttons and set icons based on the current roll
        for (int i = 0; i < diceButtons.size(); i++) {
            diceButtons.get(i).setIcon(diceImages.getDieImage(currentRoll[i]));
        }
    }

    // Inside the genDiceControlPanel method
    private JPanel genDiceControlPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        newPanel.setLayout(new GridLayout(3, 7, 1, 1));
        JLabel diceLabel = new JLabel("Dice Vals:");
        JLabel meldBoxesLabel = new JLabel("Meld 'em:");

        newPanel.add(new Panel());  // Upper left corner is blank
        for (int index = 0; index < 6; index++) {
            JLabel colLabel = new JLabel(Character.toString('A' + index), SwingConstants.CENTER);
            newPanel.add(colLabel);
        }
        newPanel.add(diceLabel);

        for (int index = 0; index < 6; index++) {
            JButton diceStatusButton = new JButton();
            diceStatusButton.setPreferredSize(new Dimension(50, 50)); // Set preferred size
            diceButtons.add(diceStatusButton);
            newPanel.add(diceStatusButton);
        }

        newPanel.add(meldBoxesLabel);
        for (int index = 0; index < 6; index++) {
            JCheckBox meldCheckbox = new JCheckBox();
            meldCheckbox.setHorizontalAlignment(SwingConstants.CENTER);
            meldCheckbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    recalculateMeldScore(); // Recalculate meld score when a checkbox is clicked
                }
            });
            this.meldCheckboxes.add(meldCheckbox);
            newPanel.add(meldCheckbox);
        }

        // Call the method to display dice icons on buttons
        displayDiceOnButtons();

        return newPanel;
    }

    /**
     * Recalculates the score of the current meld based on selected dice.
     */
    private void recalculateMeldScore() {
        // Create a list to store the indices of selected checkboxes
        ArrayList<Integer> selectedIndices = new ArrayList<>();

        // Get the current roll of dice values from the hand
        int[] currentRoll = hand.getValues();
        int[] currentMeld = new int[6];

        // Loop through the meld checkboxes to check which dice are selected
        for (int i = 0; i < meldCheckboxes.size(); i++) {
            if (meldCheckboxes.get(i).isSelected()) {
                selectedIndices.add(i); // Add the index of the selected checkbox
            }
        }

        // Add the selected dice to the meld
        for (int index : selectedIndices) {
            // Add the dice value to the meld
            currentMeld[index] = currentRoll[index];
            meld.addToMeld(currentRoll[index]);
        }

        // Calculate the meld score using the provided scoring logic
        int meldScore = Meld.calculateMeldScore(currentMeld);

        // Update the meldScoreTextLabel with the calculated meld score
        meldScoreTextLabel.setText(Integer.toString(meldScore));
    }

    /**
     * Generates and returns a JPanel containing player information components.
     * <p>
     * This method creates a JPanel with a black border and a horizontal flow layout.
     * It includes components such as a JLabel for player name, a JTextField for entering
     * the player name, a JButton for rolling dice, and a debug label for dice information.
     * The player name text field, dice debug label, and roll button are added to the panel
     * with appropriate configurations.
     *
     * @return A JPanel containing components for player information.
     */
    private JPanel genPlayerInfoPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        newPanel.setLayout(new FlowLayout());

        JLabel playerNameLabel = new JLabel("Player name:");
        playerNameTextField = new JTextField(20);
        rollButton = new JButton("Roll Dice");
        rerollButton = new JButton("Reroll");

        diceDebugLabel = new JTextField(6);
        diceDebugLabel.setHorizontalAlignment(SwingConstants.CENTER);

        newPanel.add(playerNameLabel);
        newPanel.add(playerNameTextField);
        newPanel.add(rollButton);
        newPanel.add(rerollButton);
        newPanel.add(diceDebugLabel);

        // Add ActionListener to playerNameTextField
        playerNameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update player name when Enter is pressed in the text field
                String playerName = playerNameTextField.getText();
                System.out.println("Player Name set to: " + playerName);
            }
        });

        return newPanel;
    }

    /*
     *  This is a method to show you how you can set/read the visible values
     *   in the various text widgets.
     */
    private void putDemoDefaultValuesInGUI() {
        // Example setting of player name
        this.playerNameTextField.setText("Farkle Player");
    }

    private void addDemoButtonCallbackHandlers() {
        // Get the current roll of dice values from the hand
        int[] currentRoll = hand.getValues();
        this.rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("They clicked the roll button!");

                // Roll the dice when the "Roll Dice" button is pressed
                hand.roll();
                displayDiceOnButtons();

                // Convert the array of dice values to a string
                StringBuilder diceValuesString = new StringBuilder();
                for (int value : hand.getValues()) {
                    diceValuesString.append(value);
                }
                // Set the text of diceDebugLabel to the string representation of the dice values
                diceDebugLabel.setText(diceValuesString.toString());

                // Use MeldSuggester to find potential melds and calculate meld score
                int tempScore = Meld.calculateMeldScore(currentRoll);

                // Check for Farkle
                if (tempScore == 0) {
                    JOptionPane.showMessageDialog(mainWindowFrame, "Round Over! You got a Farkle.");
                }
            }
        });

        // Example of another button callback
        // Reads the dice checkboxes and counts how many are checked (selected)
        // Sets the meldScoreTextLabel to how many of the checkboxes are checked
        this.rerollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the values of checked checkboxes to 0
                for (int i = 0; i < meldCheckboxes.size(); i++) {
                    if (meldCheckboxes.get(i).isSelected()) {
                        hand.getValues()[i] = 0; // Set the corresponding dice value to 0
                    }
                }

                // Create a list to store the indices of selected checkboxes
                ArrayList<Integer> selectedIndices = new ArrayList<>();

                // Create a list to store indices of dice not in the meld
                ArrayList<Integer> unmeldedIndices = new ArrayList<>();

                // Get the current roll of dice values from the hand
                int[] currentRoll = hand.getValues();
                int[] currentMeld = new int[6];

                int meldScore = Meld.calculateMeldScore(currentMeld);
                roundScore += meldScore; // Add meld score to round score

                // Loop through the meld checkboxes to check which dice are selected
                for (int i = 0; i < meldCheckboxes.size(); i++) {
                    if (meldCheckboxes.get(i).isSelected()) {
                        selectedIndices.add(i); // Add the index of the selected checkbox
                    }
                }

                // Add the selected dice to the meld
                for (int index : selectedIndices) {
                    // Add the dice value to the meld
                    currentMeld[index] = currentRoll[index];
                    meld.addToMeld(currentRoll[index]);

                    // Disable the checkbox to prevent removal of dice from the meld
                    meldCheckboxes.get(index).setEnabled(false);
                }

                // Check each die if it's not in the meld, add its index to unmeldedIndices
                for (int i = 0; i < currentRoll.length; i++) {
                    if (!meld.isInMeld(currentRoll[i])) {
                        unmeldedIndices.add(i);
                    }
                }

                // Reroll unmelded dice and update the dice values
                for (int index : unmeldedIndices) {
                    Die die = new Die(); // Create a new Die object
                    die.roll(); // Roll the die
                    currentRoll[index] = die.getSideUp();
                }

                // Update the GUI with the new meld score and round score
                meldScoreTextLabel.setText(Integer.toString(meldScore));
                roundScoreTextLabel.setText(Integer.toString(roundScore));

                // Update the GUI with the new dice values
                //updateDiceButtons(currentRoll);
                displayDiceOnButtons();

                // Use MeldSuggester to find potential melds and calculate meld score
                int tempScore = Meld.calculateMeldScore(currentRoll);

                // Check for Farkle
                if (tempScore == 0) {
                    JOptionPane.showMessageDialog(mainWindowFrame, "Round Over! You got a Farkle.");
                }
            }
        });

        rerollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add the current meld score to the round score
                int meldScore = Integer.parseInt(meldScoreTextLabel.getText());
                roundScore += meldScore;

                // Update the round score in the GUI
                roundScoreTextLabel.setText(Integer.toString(roundScore));

                // Reset meld score label
                meldScoreTextLabel.setText("0");
            }
        });

        bankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add the current meld score to the round score
                int meldScore = Integer.parseInt(meldScoreTextLabel.getText());
                roundScore += meldScore;

                // Update the round score in the GUI
                roundScoreTextLabel.setText(Integer.toString(roundScore));

                // Reset meld score label
                meldScoreTextLabel.setText("0");

                // Add the current meld score to the round score
                meldScoreTextLabel.getText();
                totalScore += roundScore;

                // Update the round score in the GUI
                totalScoreTextLabel.setText(Integer.toString(totalScore));

                // Reset meld score label
                roundScoreTextLabel.setText("0");
            }
        });

        // Add action listener to the new round button
        newRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollNewRound(); // Call the method to roll a new set of dice
            }

            // Define a method to roll a new set of dice for a new round
            private void rollNewRound() {
                // Clear any existing melds and scores
                meldScoreTextLabel.setText("0");
                roundScoreTextLabel.setText("0");

                hand = new Hand(6);

                // Roll a new set of dice for the hand
                hand.roll();

                for (JCheckBox checkbox : meldCheckboxes) {
                    checkbox.setSelected(false);
                    checkbox.setEnabled(true);
                }

                // Update the GUI to display the new set of rolled dice
                displayDiceOnButtons();
            }
        });

        // Example of a checkbox handling events when checked/unchecked
        JCheckBox boxWithEvent = this.meldCheckboxes.get(1);
        boxWithEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boxWithEvent.isSelected()) {
                    System.out.println("Checkbox is checked");
                } else {
                    System.out.println("Checkbox is unchecked");
                }
            }
        });
    }

    /*
     *  Builds the GUI frontend and allows you to hook up the callbacks/data for game
     */
    void runGUI() {
        System.out.println("Starting GUI app");
        setupGUI();
        //genDiceButtons();
        // These methods are to show you how it works
        // Once you get started working, you can comment them out so they don't
        //  mess up your own code.
        putDemoDefaultValuesInGUI();
        addDemoButtonCallbackHandlers();

        // Right here is where you could methods to add your own callbacks
        // so that you can make the game actually work.

        // Run the main window - begins GUI activity
        mainWindowFrame.setVisible(true);
        System.out.println("Done in GUI app");
    }
}