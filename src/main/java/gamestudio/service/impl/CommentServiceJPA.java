package gamestudio.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import gamestudio.entity.Comment;
import gamestudio.service.CommentService;

@Transactional
public class CommentServiceJPA implements CommentService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void addComment(Comment comment) {
		if(!comment.getContent().trim().isEmpty())
			entityManager.persist(comment);
	}

	@Override
	public List<Comment> getComments(String game) {
		return entityManager.createQuery("SELECT c FROM Comment c WHERE c.game = :game ORDER BY c.createdOn DESC")
				.setParameter("game", game).setMaxResults(10).getResultList();
	}

}
