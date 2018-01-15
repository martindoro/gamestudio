package gamestudio.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import gamestudio.entity.Player;
import gamestudio.service.PlayerService;

@Transactional
public class PlayerServiceJPA implements PlayerService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void register(Player player) {
		if(!(player == null) && !(player.getLogin().isEmpty() || player.getPassword().isEmpty()))
			entityManager.persist(player);
	}
	
	@Override
	public Player login(String login, String password) {
		try {
			return (Player) entityManager
					.createQuery("SELECT p FROM Player p WHERE p.login = :login AND p.password = :password")
					.setParameter("login", login).setParameter("password", password)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public boolean existsPlayer(String login) {
		try {
			Object o = (Player) entityManager.createQuery("SELECT p FROM Player p WHERE p.login = :login").setParameter("login", login).getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
}
