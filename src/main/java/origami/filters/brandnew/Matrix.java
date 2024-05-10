package origami.filters.brandnew;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.Random;

import static org.opencv.imgproc.Imgproc.*;

public class Matrix implements Filter {

    // Random object for digital rain effect
    Random random = new Random();

    // Digital rain effect settings
    public int fontSize = 15;
    Scalar color = new Scalar(0, 255, 0); // Green color for the digital rain
    public int numStreams = 30; // Number of streams
    public int streamLength = 20; // Length of each stream

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getNumStreams() {
        return numStreams;
    }

    public void setNumStreams(int numStreams) {
        this.numStreams = numStreams;
    }

    public int getStreamLength() {
        return streamLength;
    }

    public void setStreamLength(int streamLength) {
        this.streamLength = streamLength;
    }

    public String japaneseCharacters = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん"
            + "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン";

    public String getJapaneseCharacters() {
        return japaneseCharacters;
    }

    public void setJapaneseCharacters(String japaneseCharacters) {
        this.japaneseCharacters = japaneseCharacters;
    }

    @Override
    public Mat apply(Mat mat) {
        int height = mat.height();
        int width = mat.width();

        // Create a black image
        Mat matrixWallpaper = new Mat(height, width, CvType.CV_8UC3, new Scalar(0, 0, 0));

        // Array to store the y positions of characters
        int[] yPositions = new int[numStreams];
        for (int i = 0; i < numStreams; i++) {
            // Randomly initialize y positions
            yPositions[i] = random.nextInt(height);
        }

        // Clear the image
        matrixWallpaper.setTo(new Scalar(0, 0, 0));

        // Draw characters
        for (int i = 0; i < numStreams; i++) {
            int x = random.nextInt(width);
            int y = yPositions[i];

            // Generate random characters
//            char randomChar = (char) (random.nextInt(95) + 32); // ASCII characters from 32 to 126

            // Generate random Japanese character
//            char randomChar = (char) (random.nextInt(japaneseCharEnd - japaneseCharStart + 1) + japaneseCharStart);
            char randomChar = japaneseCharacters.charAt(random.nextInt(japaneseCharacters.length()));

            // Draw character on the image
//            System.out.println(randomChar);
            putText(matrixWallpaper, String.valueOf(randomChar),
                    new org.opencv.core.Point(x, y),
                    FONT_HERSHEY_SIMPLEX, fontSize, color);

            // Update position for the next frame
            y += fontSize;
            if (y > height)
                y = -fontSize; // Start from the top again

            yPositions[i] = y;
        }

        return matrixWallpaper;
    }

}
