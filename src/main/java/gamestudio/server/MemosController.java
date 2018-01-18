package gamestudio.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Score;
import gamestudio.game.memos.core.Field;
import gamestudio.game.memos.core.GameState;
import gamestudio.game.memos.core.Tile;
import gamestudio.game.memos.core.TileState;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MemosController extends AbstractGameController{
	private Field field;
	private final String currentGame = "memos";

	@Autowired
	private ScoreService scoreService;
	
	@Override
	public String getGameName() {
		return currentGame;
	}
	
	@RequestMapping("/memos_beginner")
	public String memosBeginner(Model model) {
		createGame(4, 4);
		fillModel(model);
		return "game";
	}

	@RequestMapping("/memos_intermediate")
	public String memosIntermediate(Model model) {
		createGame(5, 4);
		fillModel(model);
		return "game";
	}

	@RequestMapping("/memos_expert")
	public String memosExpert(Model model) {
		createGame(6, 5);
		fillModel(model);
		return "game";
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
		return "game";
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
		sb.append("<br></br>\n");
		sb.append("<div class='container'>\n");
		sb.append("<div class='row'>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/memos_beginner'>\n");
		sb.append("<input type='submit' value='New Beginner Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/memos_intermediate'>\n");
		sb.append("<input type='submit' value='New Intermediate Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/memos_expert'>\n");
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
