package gamestudio.service;

import gamestudio.entity.Player;

public interface PlayerService {
	void register(Player player);
	Player login(String login, String password);
	boolean existsPlayer(String login);
}
