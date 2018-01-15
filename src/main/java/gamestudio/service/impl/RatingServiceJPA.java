package gamestudio.service.impl;

import java.sql.SQLException;
import java.text.DecimalFormat;

import gamestudio.entity.Rating;
import gamestudio.service.RatingService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
	@PersistenceContext
	private EntityManager entityManager;
	private Rating rating;

	@Override
	public void setRating(Rating rating) throws IllegalAccessException, SQLException {
		try {
			Rating r = (Rating) entityManager
					.createQuery("SELECT r FROM Rating r WHERE r.game = :game AND r.username = :username")
					.setParameter("game", rating.getGame()).setParameter("username", rating.getUsername())
					.getSingleResult();
			r.setValue(rating.getValue());
		} catch (NoResultException e) {
			entityManager.persist(rating);
		}
	}

	@Override
	public double getAverageRating(String game) {
		Object o = entityManager.createQuery("SELECT AVG(r.value) FROM Rating r WHERE r.game = :game")
				.setParameter("game", game).getSingleResult();
		return o == null ? 0 : Double.parseDouble(o.toString());
	}

	@Override
	public int getUserRating(String username, String game) {
		try {
			return (int) entityManager
					.createQuery("SELECT r.value FROM Rating r WHERE r.game = :game AND r.username = :username")
					.setParameter("game", game).setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return 0;
		}
	}

}
