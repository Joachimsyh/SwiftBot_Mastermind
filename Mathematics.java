import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import swiftbot.ImageSize;
import swiftbot.SwiftBotAPI;

public class Mathematics extends MastermindTask {
	// Capture RGB values of a colour using SwiftBot's camera
	static int[] captureRGBValues(SwiftBotAPI swiftbot) {
		// Generate a unique filename using a timestamp for the image.
		String timestamp = Long.toString(System.currentTimeMillis());
		// Filename for saving the image
		String fn = "User_colour_" + timestamp + ".jpg";

		// Use OtherFunc to check the distance from the ultrasound sensor
		double distanceToObject = BotFunc.ultraSound(); // Retrieves the distance from the ultrasound sensor
		// Flag to track whether the distance warning has been printed
		boolean printedTooCloseMessage = false;
		// Loop to ensure the card is within the valid distance range
		while (distanceToObject < 39.0 || distanceToObject > 43.0) {
			// Warn if the card is too close or too far, and prevent multiple prints
			if (distanceToObject < 39.0 && !printedTooCloseMessage) {
				System.out.println("The colour card is too close, please adjust the distance.");
				printedTooCloseMessage = true; // Prevent repeated message
			} else if (distanceToObject > 43.0 && !printedTooCloseMessage) {
				System.out.println("The colour card is not close enough, please adjust the distance.");
				printedTooCloseMessage = true; // Prevent repeated message
			}

			// Wait before checking the distance again
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Update the distance measurement
			distanceToObject = BotFunc.ultraSound();
			// Reset the flag when the card is within a valid distance range
			if (distanceToObject >= 20.0 && distanceToObject <= 25.0) {
				printedTooCloseMessage = false; // Reset flag if distance is valid
			}
		}

		// Once the distance is valid, prompt user to hold the card at required
		// distance.
		System.out.println("Card is at the required distance. Please hold the card there.");

		// Add a small delay (1-2 seconds) before starting the countdown
		try {
			Thread.sleep(1500); // 1.5 second delay before beginning the countdown
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Warn the user the process is beginning.
		System.out.println("Beginning to take picture...");

		// **10-second countdown before taking a picture**
		for (int i = 10; i > 0; i--) {
			System.out.println("Taking picture in " + i + " seconds...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Capture the image using the SwiftBot camera
		BufferedImage img = swiftBot.takeStill(ImageSize.SQUARE_480x480);
		if (img == null) {
			System.out.println("Error: No image captured! Check the camera.");
			return new int[0]; // // Return an empty array if the image capture fails
		}

		// Save the image to a file
		try {
			ImageIO.write(img, "jpg", new File(fn)); // Save image in JPG format
		} catch (IOException e) {
			System.err.println("Error: Failed to take or save image!");
			e.printStackTrace();
			return new int[0]; // Return an empty array if saving the image fails
		}

		// Read the saved image and extract RGB values
		try {
			img = ImageIO.read(new File(fn)); // Read the saved image from file
			if (img == null) {
				System.err.println("Error: Failed to read the image from file!");
				return new int[0];
			}

			// Extract RGB values from the image
			int width = img.getWidth();
			int height = img.getHeight();
			int redSum = 0, greenSum = 0, blueSum = 0, totalPixels = width * height;

			// Loop through all pixels and sum the RGB components
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int pixelColour = img.getRGB(x, y); // Get RGB value of the current pixel
					redSum += (pixelColour >> 16) & 0xFF; // Extract red component
					greenSum += (pixelColour >> 8) & 0xFF; // Extract green component
					blueSum += (pixelColour) & 0xFF; // Extract blue component
				}
			}

			// Calculate average RGB values
			int avgRed = redSum / totalPixels;
			int avgGreen = greenSum / totalPixels;
			int avgBlue = blueSum / totalPixels;
			System.out.println("Average RGB: (" + avgRed + ", " + avgGreen + ", " + avgBlue + ")");
			return new int[] { avgRed, avgGreen, avgBlue }; // Return the average RGB values
		} catch (IOException e) {
			System.err.println("Error: Failed to read the saved image!");
			e.printStackTrace();
			return new int[0]; // Return empty array if image reading fails
		}
	}

	// Calculate Euclidean distance between two RGB values
	public static double calculateEuclideanDistance(int[] rgb1, int[] rgb2) {
		// Ensure both arrays are RGB arrays of length 3
		if (rgb1.length != 3 || rgb2.length != 3) {
			System.out.println("ERROR: RGB arrays must have exactly 3 elements.");
			return 0; // Return early if the arrays do not represent RGB values
		}
		// Calculate the squared differences for each component
		int rDiff = rgb1[0] - rgb2[0];
		int gDiff = rgb1[1] - rgb2[1];
		int bDiff = rgb1[2] - rgb2[2];
		// Return the Euclidean distance between the two RGB values
		return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
	}

	// Check if two RGB values are within a specified tolerance
	public static boolean isColourInRange(int[] rgb1, int[] rgb2, int tolerance) {
		// Ensure both arrays are of length 3
		if (rgb1.length != 3 || rgb2.length != 3) {
			System.out.println("ERROR: RGB arrays must have exactly 3 elements.");
			return false; // Return false if the arrays are invalid
		}

		// Calculate the absolute differences for each RGB component
		int rDiff = Math.abs(rgb1[0] - rgb2[0]);
		int gDiff = Math.abs(rgb1[1] - rgb2[1]);
		int bDiff = Math.abs(rgb1[2] - rgb2[2]);

		// Check if the differences are within the tolerance, if the differences are all
		// within the tolerance, it's considered a match
		return rDiff <= tolerance && gDiff <= tolerance && bDiff <= tolerance;
	}

	// Convert Euclidean distance between RGB values into a percentage match
	public static double calculateMatchPercentage(int[] rgb1, int[] rgb2) {
		// Calculate the Euclidean distance between the two RGB values
		double distance = calculateEuclideanDistance(rgb1, rgb2);

		// Maximum possible RGB distance (when each RGB component differs by 255)
		double maxDistance = Math.sqrt(3 * Math.pow(255, 2));

		// Calculate the match percentage by subtracting the normalized distance from
		// 100
		double percentage = 100 - (distance / maxDistance) * 100;

		// Ensure the percentage is between 0 and 100
		return Math.max(0, Math.min(percentage, 100));
	}

}
