package ui;
import client.ServerFacade;

public class PreloginUI {
    private final ServerFacade facade;
    private String authToken = null;
    private String newline = "";

    public PreloginUI(ServerFacade facade) {
        this.facade = facade;
    }

    public String eval(String input) {
        var cmd = input.toLowerCase().trim();
        switch (cmd) {
            case "help" -> { return "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n" +
                    "login <USERNAME> <PASSWORD> - to play chess\n" +
                    "quit - playing chess\n" +
                    "help - with possible commands\n"; }
            case "quit" -> { return "quit"; }
            case "login" -> { }
            case "register" -> { }
            default -> { return "Unknown command. Type 'help' for options."; }
        }
    }


}
