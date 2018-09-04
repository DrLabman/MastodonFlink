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
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import net.gluonporridge.jpa.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.nio.file.Paths;
import java.util.List;

public class Server {

    public static void main(String args[]) {
        ResourceHandler fileHandler = Handlers.resource(new PathResourceManager(Paths.get("./src/main/web"), 100))
                .setDirectoryListingEnabled(true);

        PathHandler pathHandler = Handlers.path(new ResponseCodeHandler(404))
                .addPrefixPath("/", fileHandler)
                .addExactPath("/status", new StatusHandler());


        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(pathHandler)
                .build();
        server.start();

//        HttpFileServer.buildWebServer("./src/main/web");
//        HttpFileServer.runWebServer(true);
//
//        java.util.concurrent.TimeUnit.MINUTES.sleep(10);
//        HttpFileServer.runWebServer(false);
    }

    public static class StatusHandler implements HttpHandler {

        public static final HttpString CORS = new HttpString("Access-Control-Allow-Origin");

        private final EntityManagerFactory sessionFactory;
        private final EntityManager entityManager;

        public StatusHandler() {
            sessionFactory = Persistence.createEntityManagerFactory("net.gluonporridge.mastodon.jpa");
            entityManager = sessionFactory.createEntityManager();
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            int statusCode = 200;

            List<Status> status = entityManager.createNamedQuery("GetLatest").setMaxResults(10).getResultList();

            Gson gson = new Gson();
            String response = gson.toJson(status);

            exchange.setStatusCode(statusCode);
            exchange.getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, "application/json")
                    .put(CORS, "*");
            exchange.getResponseSender().send(response);
        }
    }
}
