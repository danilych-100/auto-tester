package renue.fts.gateway.admin.autotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//CHECKSTYLE:OFF

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class MainApplicationClass {

    /**
     * Start point of application
     */
    public static void main(final String[] args) throws Exception {
        SpringApplication.run(MainApplicationClass.class, args);
    }


}
