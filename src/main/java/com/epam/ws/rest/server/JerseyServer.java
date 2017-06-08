package com.epam.ws.rest.server;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class JerseyServer {

    public static void main(String[] args) throws Exception {
        setUp();
    }

    public static void setUp() throws Exception {
        ResourceConfig config = new ResourceConfig();
        config.register(MoxyXmlFeature.class);
        config.register(JacksonFeature.class);
        config.register(MultiPartFeature.class);
        config.packages("com.epam.ws.rest");

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/services/*");


        String resourceBasePath = JerseyServer.class.getResource("/WEB-INF").toExternalForm();
        context.setWelcomeFiles(new String[] {"index.html"});
        context.setResourceBase(resourceBasePath);
        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");


        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}