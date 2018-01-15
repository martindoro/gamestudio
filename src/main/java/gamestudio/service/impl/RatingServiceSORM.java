package gamestudio.service.impl;

import java.sql.SQLException;
import java.util.List;

import gamestudio.entity.Rating;
import gamestudio.service.RatingService;
import orm.SORM;

public class RatingServiceSORM implements RatingService {
	private SORM sorm = new SORM();

	@Override
	public void setRating(Rating rating) throws IllegalAccessException, SQLException {
		sorm.update(rating);
	}

	@Override
	public double getAverageRating(String game) {
		List<Rating> list = sorm.select(Rating.class, String.format("WHERE game = '%s'", game));
		double r = 0;
		for (Rating rating : list) {
			r += rating.getValue();
		}
		return r / list.size();
	}

	@Override
	public int getUserRating(String user, String game) {
		// TODO Auto-generated method stub
		return 0;
	}

}
