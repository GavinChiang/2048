package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import engine.Game;
import engine.GridData.*;
import engine.*;

public class GameWindow extends Game {

	public static final int GRID_LENGTH = 4;
	public static final int GRID_SPACE = 10;
	public static final int GRID_START_X = 10;
	public static final int GRID_START_Y = 10;
	private static final int BLOCKSIZE = 70;
	private static final int FONTSIZE = 24;
	private Canvas canvas;
	protected Shell shell;

	public GameWindow() {

	}

	
	public void open() {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	protected void createContents() {
		shell = new Shell();
		shell.setSize(750, 638);
		shell.setText("2048");
		shell.setLayout(null);
		shell.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (isGameOver())
					return;
				
				if (e.keyCode == SWT.ARROW_UP) {
					insertDirection(DIR.UP);
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					insertDirection(DIR.DOWN);
				} else if (e.keyCode == SWT.ARROW_LEFT) {
					insertDirection(DIR.LEFT);
				} else if (e.keyCode == SWT.ARROW_RIGHT) {
					insertDirection(DIR.RIGHT);
				} else if (e.keyCode == SWT.CR) {
					reset();
				} 
				canvas.redraw();
			}
		});

		draw();
	}

	private void setFontColorByTile(GC gc, int tile) {
		Display display = Display.getDefault();
		Font font = new Font(display, "Arial", FONTSIZE, SWT.BOLD);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		switch (tile) {
		case 0:
			gc.setBackground(new Color(display, 200, 200, 200));
			gc.setForeground(new Color(display, 200, 200, 200));
			break;
		case 2:
			gc.setBackground(new Color(display, 255, 0, 0));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 4:
			gc.setBackground(new Color(display, 255,165, 0));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 8:
			gc.setBackground(new Color(display, 255,255, 0));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 16:
			gc.setBackground(new Color(display, 0,255, 0));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 32:
			gc.setBackground(new Color(display, 0,255,255));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 64:
			gc.setBackground(new Color(display, 0, 0,255));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 128:
			gc.setBackground(new Color(display, 43, 0,255));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 256:
			gc.setBackground(new Color(display, 87, 0,255));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 512:
			gc.setBackground(new Color(display, 238, 121, 159));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 1024:
			gc.setBackground(new Color(display, 238, 48, 167));
			gc.setForeground(new Color(display, 0, 0, 0));
			break;
		case 2048:
			gc.setBackground(new Color(display, 139, 34, 82));
			gc.setForeground(new Color(display, 248, 250, 247));
			break;
		default:
			gc.setBackground(new Color(display, 60, 58, 49));
			gc.setForeground(new Color(display, 248, 250, 247));
			break;
		}
		gc.setFont(font);
		font.dispose();
	}

	private void draw() {
		canvas = new Canvas(shell, SWT.NONE);
		canvas.setBounds(0, 0, 600, 600);
		canvas.setLayout(null);

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = new GC(canvas);
				FontMetrics fm = gc.getFontMetrics();
				Display display = Display.getDefault();
				Font font = new Font(display, "Arial", FONTSIZE, SWT.BOLD);
				gc.setFont(font);
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
				String scoreStr = "Score: " + Integer.toString(getScore());
				gc.drawText(scoreStr, 330, 120);
				String moveCntStr = "Moves: " + Integer.toString(getmoveCnt());
				gc.drawText(moveCntStr, 330, 170);
				String maxScareStr = "Max Score: " + Integer.toString(getMaxScore());
				gc.drawText(maxScareStr, 330, 20);
				if(isGameOver()){
				String gameOverStr = "Game over" ;
				gc.drawText(gameOverStr, 330, 220);
				}
				for (int i = 0; i < GRID_LENGTH; i++) {
					for (int j = 0; j < GRID_LENGTH; j++) {

						int number = grid.getTileByXY(i, j);
						setFontColorByTile(gc, number);
						int x = (j * (BLOCKSIZE + GRID_SPACE) + GRID_START_Y);
						int y = (i * (BLOCKSIZE + GRID_SPACE) + GRID_START_X);
						gc.fillRectangle(x, y, BLOCKSIZE, BLOCKSIZE);
						y += ((BLOCKSIZE - fm.getHeight()) / 2) - 7;
						int stringLength = 0;
						String numberStr = String.valueOf(number);
						for (int p = 0; p < numberStr.length(); p++) {
							stringLength += gc.getAdvanceWidth(numberStr
									.charAt(p));
						}
						x += ((BLOCKSIZE - stringLength) / 2);
						gc.drawText(numberStr, x, y, true);
					}
				}
				font.dispose();
				gc.dispose();
			}
		});
		canvas.redraw();
	}

	
	public static void main(String[] args) {
		try {
			GameWindow window = new GameWindow();
			window.open();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
