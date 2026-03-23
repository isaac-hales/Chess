package client;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    private <T> T makeRequest(String method, String path, Object requestBody, Class<T> responseClass, String authToken) {

        return;
    }
}