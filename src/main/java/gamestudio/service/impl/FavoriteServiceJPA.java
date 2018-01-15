package gamestudio.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import gamestudio.entity.Favorite;
import gamestudio.service.FavoriteService;

@Transactional
public class FavoriteServiceJPA implements FavoriteService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void setFavorite(Favorite favorite) {
		try {
			entityManager.createQuery("SELECT f FROM Favorite f WHERE f.game = :game AND f.username = :username")
					.setParameter("game", favorite.getGame()).setParameter("username", favorite.getUsername())
					.getSingleResult();
			entityManager.createQuery("DELETE FROM Favorite f WHERE f.username = :username AND f.game = :game")
					.setParameter("username", favorite.getUsername()).setParameter("game", favorite.getGame())
					.executeUpdate();
		} catch (NoResultException e) {
			entityManager.persist(favorite);
		}
	}

	@Override
	public List<Favorite> getFavorite(String username) {
		return entityManager.createQuery("SELECT f FROM Favorite f WHERE f.username = :username")
				.setParameter("username", username).getResultList();
	}

	@Override
	public boolean isFavorite(Favorite favorite) {
		try {
			entityManager.createQuery("SELECT f FROM Favorite f WHERE f.game = :game AND f.username = :username")
					.setParameter("game", favorite.getGame()).setParameter("username", favorite.getUsername())
					.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
}
