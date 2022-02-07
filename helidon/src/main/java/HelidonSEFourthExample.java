import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.spi.ConfigSource;
import io.helidon.webserver.ServerConfiguration;
import model.MyUser;


import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.helidon.security;
import io.helidon.security.provider.httpauth.UserStore;

public class HelidonSEFourthExample {
    public static void main(String[] args) {
        Config config = Config.create();
        ServerConfiguration serverConfig =
                ServerConfiguration.fromConfig(config.get("server"));

        Map<String, MyUser> users = new HashMap<>();
        users.put("user", new MyUser("user", "user".toCharArray(), Arrays.asList("ROLE_USER")));
        users.put("admin", new MyUser("admin", "admin".toCharArray(), Arrays.asList("ROLE_USER", "ROLE_ADMIN")));
        UserStore store = user -> Optional.ofNullable(users.get(user));

        HttpBasicAuthProvider httpBasicAuthProvider = HttpBasicAuthProvider.builder()
                .realm("myRealm")
                .subjectType(SubjectType.USER)
                .userStore(store)
                .build();

        //1. Using Builder Pattern or Config Pattern
        Security security = Security.builder()
                .addAuthenticationProvider(httpBasicAuthProvider)
                .build();
        //Security security = Security.fromConfig(config);

        //2. WebSecurity from Security or from Config
        // WebSecurity webSecurity = WebSecurity.from(security)
        // .securityDefaults(WebSecurity.authenticate());

        WebSecurity webSecurity = WebSecurity.from(config);

        Routing routing = Routing.builder()
                .register(webSecurity)
                .get("/user", (request, response) -> response.send("Hello, I'm a Helidon SE user with ROLE_USER"))
                .get("/admin", (request, response) -> response.send("Hello, I'm a Helidon SE user with ROLE_ADMIN"))
                .build();

        WebServer webServer = WebServer.create(serverConfig, routing);

        webServer.start().thenAccept(ws ->
                System.out.println("Server started at: http://localhost:" + ws.port())
        );

    }
}
