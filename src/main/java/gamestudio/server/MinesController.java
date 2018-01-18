package gamestudio.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Score;
import gamestudio.game.minesweeper.core.Clue;
import gamestudio.game.minesweeper.core.Field;
import gamestudio.game.minesweeper.core.GameState;
import gamestudio.game.minesweeper.core.Tile;
import gamestudio.game.minesweeper.core.TileState;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesController extends AbstractGameController {
	private Field field;
	private boolean marking = false;
	private final String currentGame = "mines";

	@Autowired
	private ScoreService scoreService;

	public boolean isMarking() {
		return marking;
	}

	@Override
	public String getGameName() {
		return currentGame;
	}

	@RequestMapping("/mines_mark")
	public String mines(Model model) {
		marking = !marking;
		fillModel(model);
		return "game";
	}

	@RequestMapping("/mines_beginner")
	public String minesBeginner(Model model) {
		createGame(8, 8, 8);
		fillModel(model);
		return "game";
	}

	@RequestMapping("/mines_intermediate")
	public String minesIntermediate(Model model) {
		createGame(16, 16, 25);
		fillModel(model);
		return "game";
	}

	@RequestMapping("/mines_expert")
	public String minesExpert(Model model) {
		createGame(24, 24, 70);
		fillModel(model);
		return "game";
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
					scoreService.addScore(new Score(userController.getLoggedPlayer().getLogin(), currentGame,
							1000 - 10 * field.getTimePlayed()));
			}
		} catch (NumberFormatException e) {
			createGame(8, 8, 8);
		}
		fillModel(model);
		return "game";
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
		sb.append("<div class='centered'>\n");
		sb.append("<a href='mines_mark'>\n");
		if (marking) {
			sb.append("<span><b>Marking</b>(click to change)</span>\n");
		} else {
			sb.append("<span><b>Opening</b>(click to change)</span>\n");
		}
		sb.append("</a>\n");
		sb.append("</div>\n");
		sb.append("<br></br>\n");
		sb.append("<div class='container'>\n");
		sb.append("<div class='row'>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/mines_beginner'>\n");
		sb.append("<input type='submit' value='New Beginner Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/mines_intermediate'>\n");
		sb.append("<input type='submit' value='New Intermediate Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/mines_expert'>\n");
		sb.append("<input type='submit' value='New Expert Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		return sb.toString();
	}

	private void createGame(int row, int column, int mines) {
		field = new Field(row, column, mines);
		message = "";
	}
}
