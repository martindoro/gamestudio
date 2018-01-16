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
public class PuzzleController {
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
		return favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "puzzle"));
	}
	
	public double getRating() {
		return ratingService.getAverageRating("puzzle");
	}
	
	public String getMessage() {
		return message;
	}

	@RequestMapping("/puzzle_favorite")
	public String favorite(Model model) {
		favoriteService.setFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "puzzle"));
		fillModel(model);
		return "puzzle";
	}
	
	@RequestMapping("/puzzle_set_rating")
	public String rating(@RequestParam(value = "value", required = false) String value, Model model) {
		try {
			ratingService.setRating(new Rating("puzzle", userController.getLoggedPlayer().getLogin(), Integer.parseInt(value)));
		} catch (NumberFormatException | IllegalAccessException | SQLException e) {
			return "puzzle";
		}
		fillModel(model);
		return "puzzle";
	}
	
	@RequestMapping("/puzzle_comment")
	public String comment(String content, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "puzzle", content));
		fillModel(model);
		return "puzzle";	
	}
	
	@RequestMapping("/puzzle_beginner")
	public String puzzleBeginner(Model model) {
		createGame(3,3);
		fillModel(model);
		return "puzzle";
	}

	@RequestMapping("/puzzle_intermediate")
	public String puzzleIntermediate(Model model) {
		createGame(4,4);
		fillModel(model);
		return "puzzle";
	}
	
	@RequestMapping("/puzzle_expert")
	public String puzzleExpert(Model model) {
		createGame(5,5);
		fillModel(model);
		return "puzzle";
	}
		
	@RequestMapping("/puzzle")
	public String puzzle(@RequestParam(value = "tile", required = false) String tile, Model model) {
		try {
			field.moveNumber(Integer.parseInt(tile));
			if (field.getState() == GameState.STOPPED) {
				message = "SOLVED";
				if(userController.isLogged())
					scoreService.addScore(new Score(userController.getLoggedPlayer().getLogin(), "puzzle", 1000 - 10 * field.getTimePlayed()));
			}
		} catch (NumberFormatException e) {
			createGame(3,3);
		}
		fillModel(model);
		return "puzzle";
	}
	
	private void fillModel(Model model) {
		model.addAttribute("controller", this);
		model.addAttribute("scores", scoreService.getTopScores("puzzle"));
		model.addAttribute("comments", commentService.getComments("puzzle"));
		model.addAttribute("game", "puzzle");
		if(userController.isLogged())
			model.addAttribute("userRating", ratingService.getUserRating(userController.getLoggedPlayer().getLogin(), "puzzle"));
		if(userController.isLogged())
			model.addAttribute("favorite", favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "puzzle")));
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\" class='puzzle_table'>\n");

		for (int row = 0; row < field.getRowCount(); row++) {
			sb.append("<tr>\n");
			for (int column = 0; column < field.getColumnCount(); column++) {
				int tile = field.getNumber(row, column);

				sb.append("<td>\n");
				if (field.getState() == GameState.PLAYING)
					sb.append(String.format("<a href='puzzle?tile=%d'>\n", tile));
				if (tile != field.getLastNumber()) {
					sb.append(tile);
				}
				sb.append("</a>\n");
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
