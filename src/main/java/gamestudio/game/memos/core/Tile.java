package gamestudio.game.memos.core;

public class Tile {
	private final int value;
	private TileState tileState;

	public Tile(int value) {
		this.tileState = TileState.CLOSED;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public TileState getTileState() {
		return tileState;
	}

	public void setTileState(TileState tileState) {
		this.tileState = tileState;
	}
}
