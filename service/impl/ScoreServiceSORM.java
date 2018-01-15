package gamestudio.service.impl;

import java.util.List;

import gamestudio.entity.Score;
import gamestudio.service.ScoreService;
import orm.SORM;

public class ScoreServiceSORM implements ScoreService {
	private SORM sorm = new SORM();

	@Override
	public void addScore(Score score) {
		sorm.insert(score);
	}

	@Override
	public List<Score> getTopScores(String game) {
		return sorm.select(Score.class, String.format("WHERE game = '%s' ORDER BY value DESC LIMIT 10", game));
	}
}
