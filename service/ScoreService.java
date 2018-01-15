package gamestudio.service;

import java.util.List;

import gamestudio.entity.Score;

public interface ScoreService {
	void addScore(Score score);
	
	List<Score> getTopScores(String game);
}
