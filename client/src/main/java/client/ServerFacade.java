package client;

import chess.AuthData;
import chess.GameData;
import chess.UserData;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();
    private record CreateGameRequest(String gameName) {}
    private record CreateGameResult(int gameID) {}
    private record ListGamesResult(Collection<GameData> games) {}
    private record JoinGameRequest(String playerColor, int gameID) {}

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;

    }

    public void clear() throws Exception{
        makeRequest("DELETE", "/db", null,null,null);
    }

    private <T> T makeRequest(String method, String path, Object requestBody, Class<T> responseClass, String authToken) throws Exception{
        var url = new URI(serverUrl + path).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }
        connection.setDoOutput(true);

        if (requestBody != null) {
            try (var outputStream = connection.getOutputStream()) {
                var json = gson.toJson(requestBody);
                outputStream.write(json.getBytes());
            }
        }

        var statusCode = connection.getResponseCode();
        if (statusCode != 200) {
            try (var errorStream = connection.getErrorStream()) {
                var error = gson.fromJson(new InputStreamReader(errorStream), Map.class);
                throw new Exception(error.get("message").toString());
            }
        }

        if (responseClass != null) {
            try (var inputStream = connection.getInputStream()) {
                return gson.fromJson(new InputStreamReader(inputStream), responseClass);
            }
        }
        return null;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        UserData tempUser = new UserData(username,password,email);
        return makeRequest("POST","/user",tempUser, AuthData.class, null);
    }

    public AuthData login(String username, String password) throws Exception {
        UserData tempUser = new UserData(username, password, null);
        return makeRequest("POST", "/session", tempUser, AuthData.class,null);
    }

    public void logout(String authToken) throws Exception {
        makeRequest("DELETE","/session", null, null, authToken);
    }

    public int createGame(String gameName, String authToken) throws Exception {
        var request = new CreateGameRequest(gameName);
        var result = makeRequest("POST", "/game", request, CreateGameResult.class, authToken);
        return result.gameID();
    }

    public Collection<GameData> listGames(String authToken) throws Exception {
        var result = makeRequest("GET", "/game", null, ListGamesResult.class, authToken);
        return result.games();
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws Exception {
        var request = new JoinGameRequest(playerColor,gameID);
        makeRequest("PUT","/game",request,null,authToken);
    }

}