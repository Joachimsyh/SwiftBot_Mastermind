# SwiftBot_Mastermind

The project is to make the SwiftBot run a game on the command prompt window.

The way the game works is as follows:

The Swiftbot welcomes you to the game, and prompts you to choose either customized mode or default mode as you respond with buttons that are on the SwiftBot.

With default mode you have 4 lives and 4 colours to guess.

With Customized mode you are prompted to enter a number of lives (1-10) as well as a number of colours (3-6).

The SwiftBot then starts the game, creating an array of 3-6 letters that correspond to their respective colours, R for Red, Y for Yellow, B for Blue, G for Green, P for Pink, O for orange. 
The colours are not repeated.

The player's job is to utilize the SwiftBot's camera function and scan the colour cards that are provided to match the code provided.

The SwiftBot then gives feedback in terms of + signs and - signs. (+ for correct colours in right spots, - for correct colours in wrong spots, and an empty space for colours not in the code.)

If you win against the SwiftBot, the SwiftBot flashes Green and you win 1 point; if you lose against the SwiftBot, it will flash Red and you lose a life, with the SwiftBot also gaining one point.

If the lives are half of the value of initial attempts, you get prompted for an optional hint. If you agree, the SwiftBot will flash according to the colours of the code generated.

Each round ends as soon as the player or the SwiftBot gains a point. You then get asked if you want to continue the game, which then you reply by pressing the Y button for Yes or N button for no.

If you respond with Yes, you go to the next round. If you respond with No, the game will be over.

The feedback of the game details and score will be saved in a text file whenever each round ends automatically.
