package net.gluonporridge;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.nio.file.Paths;

public class Server {
//    static class HttpFileServer {
//        static Undertow server;
//
//        public static void buildWebServer(String src) throws ServletException {
//            DeploymentInfo servletBuilder = Servlets.deployment()
//                    .setClassLoader(Server.class.getClassLoader())
//                    .setDeploymentName("testServer")
//                    .setContextPath(src);
////                    .addServlets(
////                            Servlets.servlet("UploadServlet", UploadServlet.class)
////                                    .addMapping("upload")
////                                    .setMultipartConfig(new MultipartConfigElement(null, 0, 0, 0)),
////                            Servlets.servlet("DeleteServlet", DeleteServlet.class)
////                                    .addMapping("delete"),
////                            Servlets.servlet("ImportServlet", ImportServlet.class)
////                                    .addMapping("import"),
////                            Servlets.servlet("ViewServlet", ViewServlet.class)
////                                    .addMapping("view")
////                    );
//            DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
//            manager.deploy();
//
//            HttpHandler servletHandler = manager.start();
//            PathHandler path = Handlers.path(Handlers.redirect("/"))
//                    .addPrefixPath("/fileServer", servletHandler);
//
//            server = Undertow.builder()
//                    .addHttpListener(8080, "localhost")
//                    .setHandler(path)
//                    .build();
//        }
//
//        public static void runWebServer(boolean run) {
//            if (run) {
//                server.start(); System.out.println("Running");
//            } else {
//                server.stop(); System.out.println("Stopped");
//            }
//        }
//    }

    public static void main(String args[]) {
        Undertow server = Undertow.builder()
            .addHttpListener(8080, "localhost")
            .setHandler(Handlers.resource(new PathResourceManager(Paths.get("."), 100))
                    .setDirectoryListingEnabled(true))
                .build();
        server.start();

//        HttpFileServer.buildWebServer("./src/main/web");
//        HttpFileServer.runWebServer(true);
//
//        java.util.concurrent.TimeUnit.MINUTES.sleep(10);
//        HttpFileServer.runWebServer(false);
    }
}
