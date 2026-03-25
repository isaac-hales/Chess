package ui;
import client.ServerFacade;

public class PreloginUI {
    private final ServerFacade facade;
    private String authToken = null;

    public PreloginUI(ServerFacade facade) {
        this.facade = facade;
    }

    public String eval(String input) {
        var parts = input.split(" ");
        var cmd = parts[0].toLowerCase().trim();
        switch (cmd) {
            case "help" -> { return "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n" +
                    "login <USERNAME> <PASSWORD> - to play chess\n" +
                    "quit - playing chess\n" +
                    "help - with possible commands\n"; }
            case "quit" -> { return "quit"; }
            case "login" -> {
                if (parts.length != 3) {
                    return "Usage: login <USERNAME> <PASSWORD>";
                }
                try {
                    authToken = facade.login(parts[1], parts[2]).authToken();
                    return "Successfully logged in!";
                }
                catch (Exception e) {
                    return "Login failed: " + e.getMessage();
                }
            }
            case "register" -> {
                if (parts.length != 4) {
                    return "Usage: register <USERNAME> <PASSWORD> <EMAIL>";
                }
                try {
                    authToken = facade.register(parts[1],parts[2],parts[3]).authToken();
                    return "Successfully logged in!";
                }
                catch (Exception e) {
                    return "Login failed: " + e.getMessage();
                }
            }
            default -> { return "Unknown command. Type 'help' for options."; }
        }
    }

    public String getAuthToken() {
        return authToken;
    }


}
