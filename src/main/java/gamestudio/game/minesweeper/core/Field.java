package gamestudio.game.minesweeper.core;

import java.util.Random;

public class Field {
	private final int rowCount;
	private final int columnCount;
	private final int mineCount;
	private GameState state = GameState.PLAYING;
	private final Tile[][] tiles;
	private int timePlayed;
	
	public int getTimePlayed() {
		return timePlayed;
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

	public int getMineCount() {
		return mineCount;
	}

	public Tile getTile(int row, int column) {
		return tiles[row][column];
	}

	public void markTile(int row, int column) {
		if (state == GameState.PLAYING) {
			Tile tile = tiles[row][column];
			if (tile.getState() == TileState.CLOSED) {
				tile.setState(TileState.MARKED);
			} else if (tile.getState() == TileState.MARKED) {
				tile.setState(TileState.CLOSED);
			}
		}
	}

	public void openTile(int row, int column) {
		Tile tile = tiles[row][column];
		if (state == GameState.PLAYING) {
			if (tile.getState() == TileState.CLOSED) {
				if (tile instanceof Clue) {
					tile.setState(TileState.OPEN);
					if (((Clue) tile).getValue() == 0)
						openNeighbourTiles(row, column);
					if (isSolved()) {
						state = GameState.SOLVED;
						return;
					}
				} else {
					tile.setState(TileState.OPEN);
					state = GameState.FAILED;
				}
			}
		}
	}

	private void openNeighbourTiles(int row, int column) {
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int aRow = row + rowOffset;
			if (aRow >= 0 && aRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int aColumn = column + columnOffset;
					if (aColumn >= 0 && aColumn < columnCount) {
						openTile(aRow, aColumn);
					}
				}
			}
		}
	}

	private boolean isSolved() {
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				if (tile instanceof Clue && tile.getState() != TileState.OPEN)
					return false;
			}
		}
		timePlayed = (int) System.currentTimeMillis()/1000 - timePlayed;
		return true;
	}

	public Field(int rowCount, int columnCount, int mineCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.mineCount = mineCount;
		tiles = new Tile[rowCount][columnCount];
		generate();
		this.timePlayed = (int) System.currentTimeMillis()/1000;
	}

	private void generate() {
		generateMines();
		fillWithClues();
	}

	private void fillWithClues() {
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				if (!(tiles[row][column] instanceof Mine)) {
					tiles[row][column] = new Clue(countMines(row, column));
				}
			}
		}
	}

	private void generateMines() {
		int minesToSet = mineCount;
		Random random = new Random();

		while (minesToSet > 0) {
			int row = random.nextInt(rowCount);
			int column = random.nextInt(columnCount);
			if (tiles[row][column] == null) {
				tiles[row][column] = new Mine();
				minesToSet--;
			}
		}
	}

	private int countMines(int row, int column) {
		int minesCount = 0;
		int rowMinus = row - 1;
		int columnMinus = column - 1;
		int rowPlus = row + 1;
		int columnPlus = column + 1;

		if (rowMinus >= 0) {
			if (columnMinus >= 0) {
				if (tiles[rowMinus][columnMinus] instanceof Mine)
					minesCount++;
			}

			if (tiles[rowMinus][column] instanceof Mine)
				minesCount++;

			if (columnPlus < columnCount) {
				if (tiles[rowMinus][columnPlus] instanceof Mine)
					minesCount++;
			}
		}

		if (columnMinus >= 0) {
			if (tiles[row][columnMinus] instanceof Mine)
				minesCount++;
		}

		if (columnPlus < columnCount) {
			if (tiles[row][columnPlus] instanceof Mine)
				minesCount++;
		}

		if (rowPlus < rowCount) {
			if (columnMinus >= 0) {
				if (tiles[rowPlus][columnMinus] instanceof Mine)
					minesCount++;
			}

			if (tiles[rowPlus][column] instanceof Mine)
				minesCount++;

			if (columnPlus < columnCount) {
				if (tiles[rowPlus][columnPlus] instanceof Mine)
					minesCount++;
			}
		}
		return minesCount;
	}
}
