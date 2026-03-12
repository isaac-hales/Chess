package dataaccess;

import chess.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAOInterface {
    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, email, hashedPassword) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.email());
                preparedStatement.setString(3, user.password());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            throw new DataAccessException("error message", e);
        }
    }


    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("error message", e);
        }
    }
}
