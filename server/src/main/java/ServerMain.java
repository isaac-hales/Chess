import chess.UserData;
import server.Server;
import io.javalin.*;

public class ServerMain {

    public static void main(String[] args) {
        //Creating the server with Javalin

        Server server = new Server();
        server.run(8080);

        System.out.println("♕ 240 Chess Server");

    }
}