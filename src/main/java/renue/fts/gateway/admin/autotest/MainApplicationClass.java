package renue.fts.gateway.admin.autotest;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import renue.fts.gateway.admin.autotest.config.TestStepConfig;
import renue.fts.gateway.admin.autotest.jms.MyMessageSender;
import renue.fts.gateway.admin.autotest.loader.YMLFileLoader;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;
import com.fasterxml.jackson.dataformat.yaml.*;

import java.io.File;

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

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestStepConfig testStepConfig = mapper.readValue(new File("C:\\Users\\Danil\\IdeaProjects\\auto-tester\\src\\main\\resources\\application.yml"), TestStepConfig.class);




        YMLFileLoader ymlFileLoader = new YMLFileLoader();

        String loadedFilePath = ymlFileLoader.loadFileFromDir();
        while (loadedFilePath.equals("File not found")){
            loadedFilePath = ymlFileLoader.loadFileFromDir();
        }
        //Thread.sleep(12000);


        MyMessageSender sender = context.getBean(MyMessageSender.class);
        EnvelopeType envelopeType = new EnvelopeType();
        sender.sendMessage(envelopeType);


    }

}
