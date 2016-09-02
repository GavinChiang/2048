package entry;

import control.BotController;
import control.GameController;
import engine.Game;
import engine.GridData.DIR;

public class MainEntry {

	public void playNRound(Game myGame, int rounds,
			GameController gameController) {
		myGame.printGrid();
		boolean isGameOver;
		DIR dir;
		myGame.printGrid();
		System.out.println("  Round:    " + 1 + "      ");
		System.out.println("  Score:    " + myGame.getScore() + "      ");
		System.out.println("  Max Score: " + myGame.getMaxScore()
				+ "      \n\n");
		for (int i = 0; i < rounds; i++) {
			isGameOver = false;
			while (!isGameOver) {
				while ((dir = gameController.getDirection()) == DIR.INVALID)
					;
				myGame.insertDirection(dir);
				isGameOver = myGame.isGameOver();
				myGame.printGrid();
				System.out.println("  Round:    " + i + 1 + "      ");
				System.out.println("  Score:    " + myGame.getScore()
						+ "      ");
				System.out.println("  Max Score: " + myGame.getMaxScore()
						+ "      \n\n");

			}
			if (i < rounds - 1)
				myGame.reset();
		}
	}

	
	public static void main(String[] args) {
		MainEntry entry = new MainEntry();
		Game myGame = new Game();
		// GameController gameController = new WindowsConsoleController();
		 //GameController gameController = new RandomController(myGame);
		GameController gameController = new BotController(myGame);
		entry.playNRound(myGame, 100, gameController);
		myGame.endGame();
	}

}
