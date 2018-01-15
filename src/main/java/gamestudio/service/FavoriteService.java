package gamestudio.service;

import java.util.List;

import gamestudio.entity.Favorite;

public interface FavoriteService {
	void setFavorite(Favorite favorite);
	List<Favorite> getFavorite(String username);
	boolean isFavorite(Favorite favorite);
}
