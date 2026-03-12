package dataaccess;

import chess.GameData;
import java.util.Collection;

public interface GameDAOInterface {
    void createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(GameData game)throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void clearGames() throws DataAccessException;
}