package gamestudio.game.guessnumber.consoleui;

import java.util.Random;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.consoleUI.ConsoleGameUI;
import gamestudio.entity.Score;
import gamestudio.game.guessnumber.core.GameState;
import gamestudio.service.ScoreService;

public class ConsoleUI implements ConsoleGameUI {
	private Scanner scanner;
	private GameState state = GameState.PLAYING;
	private long time;
	private int number;
	private final int range = 10;
	@Autowired
	private ScoreService scoreservice;

	public ConsoleUI() {
		scanner = new Scanner(System.in);
	}

	public int getNumber() {
		return number;
	}

	public long getTime() {
		return time;
	}

	@Override
	public void run() {
		number = new Random().nextInt(range) + 1;
		state = GameState.PLAYING;
		while (state == GameState.PLAYING)
			processInput();
	}

	private void processInput() {
		System.out.println("Guess the number or X to exit");
		int index;
		long timeStarted = System.nanoTime();
		do {
			String line = scanner.nextLine();
			if (line.toLowerCase().equals("x")) {
				System.out.println("Game exited.");
				state = GameState.SOLVED;
			}
			try {
				index = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				index = -1;
			}
		} while (!(index >= 1 && index <= range));

		if (index == number) {
			int timePlayed = (int) (System.nanoTime() - timeStarted) / 100000000;
			System.out.println("Got it!");
			state = GameState.SOLVED;
			Score score = new Score(System.getProperty("user.name"), "guessnumber", timePlayed);
			scoreservice.addScore(score);
		} else if (index < number) {
			System.out.println("My number is higher");
		} else {
			System.out.println("My number is lower");
		}
	}

	@Override
	public String getName() {
		return "guessnumber";
	}
}
