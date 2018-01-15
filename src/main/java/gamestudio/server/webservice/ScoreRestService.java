package gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.entity.Score;
import gamestudio.service.ScoreService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/score")
public class ScoreRestService {
	@Autowired
	private ScoreService scoreService;

	@POST
	@Consumes("application/json")
	public Response addScore(Score score) {
		scoreService.addScore(score);
		return Response.ok().build();
	}

	@GET
	@Path("/{game}")
	@Produces("application/json")
	public List<Score> getBestScoresForGame(@PathParam("game") String game) {
		return scoreService.getTopScores(game);
	}
}
