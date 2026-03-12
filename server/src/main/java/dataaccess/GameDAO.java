package dataaccess;

import chess.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDAO implements GameDAOInterface {
    private final Map<Integer, GameData> allChessGames = new HashMap<>(); //integer is key, and GameData is value

    public Collection<GameData> listGames() {
        return allChessGames.values();
    }

    public void createGame(GameData game) throws DataAccessException {
        if (game == null){
            throw new DataAccessException("Game does not exist");
        }
        allChessGames.put(game.gameID(), game);
    }

    public GameData getGame(int gameID){
        return allChessGames.get(gameID);
    }

    public void updateGame(GameData game) throws DataAccessException {
        if (!allChessGames.containsKey(game.gameID())){
            throw new DataAccessException("Game does not exist");
        }
        allChessGames.put(game.gameID(),game);
    }


    public void clear() throws DataAccessException {
        allChessGames.clear();
    }
}
