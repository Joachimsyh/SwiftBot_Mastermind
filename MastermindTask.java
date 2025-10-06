import java.util.ArrayList; 
import java.util.Scanner;
import swiftbot.SwiftBotAPI;

public class MastermindTask {

	protected static SwiftBotAPI swiftBot;
	protected static int PlayerScore = 0; // the method can allow the variables to be called throughout the code.
	protected static int SwiftBotScore = 0;
	protected static int roundNumber = 1;
	protected static int totalGuesses = 0;
	protected static boolean continueGame = false;
	protected static boolean quitGame = false;
	protected static boolean gameOver = false;
	protected static boolean runDefaultMode = false;
	public static String playerGuess = "";
	protected static int colours = 0;
	protected static ArrayList<String> secretCode = new ArrayList<>();
	static {
		try {
			// Get the singleton instance of SwiftBotAPI
			swiftBot = SwiftbotAPIInstance.getInstance();
			if (swiftBot != null) {
				System.out.println("Successfully retrieved the SwiftBotAPI instance.");
			} else {
				System.out.println("Failed to retrieve the SwiftBotAPI instance.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(5); // Terminate if the instance cannot be initialized
		}
	}
	BotFunc bridge = new BotFunc();
	Mathematics maths = new Mathematics();
	Setup modes = new Setup();
	ColourHandling Colour = new ColourHandling();

	public static void main(String[] args) {

		// Continue with the rest of your logic...

		// get SwiftBot version
		String s = swiftBot.getVersion();

		// The print statement is an introduction page to the game.
		BotFunc.typeWriterWithDelay("Welcome to" + "\n"
				+ "   _____                   __                  _____  .__            .___\r\n"
				+ "  /     \\ _____    _______/  |_  ___________  /     \\ |__| ____    __| _/\r\n"
				+ " /  \\ /  \\\\__  \\  /  ___/\\   __\\/ __ \\_  __ \\/  \\ /  \\|  |/    \\  / __ | \r\n"
				+ "/    Y    \\/ __ \\_\\___ \\  |  | \\  ___/|  | \\/    Y    \\  |   |  \\/ /_/ | \r\n"
				+ "\\____|__  (____  /____  > |__|  \\___  >__|  \\____|__  /__|___|  /\\____ | \r\n"
				+ "        \\/     \\/     \\/            \\/              \\/        \\/      \\/ \r\n"
				+ "Powered by: SwiftBot - API version " + s + "\n"
				+ "Your objective is to match the colours stated by the SwiftBot using the provided physical colour cards and the camera of the SwiftBot, and try to win against the SwiftBot!"
				+ "\n" + "If you guess everything right, the bot will flash green lights and you will earn a point!"
				+ "\n"
				+ "If even one of your guesses are wrong, the bot will flash red lights and you will get to try again, until you ran out of guess attempts."
				+ "\n" + "But don't worry! The SwiftBot is quite generous, and will give you feedback on your input!"
				+ "\n"
				+ "There is a catch though, you won't know if which ones you got right and which ones you got wrong. You will only know if you have a correct colour in the correct position, which will be indicated with a + sign, and if you show a colour that's in the code but not in the right position, a - sign will be indicated instead."
				+ "\n"
				+ "When you win a round, you can choose whether to call it a day and end the game there, or continue playing to your heart's content!"
				+ "\n"
				+ "When you end the game, there will be a game log saved in the SwiftBot called 'saveFile' for you to check your record for future references."
				+ "\n"
				+ "You can choose between default mode and custom mode by pressing the two buttons on the SwiftBot.\r\n"
				+ "A for Default mode;\r\n" + "B for customized mode." + "\n"
				+ "For Customized mode, you can choose from 3 to 6 colours to guess from. You can also set the maximum amount of guesses as well!"
				+ "\n"
				+ "Please hold the cards at the right angle and at the right distance. The SwiftBot will tell you if you're not close enough to the camera."
				+ "\n" + "Have Fun!" + "\n" + "Please choose a mode by pressing button A or button B on the SwiftBot.");
		BotFunc.run();
		while (true) {
		    if (runDefaultMode) {
		        Setup.DefaultModeSetup(); // ← now runs on the main thread
		        runDefaultMode = false; // reset flag so it doesn’t repeat
		    }

		    try {
		        Thread.sleep(500); // Prevents excessive CPU usage
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}
	}
		// Keep the program running to detect button presses
		

	

	protected static void GameLoop(ArrayList<String> secretCode, int attempts, int initialAttempts) {
		
		try (Scanner scanner = new Scanner(System.in)) {
			ArrayList<String> userGuesses = new ArrayList<>();
			ArrayList<int[]> userGuessRGB = new ArrayList<>(); // To store RGB values

			// Convert secret code to RGB once at the start
			// This maps the secret code colours to RGB values, so they can be compared
			// later with user guesses
			ArrayList<int[]> secretCodeRGB = ColourHandling.mapToRGB(secretCode);

			while (attempts > 0) {
				// clears the user inputs so the Bot doesn't use past inputs.
				userGuesses.clear();
				userGuessRGB.clear();
				BotFunc.typeWriterWithDelay("Please hold a colour card at a suitable distance (around 40 cm) in front of the camera. \r\n The SwiftBot will guide you to the right distance. ");

				// Get target colours and their RGB equivalents
				// This retrieves the list of target colours for the game and their
				// corresponding RGB values
				ArrayList<String> targetColourStrings = ColourHandling.getTargetColours();
				ArrayList<int[]> targetColours = new ArrayList<>();

				try {
					// 3 second delay to allow time for the user to place a colour card
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();// Handle interruptions during sleep
				}
				// Populate the targetColours array with RGB values for each colour
				for (String colour : targetColourStrings) {
					targetColours.add(ColourHandling.getColourRGB(colour));
				}

				if (targetColours.isEmpty()) {
					BotFunc.typeWriterWithDelay("No target colours found! Check the database.");
					continue;
				}
				for (int i = 0; i < secretCode.size(); i++) {
					int[] rgbValues = Mathematics.captureRGBValues(swiftBot);

					if (rgbValues.length == 0) {
						BotFunc.typeWriterWithDelay("No RGB values detected! Retrying...");
						continue;
					}
					int tolerance = 7;
					// Find the closest match using the backup function
					int[] closestColour = ColourHandling.findClosestColour(rgbValues, targetColours, tolerance);

					if (closestColour == null) {
						BotFunc.typeWriterWithDelay("No close match. Try again.");
						i--; // Retry this guess if no matching colour is found
						continue;
					}
					// Convert matched RGB values to a colour name for matching.
					String matchedColourName = ColourHandling.mapSingleRGBToColourName(closestColour);

					if (matchedColourName.isEmpty()) {
						BotFunc.typeWriterWithDelay("Unknown color detected. Please try again.");
						i--;// Retry this guess if an unknown colour is detected
						continue;
					}

					BotFunc.typeWriterWithDelay("Detected Colour: " + matchedColourName);
					userGuesses.add(matchedColourName); // Store the user's guess
					userGuessRGB.add(closestColour); // Store RGB values as well
				}

				if (userGuesses.size() == secretCode.size()) {
					// Convert user guess RGB back to string before calling feedback
					ArrayList<String> userGuessStrings = ColourHandling.mapRGBToColourName(userGuessRGB);
					String playerGuess = String.join(", ", userGuessStrings);

					// Call WinLossCheck with RGB arrays
					boolean isCorrect = WinLossCheck(userGuessRGB, secretCodeRGB, attempts);

					if (isCorrect) {
						System.out.println("Congratulations! You have guessed the correct code.");
						PlayerScore++;
						// Log the round and the player's guess
						BotFunc.logGameDetails(roundNumber, playerGuess, totalGuesses, attempts, secretCode);
						Setup.PauseGame(secretCode, attempts, initialAttempts); // Pause the game after a win
						break;// Exit the loop if the player wins
					}
				} else {
					System.out.println("Invalid guesses detected. Please try again.");
				}
				// Increment the total guesses after each guess
				totalGuesses++;
				// Decrement attempts
				attempts--;
				System.out.println("Incorrect guess. Remaining guesses: " + attempts);

				// Log the round and the player's guess
				BotFunc.logGameDetails(roundNumber, playerGuess, totalGuesses, attempts, secretCode);
				if (attempts > 0 && !gameOver) {
					// Increment the SwiftBotScore after every round
					SwiftBotScore++;
					Setup.PauseGame(secretCode, attempts, initialAttempts);
				} else if (attempts == 0 || gameOver) {
					BotFunc.typeWriterWithDelay("The secret code was :" + secretCode);
					Setup.QuitGame(); // Quit the game if attempts are exhausted or if gameOver flag is true
					return;
				}
			}
		}
	}

	public static ArrayList<String> feedBack(ArrayList<String> userGuesses, ArrayList<String> secretCode) {
		ArrayList<String> feedback = new ArrayList<>();
		// Copy the secretCode list so that we can mark used colours without modifying
		// the original list.
		ArrayList<String> secretCodeCopy = new ArrayList<>(secretCode);

		// Check exact matches first ("+")
		for (int i = 0; i < userGuesses.size(); i++) {
			if (userGuesses.get(i).equals(secretCodeCopy.get(i))) {
				feedback.add("+");// Add a "+" for exact match
				secretCodeCopy.set(i, null); // Mark the position as used
			}
		}

		// Check for correct colour but wrong position ("-")
		for (int i = 0; i < userGuesses.size(); i++) {
			if (feedback.size() > i && feedback.get(i).equals("+")) {
				continue; // Skip already matched positions
			}
			if (secretCodeCopy.contains(userGuesses.get(i))) {
				feedback.add("-");// Add a "-" for correct colour but wrong position
				// Mark this colour in the secretCode as used to avoid double-counting
				secretCodeCopy.set(secretCodeCopy.indexOf(userGuesses.get(i)), null); // Mark as used
			}
		}

		// If feedback size is less than the user guesses size, fill the remaining with
		// empty spaces
		while (feedback.size() < userGuesses.size()) {
			feedback.add(" ");
		}

		return feedback;
	}

	public static boolean WinLossCheck(ArrayList<int[]> userGuess, ArrayList<int[]> secretCode, int attempts) {
		// Convert RGB values of user guesses and secret code to colour names for easier
		// comparison
		ArrayList<String> userGuessStrings = ColourHandling.mapRGBToColourName(userGuess);
		ArrayList<String> secretCodeStrings = ColourHandling.mapRGBToColourName(secretCode);

		// Get feedback on the guesses compared to the secret code
		ArrayList<String> feedback = feedBack(userGuessStrings, secretCodeStrings);
		// Join feedback into a single string for logging purposes
		String feedbackString = String.join("", feedback);
		// If all feedback elements are "+" (exact matches), the user has won the round
		if (feedback.stream().allMatch(f -> f.equals("+"))) {
			BotFunc.typeWriterWithDelay("++++");
			BotFunc.typeWriterWithDelay("Congratulations! You won the round!");
			// Flash green lights to indicate success
			BotFunc.flashLights(swiftBot, new int[] { 0, 255, 0 });
			PlayerScore++;
			BotFunc.typeWriterWithDelay("The score is now " + PlayerScore + " - " + SwiftBotScore);
			return true; // Return true to indicate the player won
		} else {
			System.out.println("Feedback: " + feedbackString);
			// Flash red lights to indicate failure
			BotFunc.flashLights(swiftBot, new int[] { 255, 0, 0 });
			
			SwiftBotScore++;
			return false; // Return false to indicate the player did not win
		}
	}
}
