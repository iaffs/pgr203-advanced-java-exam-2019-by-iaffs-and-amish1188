package no.ingridmarcin.taskmanager;

import no.ingridmarcin.http.HttpController;
import no.ingridmarcin.http.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TaskManagerServer {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("taskmanager.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        dataSource.setUser(properties.getProperty("dataSource.user"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        HttpServer server = new HttpServer(8080);
        server.setFileLocation("src/main/resources/taskmanager");
        server.addController("/api/projects", new ProjectsController(new ProjectDao(dataSource)));
        server.startServer();

    }

}