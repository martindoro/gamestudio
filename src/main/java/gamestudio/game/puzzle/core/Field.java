package gamestudio.game.puzzle.core;

import java.util.Random;

public class Field {
	private final int rowCount;
	private final int columnCount;
	private final int lastNumber;
	private GameState state;
	private final int[][] numbers;
	private int timePlayed;
	String gameType;

	public String getGameType() {
		return gameType;
	}

	public int getTimePlayed() {
		return timePlayed;
	}

	public int getLastNumber() {
		return lastNumber;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int[][] getNumbers() {
		return numbers;
	}

	// constructs field which contains two dimensional array of stones, fills and
	// shuffles it
	public Field(int rowCount, int columnCount, String gameType) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.gameType = gameType;
		numbers = new int[rowCount][columnCount];
		this.lastNumber = rowCount * columnCount;
		fillNumbers();
		shuffleNumbers();
		state = GameState.PLAYING;
		this.timePlayed = (int) System.currentTimeMillis()/1000;
	}

	public Field(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		numbers = new int[rowCount][columnCount];
		this.lastNumber = rowCount * columnCount;
		fillNumbers();
		shuffleNumbers();
		state = GameState.PLAYING;
		this.timePlayed = (int) System.currentTimeMillis() / 1000;
	}

	private void shuffleNumbers() {
		for (int i = 0; i < (10 * lastNumber); i++) {
			int[] numbersToMove = possibleMoves(getPosition(lastNumber));
			int randomDirection = new Random().nextInt(numbersToMove.length);
			moveNumber(numbersToMove[randomDirection]);
		}
	}

	private int[] possibleMoves(int[] position) {
		int[] moves = { 0, 0, 0, 0 };
		if (position[0] > 0) {
			moves[0] = getNumber(position[0] - 1, position[1]);
		}

		if (position[0] < rowCount - 1) {
			moves[1] = getNumber(position[0] + 1, position[1]);
		}

		if (position[1] > 0) {
			moves[2] = getNumber(position[0], position[1] - 1);
		}

		if (position[1] < columnCount - 1) {
			moves[3] = getNumber(position[0], position[1] + 1);
		}

		int[] temp = new int[moves.length];
		int numberOfZeros = 0;
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != 0) {
				temp[i - numberOfZeros] = moves[i];
			} else {
				numberOfZeros++;
			}
		}
		int[] result = new int[temp.length - numberOfZeros];
		System.arraycopy(temp, 0, result, 0, result.length);

		return result;
	}

	public int getNumber(int row, int column) {
		return numbers[row][column];
	}

	private void fillNumbers() {
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				numbers[row][column] = row * columnCount + column + 1;
			}
		}
	}

	// moves user entered number to free space
	public void moveNumber(int userInput) {
		if (canBeMoved(userInput))
			swap(userInput);
		if (isSolved())
			state = GameState.STOPPED;
	}

	private boolean isSolved() {
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				if (getNumber(i, j) != i * columnCount + j + 1) {
					return false;
				}
			}
		}
		timePlayed = (int) System.currentTimeMillis()/1000 - timePlayed;
		return true;
	}

	private void swap(int userInput) {
		int[] numberPosition = getPosition(userInput);
		int[] spacePosition = getPosition(lastNumber);

		numbers[numberPosition[0]][numberPosition[1]] = lastNumber;
		numbers[spacePosition[0]][spacePosition[1]] = userInput;
	}

	private int[] getPosition(int input) {
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				if (numbers[row][column] == input)
					return new int[] { row, column };
			}
		}
		return null;
	}

	// checks if user defined number can be moved - is there a free space in direct
	// neighbourhood
	public boolean canBeMoved(int userInput) {
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				if (numbers[row][column] == userInput && checkFreeSpace(row, column))
					return true;
			}
		}
		return false;
	}

	private boolean checkFreeSpace(int row, int column) {
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int aRow = row + rowOffset;
			if (aRow >= 0 && aRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int aColumn = column + columnOffset;
					if (aColumn >= 0 && aColumn < columnCount) {
						if (numbers[row][aColumn] == lastNumber || numbers[aRow][column] == lastNumber) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
