package ui;

import client.ServerFacade;
import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;
    private String authToken = null;


    public Repl(ServerFacade facade) {
        this.facade = facade;
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        PreloginUI preloginUI = new PreloginUI(facade);
        PostloginUI postloginUI = null;

        while (running) {
            if (authToken == null) {
                System.out.print("[LOGGED_OUT] >>> ");
            } else if (postloginUI != null && postloginUI.getGameplayUI() != null) {
                System.out.print("[IN GAME] >>> ");
            } else {
                System.out.print("[LOGGED_IN] >>> ");
            }
            String userInput = scanner.nextLine();

            if (authToken == null) {
                String result = preloginUI.eval(userInput);
                authToken = preloginUI.getAuthToken();
                if (authToken != null) {
                    postloginUI = new PostloginUI(facade, authToken); // create ONCE on login
                }
                if (result.equals("quit")) {
                    running = false;
                } else {
                    System.out.println(result);
                }
            }

            else if (postloginUI.getGameplayUI() != null) {
                try {
                    String result = postloginUI.getGameplayUI().eval(userInput);
                    if (result.equals("leave")) {
                        postloginUI.clearGameplayUI();
                    } else {
                        System.out.println(result);
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            else {
                try {
                    String result = postloginUI.eval(userInput);
                    if (result.equals("logout")) {
                        authToken = null;
                        postloginUI = null;
                        preloginUI = new PreloginUI(facade);
                    }
                    else {
                        System.out.println(result);
                    }
                }
                catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }


}
