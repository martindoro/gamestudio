package gamestudio.entity;

/*
 * CREATE TABLE score (
 * ident INT PRIMARY KEY,
 * username VARCHAR(32) NOT NULL,
 * game VARCHAR(32) NOT NULL,
 * value INTEGER NOT NULL
 * )
 */
public class Score {
	private int ident;
	private String username;
	private String game;
	private int value;
	
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
	public int getIdent() {
		return ident;
	}
	public void setIdent(int ident) {
		this.ident = ident;
	}
	@Override
	public String toString() {
		return "Score username " + username.toUpperCase() + ", game " + game.toUpperCase() + ", score " + value + "\n";
	}
	
	
}
