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
import gamestudio.game.minesweeper.core.Clue;
import gamestudio.game.minesweeper.core.Field;
import gamestudio.game.minesweeper.core.GameState;
import gamestudio.game.minesweeper.core.Tile;
import gamestudio.game.minesweeper.core.TileState;
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesController {
	private Field field;
	boolean marking = false;
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
		return favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "mines"));
	}
	
	public double getRating() {
		return ratingService.getAverageRating("mines");
	}

	public String getMessage() {
		return message;
	}

	public boolean isMarking() {
		return marking;
	}

	@RequestMapping("/mines_mark")
	public String mines(Model model) {
		marking = !marking;
		fillModel(model);
		return "mines";
	}
	
	@RequestMapping("/mines_favorite")
	public String favorite(Model model) {
		favoriteService.setFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "mines"));
		fillModel(model);
		return "mines";
	}
	
	@RequestMapping("/mines_comment")
	public String comment(String content, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "mines", content));
		fillModel(model);
		return "/mines";	
	}
	
	@RequestMapping("/mines_set_rating")
	public String rating(@RequestParam(value = "value", required = false) String value, Model model) {
		try {
			ratingService.setRating(new Rating("mines", userController.getLoggedPlayer().getLogin(), Integer.parseInt(value)));
		} catch (NumberFormatException | IllegalAccessException | SQLException e) {
			return "mines";
		}
		fillModel(model);
		return "mines";
	}

	@RequestMapping("/mines_beginner")
	public String minesBeginner(Model model) {
		createGame(8,8,8);
		fillModel(model);
		return "mines";
	}

	@RequestMapping("/mines_intermediate")
	public String minesIntermediate(Model model) {
		createGame(16,16,25);
		fillModel(model);
		return "mines";
	}
	
	@RequestMapping("/mines_expert")
	public String minesExpert(Model model) {
		createGame(24,24,70);
		fillModel(model);
		return "mines";
	}
	
	@RequestMapping("/mines")
	public String mines(@RequestParam(value = "row", required = false) String row,
			@RequestParam(value = "column", required = false) String column, Model model) {
		try {
			if (marking)
				field.markTile(Integer.parseInt(row), Integer.parseInt(column));
			else
				field.openTile(Integer.parseInt(row), Integer.parseInt(column));
			if (field.getState() == GameState.FAILED)
				message = "FAILED";
			else if (field.getState() == GameState.SOLVED) {
				message = "SOLVED";
				if (userController.isLogged())
					scoreService.addScore(new Score(userController.getLoggedPlayer().getLogin(), "mines",
							1000 - 10 * field.getTimePlayed()));
			}
		} catch (NumberFormatException e) {
			createGame(8,8,8);
		}
		fillModel(model);
		return "mines";
	}

	private void fillModel(Model model) {
		model.addAttribute("minesController", this);
		model.addAttribute("scores", scoreService.getTopScores("mines"));
		model.addAttribute("comments", commentService.getComments("mines"));
		if(userController.isLogged())
			model.addAttribute("userRating", ratingService.getUserRating(userController.getLoggedPlayer().getLogin(), "mines"));
		if(userController.isLogged())
			model.addAttribute("favorite", favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), "mines")));
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class='mines_table'>\n");

		for (int row = 0; row < field.getRowCount(); row++) {
			sb.append("<tr>\n");
			for (int column = 0; column < field.getColumnCount(); column++) {
				Tile tile = field.getTile(row, column);
				String image = "closed";
				switch (tile.getState()) {
				case CLOSED:
					image = "closed";
					break;
				case MARKED:
					image = "marked";
					break;
				case OPEN:
					if (tile instanceof Clue)
						image = "open" + ((Clue) tile).getValue();
					else
						image = "mine";
					break;
				}

				sb.append("<td>\n");
				if (tile.getState() != TileState.OPEN && field.getState() == GameState.PLAYING) {
					sb.append(String.format("<a href='mines?row=%d&column=%d'>\n", row, column));
					sb.append("<img src='images/mines/" + image + ".png'>\n");
					sb.append("</a>\n");
				} else
					sb.append("<img src='images/mines/" + image + ".png'>\n");
				sb.append("</td>\n");
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	private void createGame(int row, int column, int mines) {
		field = new Field(row, column, mines);
		message = "";
	}
}
