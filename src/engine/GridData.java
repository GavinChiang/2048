package engine;

import java.util.Random;

public class GridData implements Cloneable {
	private int emptyBlock = 0;
	private int score = 0;
	public static final int GRID_LENGTH = 4;
	public static final int GRID_SIZE = GRID_LENGTH * GRID_LENGTH;
	public static final int TILE_GEN_RATIO = 9;
	public static final int INITIAL_TILE_NUM = 2;
	public static final int NORMAL_TILE = 2;
	public static final int WILD_CARD_TILE = 4;
	public static final int DIR_NUM = 4;
	public static final int EMPTY = 0;
	private int grid[] = new int[GRID_SIZE];
	private Random random;

	public enum DIR {
		LEFT, DOWN, RIGHT, UP, INVALID
	}

	public GridData(Random random) {
		this.random = random;
		clear();

	}

	
	public int getValue(int index) {
		if (index < 0 || index > GRID_SIZE - 1) {
			assert (false);
			return -1;
		}
		return grid[index];
	}

	
	public int getValue(int row, int col) {
		int index = row * GRID_LENGTH + col;
		return getValue(index);
	}

	
	public void clear() {
		for (int i = 0; i < GRID_SIZE; i++) {
			grid[i] = 0;
		}
		emptyBlock = GRID_SIZE;
		score = 0;
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		GridData gridData = (GridData) super.clone();
		gridData.grid = this.grid.clone();
		return gridData;
	}

	
	public void setBlock(int index, int val) {
		if (index < 0 || index > GRID_SIZE - 1) {
			assert (false);
			return;
		}
		if (grid[index] == 0 && val != 0) {
			emptyBlock--;
		} else if (grid[index] != 0 && val == 0) {
			emptyBlock++;
		}
		grid[index] = val;
	}

	
	public int getEmptyBlkNo() {
		return emptyBlock;
	}

	
	public int getScore() {
		return score;
	}

	
	private int getEntry(int row, int col) {
		return grid[row * GRID_LENGTH + col];
	}

	private void setEntry(int row, int col, int value) {
		grid[row * GRID_LENGTH + col] = value;
	}

	
	private int getFlipEntry(int row, int col) {
		return grid[row * GRID_LENGTH + (GRID_LENGTH - 1 - col)];
	}

	private void setFlipEntry(int row, int col, int value) {
		grid[row * GRID_LENGTH + (GRID_LENGTH - 1 - col)] = value;
	}

	
	private int getTransEntry(int row, int col) {
		return grid[col * GRID_LENGTH + row];
	}

	private void setTransEntry(int row, int col, int value) {
		grid[col * GRID_LENGTH + row] = value;
	}

	
	private int getFlipTransEntry(int row, int col) {
		return grid[(GRID_LENGTH - 1 - col) * GRID_LENGTH + row];
	}

	private void setFlipTransEntry(int row, int col, int value) {
		grid[(GRID_LENGTH - 1 - col) * GRID_LENGTH + row] = value;
	}

	private int getDirEntry(DIR dir, int row, int col) {
		int value = 0;
		if (dir == DIR.LEFT)
			value = getEntry(row, col);
		else if (dir == DIR.DOWN)
			value = getFlipTransEntry(row, col);
		else if (dir == DIR.RIGHT)
			value = getFlipEntry(row, col);
		else
			value = getTransEntry(row, col);
		return value;
	}

	private void setDirEntry(DIR dir, int row, int col, int value) {
		if (dir == DIR.LEFT)
			setEntry(row, col, value);
		else if (dir == DIR.DOWN)
			setFlipTransEntry(row, col, value);
		else if (dir == DIR.RIGHT)
			setFlipEntry(row, col, value);
		else
			setTransEntry(row, col, value);

	}

	
	public boolean shift(DIR dir) {
		int nTile;
		int lastMerge;
		int shiftBuff[] = new int[GRID_LENGTH];
		boolean isShifted = false;

		for (int i = 0; i < GRID_LENGTH; i++) {
			nTile = lastMerge = 0;
			for (int k = 0; k < GRID_LENGTH; k++) {
				shiftBuff[k] = EMPTY;
			}
			for (int j = 0; j < GRID_LENGTH; j++) {
				if (getDirEntry(dir, i, j) == EMPTY)
					continue;
				if (nTile > lastMerge
						&& (getDirEntry(dir, i, j) == shiftBuff[nTile - 1])) {
					shiftBuff[nTile - 1] *= 4;
					lastMerge = nTile;
					isShifted = true;
					emptyBlock++;
					score += shiftBuff[nTile - 1];
				} else {
					if (nTile != j)
						isShifted = true;
					shiftBuff[nTile++] = getDirEntry(dir, i, j);
				}
			}
			for (int j = 0; j < GRID_LENGTH; j++)
				setDirEntry(dir, i, j, shiftBuff[j]);
		}
		return isShifted;

	}

	
	public int getMaxTile() {
		int max = 0;
		for (int i = 0; i < GRID_SIZE; i++) {
			if (max < grid[i])
				max = grid[i];
		}
		return max;
	}

	public void print() {
		StringBuffer tilePad = new StringBuffer();
		for (int i = 0; i < GRID_LENGTH; i++) {
			for (int j = 0; j < GRID_LENGTH; j++) {
				tilePad.append(grid[i * GRID_LENGTH + j] + "    ");
			}
			tilePad.append("\n\n");
		}

		System.out.println(tilePad.toString());
	}

	public int getTileByXY(int x, int y) {
		int index = getIndex(x, y);
		return grid[index];

	}

	public int getIndex(int row, int column) {
		return row * GRID_LENGTH + column;
	}

	public boolean isGameOver() {
		if (emptyBlock > 0) {
			return false;
		}
		for (int i = 0; i < GRID_LENGTH; i++) {
			for (int j = 0; j < GRID_LENGTH; j++) {
				if ((i < GRID_LENGTH - 1)
						&& grid[getIndex(i, j)] == grid[getIndex(i + 1, j)])
					return false;
				if ((j < GRID_LENGTH - 1)
						&& grid[getIndex(i, j)] == grid[getIndex(i, j + 1)])
					return false;
			}
		}
		return true;
	}

	//
	public void generateNewTile() {
		int randBlk = getRandInt(emptyBlock) + 1;
		for (int i = 0; i < GRID_SIZE; i++) {
			if (grid[i] == 0)
				randBlk--;

			if (randBlk == 0) {
				if ((getRandInt(TILE_GEN_RATIO + 1) +1)> TILE_GEN_RATIO) {
					grid[i] = WILD_CARD_TILE;
				} else {
					grid[i] = NORMAL_TILE;
				}
				emptyBlock--;
				break;
			}
		}
	}

	//
	private int getRandInt(int number) {
		return random.nextInt(number);
	}


}
