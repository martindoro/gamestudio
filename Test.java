package gamestudio;

import java.sql.SQLException;
import java.util.Date;

import gamestudio.entity.Comment;
import gamestudio.entity.Favorite;
import gamestudio.entity.Rating;
import gamestudio.service.CommentService;
import gamestudio.service.FavoritesService;
import gamestudio.service.RatingService;
import gamestudio.service.impl.CommentServiceSORM;
import gamestudio.service.impl.FavoriteServiceSORM;
import gamestudio.service.impl.RatingServiceSORM;
import orm.SORM;

public class Test {

	public static void main(String[] args) throws IllegalAccessException, SQLException {
		// Score s = new Score();
		// s.setUsername("Aladar");
		// s.setGame("mines");
		// s.setValue(149);
		// ScoreService scoreService = new ScoreServiceSORM();
		// scoreService.addScore(s);
		// System.out.println(scoreService.getTopScores("mines"));
		//
//		 Comment c = new Comment();
//		 c.setUsername("Jozko");
//		 c.setGame("mines");
//		 c.setContent("Parada");
//		 c.setCreatedOn(new java.sql.Timestamp(new Date().getTime()));
//		 CommentService cs = new CommentServiceSORM();
//		 cs.addComment(c);
		// System.out.println(cs.getComments("mines"));

		 Rating r1 = new Rating();
		 r1.setGame("puzzle");
		 r1.setUsername("Petra");
		 r1.setValue(4);
		// Rating r2 = new Rating();
		// r2.setGame("mines");
		// r2.setUsername("Oliver");
		// r2.setValue(4);
		// Rating r3 = new Rating();
		// r3.setGame("mines");
		// r3.setUsername("Simon");
		// r3.setValue(2);
//		Rating r4 = new Rating();
//		r4.setGame("mines");
//		r4.setUsername("Zuzka");
//		r4.setValue(4);
		//r4.setIdent(4);
		RatingService rs = new RatingServiceSORM();
		 rs.setRating(r1);
		// rs.setRating(r2);
		// rs.setRating(r3);
		// rs.setRating(r4);
		// System.out.println(rs.getAverageRating("mines"));

		SORM sorm = new SORM();
//		rs.setRating(r4);
//		System.out.println(rs.getAverageRating("mines"));
		
//		Favorite fav1 = new Favorite();
//		fav1.setGame("stones");
//		fav1.setUsername("Ondrej");
//		FavoritesService fs = new FavoriteServiceSORM();
//		fs.setFavorite(fav1);
//		System.out.println(fs.getFavorite("Ondrej"));
	}

}
