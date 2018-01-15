package gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/*
 * CREATE TABLE rating (
 * ident INT PRIMARY KEY,
 * username VARCHAR(32) NOT NULL,
 * game VARCHAR(32) NOT NULL,
 * value INTEGER NOT NULL,
 * UNIQUE (username, game)
 * )
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames= {"game", "username"})})
public class Rating {
	@Id
	@GeneratedValue
	private int ident;
	private String game;
	private String username;
	private int value;

	public Rating() {
	}

	public Rating(String game, String username, int value) {
		this.game = game;
		this.username = username;
		this.value = value;
	}

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

	@Override
	public String toString() {
		return "Rating from " + username.toUpperCase() + "on game " + game.toUpperCase() + " of " + value + "\n";
	}
}
