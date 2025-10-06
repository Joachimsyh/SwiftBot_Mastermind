import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import swiftbot.Button;
import swiftbot.SwiftBotAPI;

public class BotFunc extends MastermindTask {
	public static final String ANSI_GREEN = "\u001B[32m";
	protected static void run() {

		// Enabling buttons for game control
		swiftBot.enableButton(Button.A, () -> {
			System.out.println("Default Mode Chosen.");
			// This sets the A button on the SwiftBot for use
			runDefaultMode = true;
			swiftBot.disableButton(Button.A);
		});

		swiftBot.enableButton(Button.B, () -> {
			System.out.println("Customized Mode Chosen.");
			// This sets the B button on the SwiftBot for use
			Setup.CustomizedModeSetup();
			swiftBot.disableButton(Button.B);
		});
	}

	protected static void end() {
		 
		

		// Button for continuing the game
		swiftBot.disableButton(Button.Y);
		swiftBot.enableButton(Button.Y, () -> {
			continueGame = true;
			quitGame = false;
			swiftBot.disableButton(Button.Y);
		});

		// Button for quitting the game
		swiftBot.disableButton(Button.X);
		swiftBot.enableButton(Button.X, () -> {
			quitGame = true;
			continueGame = false;
			swiftBot.disableButton(Button.X);
		});
	}
	// Flash lights to hint at the colours in the secret code
	static void flashLightsForHint(SwiftBotAPI swiftBot, ArrayList<String> secretCode) {
		System.out.println("Flashing hint colors...");

		// Flash lights for the colours in the secret code as a hint
		for (int i = 0; i < secretCode.size(); i++) {
			String colour = secretCode.get(i); // get the value of the index from the secret code to form a string, in
												// this case would be each individual colour in the code.
			int[] rgbColour = ColourHandling.getColourRGB(colour); // Use the getColourRGB method to get the RGB values
																	// for the colour
			flashLights(swiftBot, rgbColour); // Flash corresponding LED colour for each colour in the secret code
		}
	}

	// Flashes lights with the specified RGB colour
	public static void flashLights(SwiftBotAPI swiftBot, int[] colour) {
		try {
			swiftBot.fillUnderlights(colour); // Flash the lights with the given colour
			Thread.sleep(2000); // Wait for 2 seconds before turning off the lights
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(5); // Exit the program if an error occurs while flashing the lights
		} finally {
			swiftBot.disableUnderlights(); // Disable the lights after flashing regardless of success or failure
		}
	}

	// Checks the distance to an object using ultrasound
	public static double ultraSound() {
		double distanceToObject = 0;
		try {
			// Get the distance using the SwiftBot ultrasound sensor
			distanceToObject = swiftBot.useUltrasound();
		} catch (Exception e) {
			// Handle any exceptions thrown during distance measurement
			e.printStackTrace();
		}
		return distanceToObject; // Return the distance measurement
	}

	// Logs game details to a file
	public static void logGameDetails(int roundNumber, String playerGuess, int totalGuesses, int attempts, ArrayList<String> secretCode) {
		try (PrintWriter writer = new PrintWriter(new FileWriter("SaveFile.txt", true))) {
			// Append round details to the "SaveFile.txt"
			writer.println("Round Number: " + roundNumber);
			writer.println("Computer's code: " + secretCode);
			writer.println("Player Input: " + playerGuess);
			writer.println("Player Score: " + PlayerScore);
			writer.println("Bot Score: " + SwiftBotScore);
			writer.println("Total Guesses: " + totalGuesses);
			writer.println("Guesses left: " + attempts);
			writer.println("------------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace(); // handle any I/O exceptions during the logging process
		}
	}

	// Typewriter effect for printing text with a delay between characters
	public static void typeWriterWithDelay(String text) {
		for (char c : text.toCharArray()) {
			System.out.print(ANSI_GREEN + c); // Print each character with a green colour
			try {
				Thread.sleep(10); // Introduce a 10ms delay between characters to create a typewriter effect
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(); // Move to the next line
		try {
			// Introduce a 2-second delay after the entire line is printed for a cinematic
			// effect
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Handle interruption if the thread is interrupted during the delay
			Thread.currentThread().interrupt();
		}
	}
}
