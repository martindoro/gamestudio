package gamestudio.game.guessnumber.core;

import java.util.Random;

public class Guess {
	private GameState gameState;
	private long timePlayed;
	private int guessedNumber;
	private int score;

	public int getGuessedNumber() {
		return guessedNumber;
	}

	public int getScore() {
		return score;
	}

	public GameState getGameState() {
		return gameState;
	}
	
	public void setGameState(GameState state) {
		gameState = state;
	}

	public Guess() {
		this.gameState = GameState.PLAYING;
		this.timePlayed = System.currentTimeMillis();
		this.guessedNumber = new Random().nextInt(100) + 1;
	}

	public int isSolved(int number) {
		if (guessedNumber == number) {
			int endTime = 100 - (int) ((System.currentTimeMillis() - timePlayed) / 1000);
			if (endTime > 0) {
				score = endTime;
			} else {
				score = 0;
			}
			gameState = GameState.SOLVED;
			return 0;
		} else if (guessedNumber > number) {
			return 1;
		} else {
			return -1;
		}
	}
}
