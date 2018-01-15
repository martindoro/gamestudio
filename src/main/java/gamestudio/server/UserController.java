package gamestudio.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Favorite;
import gamestudio.entity.Player;
import gamestudio.service.FavoriteService;
import gamestudio.service.PlayerService;
import gamestudio.service.RatingService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
	@Autowired
	private PlayerService playerService;
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private RatingService ratingService;

	private Player loggedPlayer;
	private List<Favorite> favorites;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public List<Favorite> getFavorites() {
		return favorites;
	}

	@RequestMapping("/user")
	public String user(Model model) {
		fillModel(model);
		return "login";
	}

	@RequestMapping("/login")
	public String login(Player player, Model model) {
		loggedPlayer = playerService.login(player.getLogin(), player.getPassword());
		model.addAttribute("favorites", favoriteService.getFavorite(player.getLogin()));
		fillModel(model);
		return isLogged() ? "index" : "login";
	}

	@RequestMapping("/logout")
	public String logout(Model model) {
		loggedPlayer = null;
		fillModel(model);
		return "index";
	}

	@RequestMapping("/")
	public String index(Player player, Model model) {
		if (isLogged())
			model.addAttribute("favorites", favoriteService.getFavorite(loggedPlayer.getLogin()));
		fillModel(model);
		return "index";
	}

	@RequestMapping("/register")
	public String register(String login, String password, String rpassword, Model model) {
		message = "";
		Player p = new Player(login, password);
		if (p != null && login != null && password != null) {
			if (!playerService.existsPlayer(login)) {
				if (password.equals(rpassword)) {
					playerService.register(p);
					loggedPlayer = playerService.login(p.getLogin(), p.getPassword());
				} else {
					message = "password not match";
				}
			} else {
				message = "login exists, choose another";
			}
		}
		if(login == null || password == null) {
			message = "bad input in registration fields";
		}
		fillModel(model);
		return isLogged() ? "index" : "register";
	}

	public Player getLoggedPlayer() {
		return loggedPlayer;
	}

	public boolean isLogged() {
		return loggedPlayer != null;
	}

	private void fillModel(Model model) {
		model.addAttribute("avg_mines", ratingService.getAverageRating("mines"));
		model.addAttribute("avg_puzzle", ratingService.getAverageRating("puzzle"));
		model.addAttribute("avg_guess", ratingService.getAverageRating("guess"));
		model.addAttribute("avg_memos", ratingService.getAverageRating("memos"));
		model.addAttribute("avg_slider", ratingService.getAverageRating("slider"));
	}
}
