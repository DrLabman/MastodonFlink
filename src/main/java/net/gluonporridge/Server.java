package net.gluonporridge;

import com.google.gson.Gson;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import net.gluonporridge.jpa.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.nio.file.Paths;
import java.util.List;

public class Server {

    public static void main(String args[]) {
        ResourceHandler fileHandler = Handlers.resource(new PathResourceManager(Paths.get("./src/main/web"), 100))
                .setDirectoryListingEnabled(true);

        PathHandler pathHandler = Handlers.path(new ResponseCodeHandler(404))
                .addPrefixPath("/", fileHandler)
                .addExactPath("/status", new StatusHandler())
                .addExactPath("/boosts", new BoostHandler());

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(pathHandler)
                .build();
        server.start();
    }

    public static abstract class JPAHandler implements HttpHandler {
        protected static final HttpString CORS = new HttpString("Access-Control-Allow-Origin");

        protected final EntityManagerFactory sessionFactory;
        protected final EntityManager entityManager;

        public JPAHandler() {
            sessionFactory = Persistence.createEntityManagerFactory("net.gluonporridge.mastodon.jpa");
            entityManager = sessionFactory.createEntityManager();
        }

        protected void sendResponse(HttpServerExchange exchange, List objects) {
            Gson gson = new Gson();
            String response = gson.toJson(objects);

            exchange.setStatusCode(200);
            exchange.getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, "application/json")
                    .put(CORS, "*");
            exchange.getResponseSender().send(response);
        }
    }

    public static class StatusHandler extends JPAHandler {
        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            List<Status> status = entityManager.createNamedQuery("GetLatestStatuses").setMaxResults(40).getResultList();
            sendResponse(exchange, status);
        }
    }

    public static class BoostHandler extends JPAHandler {
        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            List<Status> status = entityManager.createNamedQuery("GetLatestBoosts").setMaxResults(40).getResultList();
            sendResponse(exchange, status);
        }
    }
}
