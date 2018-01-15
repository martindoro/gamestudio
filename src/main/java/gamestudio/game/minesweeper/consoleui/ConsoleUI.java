package gamestudio.game.minesweeper.consoleui;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.consoleUI.ConsoleGameUI;
import gamestudio.entity.Score;
import gamestudio.game.minesweeper.core.Clue;
import gamestudio.game.minesweeper.core.Field;
import gamestudio.game.minesweeper.core.GameState;
import gamestudio.game.minesweeper.core.Mine;
import gamestudio.game.minesweeper.core.Tile;
import gamestudio.service.ScoreService;

public class ConsoleUI implements ConsoleGameUI{
	private Field field;
	private Scanner scanner;
	private static final Pattern INPUT_PATTERN = Pattern.compile("([OM])([A-I])([1-9])");
	@Autowired
	private ScoreService scoreservice;

	public ConsoleUI() {
		scanner = new Scanner(System.in);
	}

	@Override
	public void run() {
		this.field = new Field(9, 9, 1);
		long startTime = System.nanoTime();
		print();
		while (field.getState() == GameState.PLAYING) {
			processInput();
			print();
		}
		long endTime = System.nanoTime();
		if (field.getState() == GameState.SOLVED) {
			int timePlayed = (int) (System.nanoTime() - startTime) / 100000000;
			Score score = new Score(System.getProperty("user.name"), "mines", timePlayed);
			scoreservice.addScore(score);
			System.out.println("Gratulujem vyhral si za " + (endTime - startTime) / 1000000000 + " sekund.");
		} else if (field.getState() == GameState.FAILED) {
			System.out.println("Prehral si");
		}
	}

	private void processInput() {
		System.out.print("____________________Zadaj cinnost (MA1, OC4, X) ");
		String input = scanner.nextLine().trim().toUpperCase();

		if ("X".equals(input)) {
			field.setState(GameState.FAILED);
		}

		Matcher matcher = INPUT_PATTERN.matcher(input);
		if (matcher.matches()) {
			int row = matcher.group(2).charAt(0) - 'A';
			int column = Integer.parseInt(matcher.group(3)) - 1;

			if ("O".equals(matcher.group(1))) {
				field.openTile(row, column);
			} else {
				field.markTile(row, column);
			}
		} else {
			System.out.println("__________________Zle zadany vstup!");
		}
	}

	private void print() {
		System.out.print("  ");
		for (int column = 1; column <= field.getColumnCount(); column++) {
			System.out.print(column + " ");
		}
		System.out.println();

		for (int row = 0; row < field.getRowCount(); row++) {
			System.out.print((char) (row + 'A'));
			for (int column = 0; column < field.getColumnCount(); column++) {
				System.out.print(" ");
				Tile tile = field.getTile(row, column);
				switch (tile.getState()) {
				case CLOSED:
					System.out.print('.');
					break;
				case MARKED:
					System.out.print('M');
					break;
				case OPEN:
					if (tile instanceof Mine) {
						System.out.print('X');
					} else if (tile instanceof Clue) {
						System.out.print(((Clue) tile).getValue());
					}
					break;
				}
			}
			System.out.println();
		}
	}

	@Override
	public String getName() {
		return "mines";
	}

}
