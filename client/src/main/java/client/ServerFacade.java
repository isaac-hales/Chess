package client;

import chess.AuthData;
import chess.UserData;

import java.net.HttpURLConnection;
import java.net.URI;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    private <T> T makeRequest(String method, String path, Object requestBody, Class<T> responseClass, String authToken) {
        var url = new URI(serverUrl + "/" + path).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        //I don't really know what to put here
        connection.setRequestProperty();
        connection.setDoOutput(true);
        try (var outputStream = connection.getOutputStream()) {

        }
        try (var inputStream = connection.getInputStream()) {

        }
        return;
    }

    public AuthData register(String username, String password, String email) {
        UserData tempUser = new UserData(username,password,email);
        return makeRequest("POST","/user",tempUser, AuthData.class, null);
    }

    public AuthData login(String username, String password){
        UserData tempUser = new UserData(username, password, null);
        return makeRequest("POST", "/session", tempUser, AuthData.class,null);
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