package gamestudio.game.memos.core;

import java.util.Random;

public class Field {
	private final int rowCount;
	private final int columnCount;
	private GameState gameState;
	private final Tile[][] tiles;
	private Tile[] preopenedTiles = new Tile[2];
	private int timePlayed;

	public Tile getTile(int row, int column) {
		return tiles[row][column];
	}

	public GameState getGameState() {
		return gameState;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public int getTimePlayed() {
		return timePlayed;
	}

	public Field(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		gameState = GameState.PLAYING;
		tiles = new Tile[rowCount][columnCount];
		generate();
		timePlayed = (int) System.currentTimeMillis() / 1000;
	}

	private void generate() {
		int[] start = new int[rowCount * columnCount / 2];
		int tilesToSet = start.length * 2;
		Random random = new Random();
		for (int i = 0; i < start.length; i++) {
			start[i] = i + 1;
		}

		int j = 0;
		int k = 1;

		while (tilesToSet > 0) {
			int row = random.nextInt(rowCount);
			int column = random.nextInt(columnCount);
			if (tiles[row][column] == null) {
				tiles[row][column] = new Tile(start[j]);
				if (k == 2) {
					j++;
					k = 1;
				} else {
					k++;
				}
				tilesToSet--;
			}
		}
	}

	public void openTile(int row, int column) {
		if (areTwoTilesPreopen())
			closePreopenedTiles();
		tiles[row][column].setTileState(TileState.PREOPEN);
		if (areTwoTilesPreopen()) {
			if (preopenedTiles[0].getValue() == preopenedTiles[1].getValue()) {
				preopenedTiles[0].setTileState(TileState.OPEN);
				preopenedTiles[1].setTileState(TileState.OPEN);
			}
		}
		if (isSolved()) {
			gameState = GameState.SOLVED;
		}
	}

	private void closePreopenedTiles() {
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				if (tile.getTileState() == TileState.PREOPEN)
					tile.setTileState(TileState.CLOSED);
			}
		}
	}

	private boolean areTwoTilesPreopen() {
		int numberOfPreopenedTiles = 0;
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				if (tile.getTileState() == TileState.PREOPEN) {
					numberOfPreopenedTiles++;
					preopenedTiles[numberOfPreopenedTiles - 1] = tile;
				}
			}
		}

		if (numberOfPreopenedTiles == 2) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isSolved() {
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				if (tile.getTileState() != TileState.OPEN)
					return false;
			}
		}
		timePlayed = (int) System.currentTimeMillis() / 1000 - timePlayed;
		return true;
	}
}
