package ui;

import client.ServerFacade;
import java.util.Scanner;
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
        while (running) {
            String userInput = scanner.nextLine();
            if (authToken == null) {
                //PreLogin Stuff
            }
            else {
                //PostLogin Stuff
            }
        }
    }


}
