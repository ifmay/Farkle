package edu.gonzaga.Farkle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.Collections;

public class DiceImages {
    static ArrayList<ImageIcon> images;

    /**
     * Loads dice images from the specified path.
     *
     * @param imagesPath The path to the directory containing the dice image files.
     * @throws IOException If there is an error reading the image files.
     */
    public static void loadImages(String imagesPath) {
        BufferedImage currPicture;
        images.add(null);   // Slot 0 is kept empty (no blank die?)
        for (int i = 1; i < 7; i++) {
            try {
                String filename = imagesPath + "/D6-0" + i + ".png";
                // This shows the current working directory for your running program
                //System.out.println("Working Directory = " + System.getProperty("user.dir"));
                System.out.println("Loading image: " + filename);
                currPicture = ImageIO.read(new File(filename));
                Image dimg = currPicture.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                ImageIcon scaledImage = new ImageIcon(dimg);
                images.add(scaledImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new DiceImages object that loads dice images from the specified path.
     * This constructor calls the `loadImages` method to initialize the `images` list with dice images.
     *
     * @param imagesPath The path to the directory containing the dice image files.
     */
    public DiceImages(String imagesPath) {
        images = new ArrayList<>(12);
        loadImages(imagesPath);
    }

    /**
     * Gets the ImageIcon for a specific dice face value.
     *
     * @param faceValue The value of the dice face (1-6).
     * @return ImageIcon The ImageIcon representing the specified dice face, or null if invalid faceValue.
     */
    public ImageIcon getDieImage(int faceValue) {
        if (faceValue >= 1 && faceValue <= 6) {
            return images.get(faceValue);
        } else {
            // Return null or handle invalid faceValue as per your requirement
            return null;
        }
    }
}