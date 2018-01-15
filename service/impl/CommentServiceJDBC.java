package gamestudio.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import gamestudio.entity.Comment;
import gamestudio.service.CommentService;

public class CommentServiceJDBC implements CommentService {

	private static final String INSERT_COMMAND = "INSERT INTO comment (ident, username, game, content, createdon) VALUES (nextval('ident_seq'), ?, ?, ?, ?)";
	private static final String SELECT_COMMAND = "SELECT ident, username, game, content, createdon FROM comment WHERE game = ? ORDER BY createdon DESC";

	@Override
	public void addComment(Comment comment) {
		try(Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER, JDBCConnection.PASSWORD)) {
			PreparedStatement ps = connection.prepareStatement(INSERT_COMMAND); {
				ps.setString(1, comment.getUsername());
				ps.setString(2, comment.getGame());
				ps.setString(3, comment.getContent());
				ps.setTimestamp(4,new Timestamp(comment.getCreatedOn().getTime()));
				ps.executeUpdate();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Comment> getComments(String game) {
		try(Connection connection = DriverManager.getConnection(JDBCConnection.URL, JDBCConnection.USER, JDBCConnection.PASSWORD)) {
			PreparedStatement ps = connection.prepareStatement(SELECT_COMMAND); {
				ps.setString(1, game);
				try (ResultSet rs = ps.executeQuery()) {
					List<Comment> list = new ArrayList<>();
					while(rs.next()) {
						Comment comment = new Comment();
						comment.setIdent(rs.getInt(1));
						comment.setUsername(rs.getString(2));
						comment.setGame(rs.getString(3));
						comment.setContent(rs.getString(4));
						comment.setCreatedOn(rs.getDate(5));
						list.add(comment);
					}
					return list;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
