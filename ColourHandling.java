import java.util.ArrayList;
import java.util.Arrays;

public class ColourHandling extends MastermindTask {
	// Function to find the closest colour match from a list of target colors using
	// Euclidean distance
	public static int[] findClosestColour(int[] rgb, ArrayList<int[]> targetColours, int tolerance) {
		// System.out.println("DEBUG: Finding closest colour for RGB: " +
		// Arrays.toString(rgb));
		// Check for invalid input - ensure RGB array is not null and has a length of 3,
		// and targetColours is not empty
		if (rgb == null || rgb.length != 3 || targetColours == null || targetColours.isEmpty()) {
			// System.out.println("DEBUG: Invalid input for findClosestColour.");
			return null;
		}

		int[] closestColour = null;
		double minDistance = Double.MAX_VALUE; // Initialize with the largest possible distance
		double maxDistance = 150.0; // Set a threshold to consider as a close match (adjust as needed)
		// Iterate over each target colour in the list
		for (int[] target : targetColours) {
			if (target == null || target.length != 3) {
				// System.out.println("for back end use: Invalid target colour in list.");
				continue; // Skip invalid target colours
			}

			// Calculate the Euclidean distance between the RGB values
			double distance = Mathematics.calculateEuclideanDistance(rgb, target);
			// System.out.println("for back end use: Euclidean distance for target " +
			// Arrays.toString(target) + ": " + distance);

			// If the distance is smaller than the minimum distance and within the
			// maxDistance, update the closest match
			if (distance < minDistance && distance <= maxDistance) {
				minDistance = distance;
				closestColour = target;
			}
		}
		// Output the closest colour found (if any)
		if (closestColour == null) {
			System.out.println("No close match found.");
		} else {
			System.out.println(
					"The closest colour I can match your image with this time is " + Arrays.toString(closestColour));
		}

		return closestColour; // Return the closest colour found
	}

	// Function to convert a list of colour names (e.g., "R", "G", "B") to their RGB
	// representations
	static ArrayList<int[]> mapToRGB(ArrayList<String> secretCode) {
		ArrayList<int[]> rgbCode = new ArrayList<>();
		// Iterate over each colour name in the secret code
		for (String colour : secretCode) {
			colour = colour.toUpperCase(); // Convert to uppercase for case-insensitive matching
			// Match each colour to its corresponding RGB value and add to the rgbCode list
			switch (colour) {
			case "R":
				rgbCode.add(new int[] { 255, 0, 0 }); // Red
				break;
			case "G":
				rgbCode.add(new int[] { 0, 255, 0 }); // Green
				break;
			case "B":
				rgbCode.add(new int[] { 0, 0, 255 }); // Blue
				break;
			case "Y":
				rgbCode.add(new int[] { 255, 255, 0 }); // Yellow
				break;
			case "O":
				rgbCode.add(new int[] { 255, 165, 0 }); // Orange
				break;
			case "P":
				rgbCode.add(new int[] { 255, 192, 203 }); // Pink
				break;
			default:
				System.out.println("Error: Invalid color in secret code - " + colour);
				break;
			}
		}

		return rgbCode;// Return the list of RGB values corresponding to the secret code colours
	}

	// Function to map a single RGB value to its corresponding colour name
	static String mapSingleRGBToColourName(int[] rgb) {
		// Validate the RGB array (it must have 3 elements)
		if (rgb == null || rgb.length != 3) {
			System.out.println("ERROR: Invalid RGB array.");
			return "?"; // Handle invalid RGB
		}
		// Compare the input RGB with predefined colour values and return the
		// corresponding colour name
		if (Arrays.equals(rgb, new int[] { 255, 0, 0 })) {
			return "R"; // Red
		} else if (Arrays.equals(rgb, new int[] { 0, 255, 0 })) {
			return "G"; // Green
		} else if (Arrays.equals(rgb, new int[] { 0, 0, 255 })) {
			return "B"; // Blue
		} else if (Arrays.equals(rgb, new int[] { 255, 255, 0 })) {
			return "Y"; // Yellow
		} else if (Arrays.equals(rgb, new int[] { 255, 165, 0 })) {
			return "O"; // Orange
		} else if (Arrays.equals(rgb, new int[] { 255, 192, 203 })) {
			return "P"; // Pink
		} else {
			return "?"; // Handle unknown colours
		}
	}

// Function to map a list of RGB values to their corresponding colour names
	static ArrayList<String> mapRGBToColourName(ArrayList<int[]> rgbList) {
		ArrayList<String> colourStrings = new ArrayList<>();

		if (rgbList == null || rgbList.isEmpty()) {
			System.out.println("ERROR: RGB list is null or empty.");
			return colourStrings; // Return empty list if invalid input
		}
		// For each RGB value, convert it to a colour name and add to the output list
		for (int[] rgb : rgbList) {
			colourStrings.add(mapSingleRGBToColourName(rgb));
		}

		return colourStrings; // Return the list of colour names
	}

	// Function to return a predefined list of target colours (used for matching)
	public static ArrayList<String> getTargetColours() {
		ArrayList<String> targetColours = new ArrayList<>();
		targetColours.add("R"); // Red
		targetColours.add("G"); // Green
		targetColours.add("B"); // Blue
		targetColours.add("Y"); // Yellow
		targetColours.add("O"); // Orange
		targetColours.add("P"); // Pink
		return targetColours; // Return the list of target colours
	}

	// Function to return the RGB value for a given colour name
	protected static int[] getColourRGB(String colour) {
		switch (colour) {
		case "R": // Red
			return new int[] { 255, 0, 0 }; // RGB for Red
		case "G": // Green
			return new int[] { 0, 255, 0 }; // RGB for Green
		case "B": // Blue
			return new int[] { 0, 0, 255 }; // RGB for Blue
		case "Y": // Yellow
			return new int[] { 255, 255, 0 }; // RGB for Yellow
		case "O": // Orange
			return new int[] { 255, 165, 0 }; // RGB for Orange
		case "P": // Purple
			return new int[] { 255, 192, 203 }; // RGB for Pink
		default:
			return null; // Return null if the colour code is invalid or not recognised
		}
	}

}
