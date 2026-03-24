package client;

import chess.AuthData;
import chess.ChessGame;
import chess.GameData;
import chess.UserData;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();
    private record CreateGameRequest(String gameName) {}
    private record CreateGameResult(int gameID) {}

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;

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
}


/***
 * // 1. Build the URL
 * var url = new URI(serverUrl + "/user").toURL();
 *
 * // 2. Open a connection
 * HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 *
 * // 3. Set the request method
 * connection.setRequestMethod("POST");
 *
 * // 4. Set headers if needed (like Content-Type or Authorization)
 * connection.setRequestProperty("Content-Type", "application/json");
 *
 * // 5. Write the request body if needed
 * connection.setDoOutput(true);
 * try (var outputStream = connection.getOutputStream()) {
 *     // write JSON body here using Gson
 * }
 *
 * // 6. Read the response
 * try (var inputStream = connection.getInputStream()) {
 *     // read JSON response here using Gson
 * }
 */