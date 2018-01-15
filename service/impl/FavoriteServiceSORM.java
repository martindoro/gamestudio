package gamestudio.service.impl;

import java.util.List;

import gamestudio.entity.Favorite;
import gamestudio.service.FavoritesService;
import orm.SORM;

public class FavoriteServiceSORM implements FavoritesService {
	private SORM sorm = new SORM();

	@Override
	public void setFavorite(Favorite favorite) {
		sorm.insert(favorite);
	}

	@Override
	public List<Favorite> getFavorite(String username) {
		return sorm.select(Favorite.class, String.format("WHERE username = '%s'", username));
	}

}
