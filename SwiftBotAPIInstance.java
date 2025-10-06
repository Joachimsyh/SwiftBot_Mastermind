import swiftbot.SwiftBotAPI;

public class SwiftbotAPIInstance {

	// Declare a private static instance of SwiftBotAPI
	private static SwiftBotAPI swiftBotAPI;

	// Private constructor to prevent instantiation
	private SwiftbotAPIInstance() {
	}

	// Public static method to get the singleton instance of SwiftBotAPI
	public static SwiftBotAPI getInstance() {
		if (swiftBotAPI == null) {
			// Synchronize the block to make the method thread-safe (only one thread can
			// initialize the instance at a time
			synchronized (SwiftbotAPIInstance.class) {
				// Double-check to avoid creating multiple instances in case of a race condition
				if (swiftBotAPI == null) {
					try {
						// Initialize the SwiftBotAPI instance
						swiftBotAPI = new SwiftBotAPI();
						System.out.println("Successfully created the SwiftBotAPI instance.");
					} catch (Exception e) { // Handle any exceptions that occur during initialization
						System.err.println("Error initializing SwiftBotAPI: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		// Return the singleton instance of SwiftBotAPI
		return swiftBotAPI;
	}
}
