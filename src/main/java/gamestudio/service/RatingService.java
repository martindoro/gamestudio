package gamestudio.service;

import java.sql.SQLException;

import gamestudio.entity.Rating;

public interface RatingService {
	void setRating(Rating rating) throws IllegalAccessException, SQLException;
	double getAverageRating(String game);
	int getUserRating(String user, String game);
}
