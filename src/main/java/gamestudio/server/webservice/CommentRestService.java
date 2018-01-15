package gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.entity.Comment;
import gamestudio.service.CommentService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/comment")
public class CommentRestService {
	@Autowired
	private CommentService commentService;

	@POST
	@Consumes("application/json")
	public Response addComment(Comment comment) {
		commentService.addComment(comment);
		return Response.ok().build();
	}

	@GET
	@Path("/{game}")
	@Produces("application/json")
	public List<Comment> getCommentsForGame(@PathParam("game") String game) {
		return commentService.getComments(game);
	}
}
