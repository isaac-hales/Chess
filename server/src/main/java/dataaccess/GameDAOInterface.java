package dataaccess;

import chess.GameData;
import java.util.Collection;

public interface GameDAOInterface {
    int createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(GameData game)throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void clear() throws DataAccessException;
}