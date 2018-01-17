package gamestudio.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import gamestudio.entity.Favorite;
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

public abstract class AbstractGameController {
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private CommentService commentService;
	@Autowired
	protected FavoriteService favoriteService;
	@Autowired
	protected RatingService ratingService;
	@Autowired
	protected UserController userController;
	
	protected String message;
	
	public String getMessage() {
		return message;
	}
	
	public abstract String getGameName();
	
	public boolean isFavorite() {
		return favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), getGameName()));
	}
	
	public double getRating() {
		return ratingService.getAverageRating(getGameName());
	}

	protected void fillModel(Model model) {
		model.addAttribute("controller", this);
		model.addAttribute("scores", scoreService.getTopScores(getGameName()));
		model.addAttribute("comments", commentService.getComments(getGameName()));
		model.addAttribute("game", getGameName());
		if(userController.isLogged())
			model.addAttribute("userRating", ratingService.getUserRating(userController.getLoggedPlayer().getLogin(), getGameName()));
		if(userController.isLogged())
			model.addAttribute("favorite", favoriteService.isFavorite(new Favorite(userController.getLoggedPlayer().getLogin(), getGameName())));
	}
}
