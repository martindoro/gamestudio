package gamestudio.game.puzzle.consoleui;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.consoleUI.ConsoleGameUI;
import gamestudio.entity.Score;
import gamestudio.game.puzzle.core.Field;
import gamestudio.game.puzzle.core.GameState;
import gamestudio.service.ScoreService;

public class ConsoleUI implements ConsoleGameUI {
	private Field field;
	private int[][] numbers;
	private Scanner sc;
	private GameState state = GameState.PLAYING;
	@Autowired
	private ScoreService scoreservice;

	// constructs a console
	public ConsoleUI() {
		sc = new Scanner(System.in);
	}

	// runs game loop
	@Override
	public void run() {
		this.field = new Field(4, 4);
		numbers = field.getNumbers();
		long startTime = System.nanoTime();
		print();
		while (state == GameState.PLAYING) {
			getUserInput();
			print();
		}
		int timePlayed = (int) (System.nanoTime() - startTime) / 100000000;
		System.out.println("You did it!");
		Score score = new Score(System.getProperty("user.name"), "puzzle", timePlayed);
		scoreservice.addScore(score);
		System.out.println("You have won and it took you " + timePlayed + " seconds!");
	}

	private void getUserInput() {
		System.out.println("____Enter NUMBER to move or X to exit____");
		String line = sc.nextLine();
		int userInput;
		if (line.toLowerCase().equals("x")) {
			System.out.println("Game exited.");
			state = GameState.STOPPED;
		}
		try {
			userInput = Integer.parseInt(line);
			if (field.canBeMoved(userInput)) {
				field.moveNumber(userInput);
			} else
				System.out.println("____Entered NUMBER can`t be moved!____");
		} catch (NumberFormatException e) {
			System.out.println("Bad input, try again!");
		}
	}

	// prints current field on console
	public void print() {
		System.out.println();

		for (int[] row : numbers) {
			for (int number : row) {
				if (number == field.getLastNumber()) {
					System.out.print("     ");
				} else if (number < 10) {
					System.out.print("  ");
					System.out.print("  " + number);
				} else if (number < 100) {
					System.out.print("  ");
					System.out.print(" " + number);
				} else {
					System.out.print("  " + number);
				}
			}
			System.out.println();
			System.out.println();
		}
	}

	@Override
	public String getName() {
		return "puzzle";
	}
}
