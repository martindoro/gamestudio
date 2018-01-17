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
	private GameState gameState = GameState.PLAYING;
	private final String currentGame = "guess";

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
		return "guess";
	}

	@RequestMapping("/guess")
	public String puzzle(@RequestParam(value = "number", required = false) String number, Model model) {
		try {
			int result = guess.isSolved((Integer.parseInt(number)));
			if (guess.getGameState() == GameState.SOLVED) {
				message = "YES, " + number + " WAS MY NUMBER!";
				guess.setGameState(GameState.SOLVED);
				if (userController.isLogged())
					scoreService.addScore(
							new Score(userController.getLoggedPlayer().getLogin(), "guess", guess.getScore()));
			}
			if (result == 1)
				message = "MY NUMBER IS HIGHER";
			if (result == -1)
				message = "MY NUMBER IS LOWER";
		} catch (NumberFormatException e) {
			createGame();
		}
		fillModel(model);
		return "guess";
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		if (gameState == GameState.PLAYING) {
			sb.append("<form action=\"/guess\" method=\"post\">\n");
			sb.append(
					"You are guessing number: <input type=\"text\" name=\"number\" autofocus='autofocus' th:value=\"${@guessController.isSolved}\"/>\n");
			sb.append("<input type=\"submit\" value=\"Guess\"/></form>\n");
		}
		return sb.toString();
	}

	private void createGame() {
		guess = new Guess();
		message = "";
	}
}
