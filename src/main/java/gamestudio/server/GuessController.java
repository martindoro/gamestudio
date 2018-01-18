package gamestudio.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Score;
import gamestudio.game.guessnumber.core.GameState;
import gamestudio.game.guessnumber.core.Guess;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GuessController extends AbstractGameController {
	private Guess guess;
	private GameState gameState;
	private final String currentGame = "guess";
	boolean isNewGame = true;

	@Autowired
	private ScoreService scoreService;

	@Override
	public String getGameName() {
		return currentGame;
	}

	public GameState getGameState() {
		return gameState;
	}

	@RequestMapping("/guess_new")
	public String guessNew(Model model) {
		createGame();
		fillModel(model);
		return "game";
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
			if (result == 1)
				message = "MY NUMBER IS HIGHER";
			if (result == -1)
				message = "MY NUMBER IS LOWER";
		} catch (NumberFormatException e) {
			if(isNewGame) {
				createGame();
				isNewGame = false;
			}else {
				message = "Bad Input only NUMBERS allowed!";
				isNewGame = true;
			}
		}
		fillModel(model);
		return "game";
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		if (gameState == GameState.PLAYING) {
			sb.append("<form class='centered' action=\"/guess\" method=\"post\">\n");
			sb.append(
					"You are guessing number: <input type=\"text\" name=\"number\" autofocus='autofocus' th:value=\"${@guessController.isSolved}\"/>\n");
			sb.append("<input type=\"submit\" value=\"Guess\"/></form>\n");
		}
		sb.append("<br></br>\n");
		sb.append("<div class='container'>\n");
		sb.append("<div class='row'>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("<form class='centered' action='/guess_new'>\n");
		sb.append("<input type='submit' value='New Game'>\n");
		sb.append("</form>\n");
		sb.append("</div>\n");
		sb.append("<div class='col-4'>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		return sb.toString();
	}

	private void createGame() {
		guess = new Guess();
		isNewGame = true;
		gameState = guess.getGameState();
		message = "";
	}
}
