package gamestudio.service;

import java.util.List;

import gamestudio.entity.Favorite;

public interface FavoritesService {
	void setFavorite(Favorite favorite);
	List<Favorite> getFavorite(String username);
}
