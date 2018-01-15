package gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.entity.Favorite;
import gamestudio.service.FavoriteService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/favorite")
public class FavoriteRestService {
	@Autowired
	private FavoriteService favoriteService;

	@POST
	@Consumes("application/json")
	public Response setFavorite(Favorite favorite) {
		favoriteService.setFavorite(favorite);
		return Response.ok().build();
	}

	@GET
	@Path("/{username}")
	@Produces("application/json")
	public List<Favorite> getFavoriteForGame(@PathParam("username") String username) {
		return favoriteService.getFavorite(username);
	}
}
