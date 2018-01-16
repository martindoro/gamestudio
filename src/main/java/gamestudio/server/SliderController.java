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
import gamestudio.game.puzzle.core.Field;
import gamestudio.game.puzzle.core.GameState;
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SliderController {
	private Field field;
	private String message;
	private double rating;

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
		return favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "slider"));
	}

	public double getRating() {
		return ratingService.getAverageRating("slider");
	}

	public String getMessage() {
		return message;
	}

	@RequestMapping("/slider_favorite")
	public String favorite(Model model) {
		favoriteService.setFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "slider"));
		fillModel(model);
		return "slider";
	}

	@RequestMapping("/slider_set_rating")
	public String rating(@RequestParam(value = "value", required = false) String value, Model model) {
		try {
			ratingService.setRating(
					new Rating("slider", userController.getLoggedPlayer().getLogin(), Integer.parseInt(value)));
		} catch (NumberFormatException | IllegalAccessException | SQLException e) {
			return "slider";
		}
		fillModel(model);
		return "slider";
	}

	@RequestMapping("/slider_comment")
	public String comment(String content, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "slider", content));
		fillModel(model);
		return "slider";
	}

	@RequestMapping("/slider_beginner")
	public String puzzleBeginner(Model model) {
		createGame(3, 3, "a");
		fillModel(model);
		return "slider";
	}

	@RequestMapping("/slider_intermediate")
	public String puzzleIntermediate(Model model) {
		createGame(4, 4, "b");
		fillModel(model);
		return "slider";
	}

	@RequestMapping("/slider_expert")
	public String puzzleExpert(Model model) {
		createGame(5, 5, "c");
		fillModel(model);
		return "slider";
	}

	@RequestMapping("/slider")
	public String puzzle(@RequestParam(value = "tile", required = false) String tile, Model model) {
		try {
			field.moveNumber(Integer.parseInt(tile));
			if (field.getState() == GameState.STOPPED) {
				message = "SOLVED";
				if (userController.isLogged())
					scoreService.addScore(new Score(userController.getLoggedPlayer().getLogin(), "slider",
							1000 - 10 * field.getTimePlayed()));
			}
		} catch (NumberFormatException e) {
			createGame(3, 3, "a");
		}
		fillModel(model);
		return "slider";
	}

	private void fillModel(Model model) {
		model.addAttribute("controller", this);
		model.addAttribute("scores", scoreService.getTopScores("slider"));
		model.addAttribute("comments", commentService.getComments("slider"));
		model.addAttribute("game", "slider");
		if (userController.isLogged())
			model.addAttribute("userRating",
					ratingService.getUserRating(userController.getLoggedPlayer().getLogin(), "slider"));
		if (userController.isLogged())
			model.addAttribute("favorite",
					favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "slider")));
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\" class='slider_table'>\n");

		for (int row = 0; row < field.getRowCount(); row++) {
			sb.append("<tr>\n");
			for (int column = 0; column < field.getColumnCount(); column++) {
				int tile = field.getNumber(row, column);
				String gameType = field.getGameType();

				sb.append("<td>\n");
				sb.append(String.format("<a href='slider?tile=%d'>\n", tile));
				if (tile == field.getLastNumber() && field.getState() == GameState.PLAYING) {
					sb.append("<img src='images/slider/" + gameType + "/empty.png'>\n");
				} else {
					sb.append("<img src='images/slider/" + gameType + "/" + tile + ".png'>\n");
				}
				sb.append("</a>\n");
				sb.append("</a>\n");
				sb.append("</td>\n");
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	public String hint() {
		return "/images/slider/" + field.getGameType() + "/image.png";
	}

	private void createGame(int row, int column, String gameType) {
		field = new Field(row, column, gameType);
		message = "";
	}
}