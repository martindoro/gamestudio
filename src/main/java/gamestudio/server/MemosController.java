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
import gamestudio.game.memos.core.Field;
import gamestudio.game.memos.core.GameState;
import gamestudio.game.memos.core.Tile;
import gamestudio.game.memos.core.TileState;
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MemosController {
	private Field field;
	private String message;

	@Autowired
	private RatingService ratingService;
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private UserController userController;
	@Autowired
	private CommentService commentService;
	@Autowired
	private FavoriteService favoriteService;

	public boolean isFavorite() {
		return favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "memos"));
	}

	public double getRating() {
		return ratingService.getAverageRating("memos");
	}

	public String getMessage() {
		return message;
	}

	@RequestMapping("/memos_favorite")
	public String favorite(Model model) {
		favoriteService.setFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "memos"));
		fillModel(model);
		return "memos";
	}

	@RequestMapping("/memos_comment")
	public String comment(String content, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "memos", content));
		fillModel(model);
		return "/memos";
	}

	@RequestMapping("/memos_set_rating")
	public String rating(@RequestParam(value = "value", required = false) String value, Model model) {
		try {
			ratingService.setRating(
					new Rating("memos", userController.getLoggedPlayer().getLogin(), Integer.parseInt(value)));
		} catch (NumberFormatException | IllegalAccessException | SQLException e) {
			return "memos";
		}
		fillModel(model);
		return "memos";
	}

	@RequestMapping("/memos_beginner")
	public String memosBeginner(Model model) {
		createGame(4, 4);
		fillModel(model);
		return "memos";
	}

	@RequestMapping("/memos_intermediate")
	public String memosIntermediate(Model model) {
		createGame(5, 4);
		fillModel(model);
		return "memos";
	}

	@RequestMapping("/memos_expert")
	public String memosExpert(Model model) {
		createGame(6, 5);
		fillModel(model);
		return "memos";
	}

	@RequestMapping("/memos")
	public String memos(@RequestParam(value = "row", required = false) String row,
			@RequestParam(value = "column", required = false) String column, Model model) {
		try {
			field.openTile(Integer.parseInt(row), Integer.parseInt(column));
			if (field.getGameState() == GameState.SOLVED) {
				message = "SOLVED";
				if (userController.isLogged())
					scoreService.addScore(new Score(userController.getLoggedPlayer().getLogin(), "memos",
							1000 - 10 * field.getTimePlayed()));
			}
		} catch (NumberFormatException e) {
			createGame(4, 4);
		}
		fillModel(model);
		return "memos";
	}

	private void fillModel(Model model) {
		model.addAttribute("controller", this);
		model.addAttribute("scores", scoreService.getTopScores("memos"));
		model.addAttribute("comments", commentService.getComments("memos"));
		model.addAttribute("game", "memos");
		if (userController.isLogged()) {
			model.addAttribute("userRating",
					ratingService.getUserRating(userController.getLoggedPlayer().getLogin(), "memos"));
			model.addAttribute("favorite",
					favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "memos")));
		}
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class='memos_table'>\n");

		for (int row = 0; row < field.getRowCount(); row++) {
			sb.append("<tr>\n");
			for (int column = 0; column < field.getColumnCount(); column++) {
				Tile tile = field.getTile(row, column);
				String image = "closed";
				switch (tile.getTileState()) {
				case CLOSED:
					image = "0";
					break;
				case PREOPEN:
					image = Integer.toString(tile.getValue());
					break;
				case OPEN:
					image = Integer.toString(tile.getValue());
					break;
				}

				sb.append("<td>\n");
				if (tile.getTileState() == TileState.CLOSED && field.getGameState() == GameState.PLAYING) {
					sb.append(String.format("<a href='memos?row=%d&column=%d'>\n", row, column));
					sb.append("<img src='images/memos/" + image + ".png'>\n");
					sb.append("</a>\n");
				} else
					sb.append("<img src='images/memos/" + image + ".png'>\n");
				sb.append("</td>\n");
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	private void createGame(int row, int column) {
		field = new Field(row, column);
		message = "";
	}
}
