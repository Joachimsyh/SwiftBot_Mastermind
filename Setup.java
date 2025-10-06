import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Setup extends MastermindTask {
	// Method to generate a random code based on the number of colours provided
	public static ArrayList<String> RandomCode(int numColours) {
		ArrayList<String> generatedCode = new ArrayList<>(); // List to store colour names (e.g., "R", "G")
		Random rand = new Random(); // Random object for generating random indices

		// Get target colours from the method
		ArrayList<String> targetColours = ColourHandling.getTargetColours();

		// Ensure that RGB values and target colours were captured successfully
		if (numColours > targetColours.size()) {
			System.out.println("Error: Not enough unique colours to generate the code!");
			return generatedCode; // Return empty if not enough colours are available
		}

		// Copy the list to avoid modifying the original target colours list
		ArrayList<String> colours = new ArrayList<>(targetColours);

		// Keep adding random, non-repeating colours until we have the required number
		for (int i = 0; i < numColours; i++) {
			int randomIndex = rand.nextInt(colours.size()); // Pick a random index
			String randomColour = colours.get(randomIndex); // Get the random colour name
			generatedCode.add(randomColour); // Add to the generated code
			colours.remove(randomIndex); // Remove the selected colour to avoid duplicates
		}

		return generatedCode;
	}
	// Default mode setup: sets up a default game configuration with 4 colours and 6 attempts
	public static void DefaultModeSetup() {
		colours = 4;
		ArrayList<String> secretCode = RandomCode(colours);// Generate the secret code
		int initialAttempts = 6; // Set total amount of attempts for hint prompt
		int attempts = initialAttempts; //set the actual attempts for use
		BotFunc.typeWriterWithDelay(
				"Default mode setup complete! \n" + "Start guessing! \n" + "The secret code is " + secretCode);
		BotFunc.typeWriterWithDelay("-".repeat(30));
		// Start the game loop using the default configuration
		MastermindTask.GameLoop(secretCode, attempts, initialAttempts);

	}
	// Customized mode setup: allows the user to specify the number of colours and attempts
	public static void CustomizedModeSetup() {
		try (Scanner scanner = new Scanner(System.in)) {
			int initialAttempts = 0;
			// Ask for the number of colours (between 3 and 6)
			while (true) {
				BotFunc.typeWriterWithDelay(
						"Please choose the amount of colours you would like to guess (between 3 and 6).");

				if (scanner.hasNextInt()) {
					colours = scanner.nextInt(); // Read user input
					if (colours >= 3 && colours <= 6) {
						break; // exits loop if input is valid
					} else {
						System.out.println("Invalid amount, please try again.");
					}
				} else {
					System.out.println("Invalid input. Please enter a number.");
					scanner.next(); // Clear invalid input
				}
			}
			 // Ask for the number of maximum guesses (between 1 and 10)
			int attempts = 0;
			while (true) {
				BotFunc.typeWriterWithDelay("Please choose the number of maximum guesses (between 1 and 10).");

				if (scanner.hasNextInt()) {
					attempts = scanner.nextInt(); // Read user input
					if (attempts >= 1 && attempts <= 10) {
						break; // Valid input, exit loop
					} else {
						System.out.println("Invalid amount, please try again.");
					}
				} else {
					System.out.println("Invalid input. Please enter a number.");
					scanner.next(); // Clear invalid input
				}
			}
			initialAttempts = attempts;

			// Proceed to the game with the chosen settings
			BotFunc.typeWriterWithDelay("Customized mode setup complete!");
			BotFunc.typeWriterWithDelay("-".repeat(30));
			// Generate secret code with user-defined colours
			ArrayList<String> secretCode = RandomCode(colours);
			BotFunc.typeWriterWithDelay("The secret code is " + secretCode);
			BotFunc.typeWriterWithDelay("-".repeat(30));

			// Start the game loop with user-defined configuration
			MastermindTask.GameLoop(secretCode, attempts, initialAttempts);
		}
	}
	public static void PauseGame(ArrayList<String> secretCode, int attempts, int initialAttempts) {
		try (Scanner scanner = new Scanner(System.in)) {
			BotFunc.typeWriterWithDelay("Would you like to continue the game or would you like to end the game?"
					+ "\n" + "Press X to end the game" + "\n OR " + "\n press Y to continue the game.");
			BotFunc.end();
			// Keep the program running to detect button presses
			while (!quitGame && !continueGame) {
				try {
					Thread.sleep(500); // Prevents excessive CPU usage
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// After the game ends, prompt the player whether they want to continue or quit
				if (quitGame && !continueGame) {
					Setup.QuitGame();
					return; // Exit the method
				}

				if (continueGame && !quitGame) {
					Setup.ContinueGame(secretCode, attempts, initialAttempts);
					break; // Exit the EndGame loop after GameLoop starts
				}
			}
		}
	}
	// Display game over details, including round, player and bot scores, and total guesses
	public static void displayGameOverDetails() {
		BotFunc.typeWriterWithDelay("  ________                      ________                     \r\n"
				+ " /  _____/_____    _____   ____ \\_____  \\___  __ ___________ \r\n"
				+ "/   \\  ___\\__  \\  /     \\_/ __ \\ /   |   \\  \\/ // __ \\_  __ \\\r\n"
				+ "\\    \\_\\  \\/ __ \\|  Y Y  \\  ___//    |    \\   /\\  ___/|  | \\/\r\n"
				+ " \\______  (____  /__|_|  /\\___  >_______  /\\_/  \\___  >__|   \r\n"
				+ "        \\/     \\/      \\/     \\/        \\/          \\/       ");
		BotFunc.typeWriterWithDelay("You have lost.");
		BotFunc.typeWriterWithDelay("Round Details: " + roundNumber + "\r\n" + "Player Guess: " + playerGuess + "\r\n"
				+ "Player score vs computer score: " + PlayerScore + "-" + SwiftBotScore + "\r\n" + "Total Guesses: "
				+ totalGuesses);
		BotFunc.typeWriterWithDelay(
				"The details have been saved to a text file you can access through the SwiftBot. Thanks for playing!");
		System.exit(0); // Exit the program
	}

	public static void QuitGame() {
		gameOver = true; // Set the game over flag
		Setup.displayGameOverDetails(); // Show game over details
		System.exit(0); // Exit the program (double checker in case the program runs through to gameloop again)
	}
	 // Continue the game with a new secret code and updated round number
	public static void ContinueGame(ArrayList<String> secretCode, int attempts, int initialAttempts) {
		try (Scanner scanner = new Scanner(System.in)) {
			// Reset continueGame flag and display restart message
			continueGame = false; // Reset continueGame flag
			BotFunc.typeWriterWithDelay("Restarting the game...");

			// Generate a new secret code 
			secretCode = Setup.RandomCode(colours);
			BotFunc.typeWriterWithDelay("Welcome back to Mastermind. Are you ready to rumble?");
			// Increment round number after each round
			roundNumber++;
			// Countdown for 3 seconds before continuing
			for (int i = 3; i > 0; i--) {
				System.out.println("Continuing the game in " + i + " seconds...");
				try {
					Thread.sleep(1000); // Wait for 1 second before continuing
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Offer a hint if half of the initial attempts are left
			if (attempts == initialAttempts / 2 && initialAttempts > 1) {
				String response = "";
				while (response.isBlank()) {
					System.out.println("Would you like a hint of the code? (Y/N)");
					response = scanner.nextLine().trim();

					if (response.equalsIgnoreCase("Y")) {
						// Provide a hint if user requests it
						System.out.println("Please pay attention to the SwiftBot.");
						BotFunc.flashLightsForHint(swiftBot, secretCode);
						break;
					} else if (response.equalsIgnoreCase("N")) {
						// No hint if user declines
						System.out.println("No hint provided.");
						break;
					} else {
						System.out.println("Invalid input. Please type 'Y' or 'N'.");
						response = ""; // Clear invalid input
					}
				}
				GameLoop(secretCode, attempts, initialAttempts); // Restart game loop
			}
		}
	}
}
