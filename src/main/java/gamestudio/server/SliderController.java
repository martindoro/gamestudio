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
public class SliderController extends AbstractGameController {
	private Field field;
	private final String currentGame = "slider";

	@Autowired
	private ScoreService scoreService;

	@Override
	public String getGameName() {
		return currentGame;
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