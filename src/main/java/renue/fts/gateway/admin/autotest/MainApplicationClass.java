package renue.fts.gateway.admin.autotest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import renue.fts.gateway.admin.autotest.jms.MyMessageSender;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Danil on 06.07.2017.
 */
//CHECKSTYLE:OFF
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class MainApplicationClass {

    /**
     * Created by Danil on 06.07.2017.
     */
    public static void main(final String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(MainApplicationClass.class, args);
        MyMessageSender sender = context.getBean(MyMessageSender.class);

        EnvelopeType envelopeType = new EnvelopeType();
        sender.sendMessage(envelopeType);

    }

}
