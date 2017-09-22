package renue.fts.gateway.admin.autotest;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//CHECKSTYLE:OFF

@SpringBootApplication
public class MainApplicationClass {


    /**
     * Start point of application
     */
    public static void main(final String[] args) throws Exception {
        SpringApplication.run(MainApplicationClass.class, args);
    }

}
