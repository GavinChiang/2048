package engine;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.Random;

import org.fusesource.jansi.internal.Kernel32.INPUT_RECORD;
import org.fusesource.jansi.internal.Kernel32.KEY_EVENT_RECORD;
import org.fusesource.jansi.internal.WindowsSupport;

import control.GameController;

import engine.GridData.DIR;


public class Game {
	public static final int STAGE_NUM = 7;
	public static final int FIRST_STAGE = 2048;
	public static final int ERROR_KEY = -1;

	protected GridData grid;
	private boolean gameOver = false;
	private int round = 0;
	private int maxScore = 0;
	private int scoreSum = 0;
	private int maxTile = 0;
	private int passCnt[] = new int[STAGE_NUM];
	private int moveCnt = 0;
	private long startTime;
	private long endTime;

	private static final int RAND_MAX = 10;

	private Random rand;

	public Game() {
		this.round = 0;
		this.moveCnt = 0;
		this.maxScore = 0;
		this.scoreSum = 0;
		this.maxTile = 0;
		gameOver = false;
		rand = new Random();
		grid = new GridData(rand);
		for (int i = 0; i < GridData.INITIAL_TILE_NUM; i++)
			genNewTile();
		startTime = getCpuTime();
	}

	public void endGame() {
		endTime = getCpuTime();
		updateStats();
		dumpLog("open_src_version.log");
	}

	public void reset() {
		updateStats();
		grid.clear();
		gameOver = false;
		for (int i = 0; i < GridData.INITIAL_TILE_NUM; i++)
			genNewTile();
	}

	private void updateStats() {
		round++;
		int score = grid.getScore();
		scoreSum += score;
		if (score > maxScore)
			maxScore = score;
		int maxGridTile = grid.getMaxTile();
		if (maxGridTile > maxTile)
			maxTile = maxGridTile;
		for (int i = 0; i < STAGE_NUM; i++) {
			if (maxGridTile >= (FIRST_STAGE << i))
				passCnt[i]++;
		}
	}

	private void genNewTile() {
		grid.generateNewTile();
	}

	
	public int getRand() {
		return rand.nextInt(RAND_MAX);
	}

	public void setGameOver() {
		gameOver = grid.isGameOver();
	}

	
	public boolean isGameOver() {
		return gameOver;
	}

	
	public int getScore() {
		return grid.getScore();
	}

	
	public int getMaxScore() {
		int currentScore = getScore();
		if (currentScore > maxScore)
			return currentScore;
		else
			return maxScore;
	}

	
	public boolean insertDirection(DIR dir) {
		if (!grid.shift(dir))
			return false;
		genNewTile();
		moveCnt++;
		setGameOver();
		return true;
	}
	
	public int getmoveCnt() {
		
			return moveCnt;
	}

	
	public GridData getCurrentGrid() {
		GridData cloneObj = null;
		try {
			cloneObj = (GridData) grid.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cloneObj;
	}

	
	public void printGrid() {
		grid.print();
	}

	public void dumpLog(String fileName) {
		FileWriter out = null;
		try {
			out = new FileWriter(fileName, true);
			out.write("#Rounds: " + round + '\n');
			out.write("Highest Score: " + maxScore + '\n');
			out.write("Average Score: " + ((double) scoreSum / round) + '\n');
			out.write("Max Tile: " + maxTile + '\n');
			for (int i = 0; i < STAGE_NUM; i++) {
				out.write(String.valueOf((FIRST_STAGE << i)));
				out.write(" Rate: " + ((double) (passCnt[i] * 100 / round))
						+ "%\n");
			}
			out.write("Move Count: " + moveCnt + '\n');
			out.write("Time: " + (endTime - startTime) + '\n');
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static long getCpuTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? bean
				.getCurrentThreadCpuTime() : 0L;
	}
}
