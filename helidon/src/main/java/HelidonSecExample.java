
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.spi.ConfigSource;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;


public class HelidonSecExample {
    public static void main(String[] args) {
        ConfigSource configSource = ConfigSources.classpath("application.yaml").build();
        Config config = Config.builder()
                .disableSystemPropertiesSource()
                .disableEnvironmentVariablesSource()
                .sources(configSource)
                .build();

        int port = config.get("server.port").asInt();
        int pageSize = config.get("web.page-size").asInt();
        boolean debug = config.get("web.debug").asBoolean();
        String userHome = config.get("user.home").asString();

        System.out.println("port: " + port);
        System.out.println("pageSize: " + pageSize);
        System.out.println("debug: " + debug);
        System.out.println("userHome: " + userHome);
        ServerConfiguration serverConfig = ServerConfiguration.builder()
                .port(port).build();
        Routing routing = Routing.builder()
                .get("/greet", (request, response) -> response.send("Hello Latha ! This is from Second app")).build();

        WebServer.create(serverConfig, routing)
                .start()
                .thenAccept(ws ->
                        System.out.println("Server started at: http://localhost:" + ws.port())
                );
    }
}
