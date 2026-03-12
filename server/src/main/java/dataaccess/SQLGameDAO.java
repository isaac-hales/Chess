package dataaccess;

import chess.ChessGame;
import chess.GameData;
import com.google.gson.Gson;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAOInterface {

    public void createGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, gameName, whiteUsername, blackUsername, chessGame) VALUES(?, ?, ?, ?, ?)")) {
                String chessGameJson = new Gson().toJson(game.game());
                preparedStatement.setInt(1, game.gameID());
                preparedStatement.setString(2, game.gameName());
                preparedStatement.setString(3, game.whiteUsername());
                preparedStatement.setString(4, game.blackUsername());
                preparedStatement.setString(5, chessGameJson);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID = ?")) {
                preparedStatement.setInt(1,gameID);
                var results = preparedStatement.executeQuery();
                if (results.next()) {
                    String chessGameJson = results.getString("chessGame");
                    ChessGame chessGame = new Gson().fromJson(chessGameJson, ChessGame.class);
                    return new GameData(results.getInt("gameID"), results.getString("whiteUsername"),
                            results.getString("blackUsername"), results.getString("gameName"), chessGame);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
                var results = preparedStatement.executeQuery();
                var games = new ArrayList<GameData>();
                while (results.next()) {
                    String chessGameJson = results.getString("chessGame");
                    ChessGame chessGame = new Gson().fromJson(chessGameJson, ChessGame.class);
                    GameData tempGame = new GameData(results.getInt("gameID"), results.getString("whiteUsername"),
                            results.getString("blackUsername"), results.getString("gameName"), chessGame);
                    games.add(tempGame);
                }
                return games; //User is not found.
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET whiteUsername=?, blackUsername=?, chessGame=? WHERE gameID=?")) {
                String chessGameJson = new Gson().toJson(game.game());
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, chessGameJson);
                preparedStatement.setInt(4, game.gameID());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

    public void clearGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error message", e);
        }
    }

}
