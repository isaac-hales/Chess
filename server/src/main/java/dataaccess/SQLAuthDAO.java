package dataaccess;

import chess.AuthData;
import chess.UserData;

import java.sql.SQLException;

public class SQLAuthDAO {

    public AuthData createAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth ")) {
                preparedStatement.setString(1,username);
                AuthData newAuth = AuthData.create(username);
                var results = preparedStatement.executeUpdate();
                if (results.next()) {
                    return newAuth;
                }
                return null; //User is not found.
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public AuthData getAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, authorization FROM authTokens WHERE username = ?")) {
                preparedStatement.setString(1,username);
                var results = preparedStatement.executeQuery();
                if (results.next()) {
                    return new AuthData(results.getString("username"),
                            results.getString("authorization"));
                }
                return null; //User is not found.
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public void deleteAuth(String username) throws DataAccessException {

    }

    public void clearAuthTokens() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }
}
