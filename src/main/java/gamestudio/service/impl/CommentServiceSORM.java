package gamestudio.service.impl;

import java.util.List;

import gamestudio.entity.Comment;
import gamestudio.service.CommentService;
import orm.SORM;

public class CommentServiceSORM implements CommentService {
	private SORM sorm = new SORM();

	@Override
	public void addComment(Comment comment) {
		sorm.insert(comment);
	}

	@Override
	public List<Comment> getComments(String game) {
		return sorm.select(Comment.class, String.format("WHERE game = '%s' ORDER BY createdon DESC", game));
	}

}
