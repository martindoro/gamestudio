package gamestudio.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Score;
import gamestudio.game.puzzle.core.Field;
import gamestudio.game.puzzle.core.GameState;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PuzzleController extends AbstractGameController{
	private Field field;
	private final String currentGame = "puzzle";

	@Autowired
	private ScoreService scoreService;
	
	@Override
	public String getGameName() {
		return currentGame;
	}
	
	@RequestMapping("/puzzle_beginner")
	public String puzzleBeginner(Model model) {
		createGame(3,3);
		fillModel(model);
		return "game";
	}

	@RequestMapping("/puzzle_intermediate")
	public String puzzleIntermediate(Model model) {
		createGame(4,4);
		fillModel(model);
		return "game";
	}
	
	@RequestMapping("/puzzle_expert")
	public String puzzleExpert(Model model) {
		createGame(5,5);
		fillModel(model);
		return "game";
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
		return "game";
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
		sb.append("<br></br>\n");
		sb.append("<div class='container'>\n");
		sb.append("<div class='row'>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/puzzle_beginner'>\n");
		sb.append("<input type='submit' value='New Beginner Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/puzzle_intermediate'>\n");
		sb.append("<input type='submit' value='New Intermediate Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/puzzle_expert'>\n");
		sb.append("<input type='submit' value='New Expert Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		return sb.toString();
	}

	private void createGame(int row, int column) {
		field = new Field(row, column);
		message = "";
	}
}
