package gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.entity.Rating;
import gamestudio.service.RatingService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/rating")
public class RatingRestService {
	@Autowired
	private RatingService ratingService;

	@POST
	@Consumes("application/json")
	public Response setRating(Rating rating) throws IllegalAccessException, SQLException {
		ratingService.setRating(rating);
		return Response.ok().build();
	}

	@GET
	@Path("/{game}")
	@Produces("application/json")
	public double getAverageRatingForGame(@PathParam("game") String game) {
		return ratingService.getAverageRating(game);
	}
	
	@GET
	@Path("/game/{username}")
	@Produces("aplication/json")
	public int getUserRatingForGame(@PathParam("username") String username, @PathParam("game") String game) {
		return ratingService.getUserRating(username, game);
	}
}
