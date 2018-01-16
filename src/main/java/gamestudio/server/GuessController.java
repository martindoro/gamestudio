package gamestudio.server;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Comment;
import gamestudio.entity.Favorite;
import gamestudio.entity.Rating;
import gamestudio.entity.Score;
import gamestudio.game.guessnumber.core.Guess;
import gamestudio.game.guessnumber.core.GameState;
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GuessController {
	private int guessedNumber;
	private Guess guess;
	private String message;
	private double rating;
	private GameState gameState;

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private UserController userController;
	@Autowired
	private RatingService ratingService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private FavoriteService favoriteService;

	public boolean isFavorite() {
		return favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "guess"));
	}

	public double getRating() {
		return ratingService.getAverageRating("guess");
	}

	public String getMessage() {
		return message;
	}

	public GameState getGameState() {
		return gameState;
	}
	
	@RequestMapping("/guess_favorite")
	public String favorite(Model model) {
		favoriteService.setFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "guess"));
		fillModel(model);
		return "guess";
	}

	@RequestMapping("/guess_set_rating")
	public String rating(@RequestParam(value = "value", required = false) String value, Model model) {
		try {
			ratingService.setRating(
					new Rating("guess", userController.getLoggedPlayer().getLogin(), Integer.parseInt(value)));
		} catch (NumberFormatException | IllegalAccessException | SQLException e) {
			return "guess";
		}
		fillModel(model);
		return "guess";
	}

	@RequestMapping("/guess_comment")
	public String comment(String content, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "guess", content));
		fillModel(model);
		return "guess";
	}

	@RequestMapping("/guess_new")
	public String guessNew(Model model) {
		createGame();
		fillModel(model);
		return "guess";
	}
	
	@RequestMapping("/guess")
	public String puzzle(@RequestParam(value = "number", required = false) String number, Model model) {
		try {
			int result = guess.isSolved((Integer.parseInt(number)));
			if (guess.getGameState() == GameState.SOLVED) {
				message = "YES, " + number + " WAS MY NUMBER!";
				gameState = GameState.SOLVED;
				if (userController.isLogged())
					scoreService.addScore(
							new Score(userController.getLoggedPlayer().getLogin(), "guess", guess.getScore()));
			}
			if(result == 1)
				message = "MY NUMBER IS HIGHER";
			if(result == -1)
				message = "MY NUMBER IS LOWER";
		} catch (NumberFormatException e) {
			createGame();
		}
		fillModel(model);
		return "guess";
	}

	private void fillModel(Model model) {
		model.addAttribute("controller", this);
		model.addAttribute("scores", scoreService.getTopScores("guess"));
		model.addAttribute("comments", commentService.getComments("guess"));
		model.addAttribute("game", "guess");
		if (userController.isLogged())
			model.addAttribute("userRating",
					ratingService.getUserRating(userController.getLoggedPlayer().getLogin(), "guess"));
		if (userController.isLogged())
			model.addAttribute("favorite",
					favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "guess")));
		model.addAttribute("gameState", guess.getGameState());
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		sb.append("<form action=\"/guess\" method=\"post\">\n");
		sb.append("You are guessing number: <input type=\"text\" name=\"number\" autofocus='autofocus' th:value=\"${@guessController.isSolved}\"/>\n");
		sb.append("<input type=\"submit\" value=\"Guess\"/></form>\n");
		return sb.toString();
	}

	private void createGame() {
		guess = new Guess();
		gameState = GameState.PLAYING;
		message = "";
	}
}
