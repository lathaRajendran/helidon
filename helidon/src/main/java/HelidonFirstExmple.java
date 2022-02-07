import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
//Run the app and execute http://loclhost:9001/greet
public class HelidonFirstExmple {
    public static void main(String[] args) {
        ServerConfiguration serverConfig = ServerConfiguration.builder()
                .port(9001).build();
        Routing routing = Routing.builder()
                .get("/greet", (request, response) -> response.send("Hello Latha . Good Morning !")).build();
        WebServer.create(serverConfig, routing)
                .start()
                .thenAccept(ws ->
                        System.out.println("Server started at: http://localhost:" + ws.port())
                );
    }
}
