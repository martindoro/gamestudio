package gamestudio.entity;

/*
 * CREATE TABLE rating (
 * ident INT PRIMARY KEY,
 * username VARCHAR(32) NOT NULL,
 * game VARCHAR(32) NOT NULL,
 * value INTEGER NOT NULL,
 * UNIQUE (username, game)
 * )
 */

public class Rating {
	private int ident;
	private String username;
	private String game;
	private int value;
	
	public int getIdent() {
		return ident;
	}
	public void setIdent(int ident) {
		this.ident = ident;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
