package gamestudio.service;

import java.util.List;

import gamestudio.entity.Comment;

public interface CommentService {
	void addComment(Comment comment);
	List<Comment> getComments(String game);
}
