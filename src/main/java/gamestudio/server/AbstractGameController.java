package gamestudio.server;

import org.springframework.beans.factory.annotation.Autowired;

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
	protected FavoriteService favoriteservice;
	
	@Autowired
	protected RatingService ratingService;
	
	@Autowired
	protected UserController userController;
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	
}
