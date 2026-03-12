package dataaccess;

import chess.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAOInterface{

    public AuthData createAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth(username, authToken) VALUES(?, ?) ")) {
                AuthData newAuth = AuthData.create(username);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, newAuth.authToken());
                preparedStatement.executeUpdate();
                return newAuth;
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE authToken = ?")) {
                preparedStatement.setString(1,authToken);
                var results = preparedStatement.executeQuery();
                if (results.next()) {
                    return new AuthData(results.getString("authToken"),
                            results.getString("username"));
                }
                return null; //User is not found.
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken = ?")) {
                preparedStatement.setString(1,authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    @Override
    public void clearAuth() throws DataAccessException {
        clear();
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }
}
