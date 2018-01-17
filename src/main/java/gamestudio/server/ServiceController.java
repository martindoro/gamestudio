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
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ServiceController {
	@Autowired
	private CommentService commentService;
	@Autowired
	protected FavoriteService favoriteService;
	@Autowired
	protected RatingService ratingService;
	@Autowired
	protected UserController userController;
	
	@RequestMapping("/favorite")
	public String favorite(String game, Model model) {
		favoriteService.setFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), game));
		return "forward:/" + game;	
	}
	
	@RequestMapping("/comment")
	public String comment(String content,String game, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), game, content));
		return "forward:/" + game;	
	}
	
	@RequestMapping("/rating")
	public String rating(@RequestParam(value = "value", required = false) String value, @RequestParam(value = "game", required = false) String game, Model model) {
		try {
			ratingService.setRating(new Rating(game, userController.getLoggedPlayer().getLogin(), Integer.parseInt(value)));
		} catch (NumberFormatException | IllegalAccessException | SQLException e) {
			return "forward:/" + game;
		}
		return "forward:/" + game;
	}
}
