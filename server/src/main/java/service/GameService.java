package service;

import chess.ChessGame;
import chess.GameData;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

import java.util.Collection;

public class GameService {


    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(String authToken) {
        if (authDAO.getAuth(authToken) == null){
            throw new ServiceException(401, "Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public int createGame(String gameName,String authToken) {
        try {
            if (authDAO.getAuth(authToken) == null) {
                throw new ServiceException(401, "Error: unauthorized");
            }
            int gameID = gameDAO.listGames().size() + 1;
            GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
            ;
            gameDAO.createGame(newGame);
            return newGame.gameID();
        }
        catch (DataAccessException e){
            throw new ServiceException(500, "message" + e.getMessage());
        }
    }

    public void joinGame(String authToken, int gameID, String playerColor) {
        try {
            var authenticUser = authDAO.getAuth(authToken);
            if (authenticUser == null) {
                throw new ServiceException(401, "Error: unauthorized");
            }
            GameData updatedGame;
            GameData currentGame = gameDAO.getGame(gameID);
            String username = authenticUser.userName();
            if (currentGame == null) {
                throw new ServiceException(400, "Error: bad request");
            }
            if (playerColor == null || (!playerColor.equalsIgnoreCase("WHITE") && !playerColor.equalsIgnoreCase("BLACK"))) {
                throw new ServiceException(400, "Error: bad request");
            }
            if (playerColor.equalsIgnoreCase("WHITE") && currentGame.whiteUsername() == null) {
                updatedGame = new GameData(gameID, username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game());
            }
            //if the player joining is black
            else if (playerColor.equalsIgnoreCase("BLACK") && currentGame.blackUsername() == null) {
                updatedGame = new GameData(gameID, currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game());
            } else {
                throw new ServiceException(403, "Error: The color is already taken");
            }
            gameDAO.updateGame(updatedGame);
        }
        catch (DataAccessException e) {
            throw new ServiceException(500, "message" + e.getMessage());
        }

    }

}
