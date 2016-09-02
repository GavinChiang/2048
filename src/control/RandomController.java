package control;

import java.util.Random;

import engine.GridData.DIR;

public class RandomController implements GameController {
	private Random rand;

	public RandomController() {
		rand = new Random();
	}

	@Override
	public DIR getDirection() {
		DIR dir = DIR.INVALID;
		switch (rand.nextInt(4)) {
		case 0:
			dir = DIR.LEFT;
			break;
		case 1:
			dir = DIR.RIGHT;
			break;
		case 2:
			dir = DIR.UP;
			break;
		case 3:
			dir = DIR.DOWN;
			break;
		default:
			break;
		}
		return dir;
	}

}
