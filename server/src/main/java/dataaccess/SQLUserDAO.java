package dataaccess;

import chess.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserDAO {
    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, email, hashedPassword) VALUES(?, ?, ?)")) {
                String password = user.password();
                String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.email());
                preparedStatement.setString(3,hashPassword);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, email, hashedPassword FROM users WHERE username = ?")) {
                preparedStatement.setString(1,username);
                var results = preparedStatement.executeQuery();
                if (results.next()) {
                    return new UserData(results.getString("username"),
                                        results.getString("hashedPassword"),
                                        results.getString("email"));
                }
                return null; //User is not found.
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public void clearUsers() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }
}
