package gamestudio.consoleUI;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.entity.Comment;
import gamestudio.entity.Rating;
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

public class ConsoleMenu {
	private ConsoleGameUI[] games;
	private Scanner sc = new Scanner(System.in);

	@Autowired
	private RatingService ratingService;

	@Autowired
	private ScoreService scoreService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private FavoriteService favoriteService;

	public ConsoleMenu(ConsoleGameUI[] games) {
		this.games = games;
	}

	public void show() throws IllegalAccessException, SQLException {
		while (true) {
			// favoriteService.setFavorite(new Favorite("Peter", "mines"));
			// favoriteService.removeFavorite("Alojz", "mines");
			// ratingService.setRating(new Rating("mines", "Alojz", 3));
			//scoreService.getTopScores("mines");
			System.out.println("///////////////////////////////////////");
			int index = 1;
			System.out.println("//////////////////////////List of games");
			for (ConsoleGameUI game : games) {
				double rating = ratingService.getAverageRating(game.getName());
				System.out.printf("%d. %s (%.1f/5)\n", index, game.getName(), rating);
				index++;
			}
			System.out.println("X. Exit");
			System.out.println("////////Select a game by number or name");
			System.out.println("///////////////////////////////////////");
			do {
				String line = sc.nextLine();
				if ("x".equals(line.toLowerCase())) {
					return;
				}
				try {
					index = Integer.parseInt(line);
				} catch (NumberFormatException e) {
					index = -1;
				}
				int ind = 0;
				for (ConsoleGameUI game : games) {
					ind++;
					if (line.equals(game.getName())) {
						index = ind;
						break;
					}
				}
			} while (!(index >= 1 && index <= games.length));
			games[index - 1].run();
			System.out.println(scoreService.getTopScores(games[index - 1].getName()));
			rateGame(games[index - 1]);
			commentGame(games[index - 1]);
		}
	}

	private void rateGame(ConsoleGameUI consoleGameUI) {
		int rating = (int) ratingService.getUserRating(System.getProperty("user.name"), consoleGameUI.getName());
		System.out.println("You rated this game " + rating + "out of 5 to change enter new rating(0-5) or Enter to dismiss");
		String userChoice = sc.nextLine();
		if ("".equals(userChoice.trim()))
			return;
		else
			try {
				ratingService.setRating(new Rating(consoleGameUI.getName(), System.getProperty("user.name"),
						Integer.parseInt(userChoice)));
			} catch (NumberFormatException | IllegalAccessException | SQLException e) {
				System.out.println("Aaaaarrrrrgh!");
			}
	}

	private void commentGame(ConsoleGameUI consoleGameUI) {
		List<Comment> comments = commentService.getComments(consoleGameUI.getName());
		for(Comment comment : comments) {
			System.out.println(comment);
		}
		System.out.println("These are comments for " + consoleGameUI.getName().toUpperCase() + " enter new comment or Enter to dismiss");
		String userChoice = sc.nextLine();
		if ("".equals(userChoice.trim()))
			return;
		else
			try {
				commentService.addComment(new Comment(System.getProperty("user.name"), consoleGameUI.getName(), userChoice));
			} catch (Exception e) {
				System.out.println("Aaaaarrrrrgh!");
			}
	}
}
