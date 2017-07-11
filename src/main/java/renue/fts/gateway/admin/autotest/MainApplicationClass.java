package renue.fts.gateway.admin.autotest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import renue.fts.gateway.admin.autotest.config.TestStepConfig;
import renue.fts.gateway.admin.autotest.document.builder.DocumentBuilder;
import renue.fts.gateway.admin.autotest.jms.MyMessageSender;
import renue.fts.gateway.admin.autotest.loader.YMLFileLoader;
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
        MyMessageSender sender = context.getBean(MyMessageSender.class);

        YMLFileLoader ymlFileLoader = new YMLFileLoader();

        String loadedFilePath = ymlFileLoader.loadFileFromDir();
        while (loadedFilePath.equals("File not found")){
            loadedFilePath = ymlFileLoader.loadFileFromDir();
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestStepConfig testStepConfig = mapper.readValue(new File(loadedFilePath), TestStepConfig.class);

        sender.sendMessage(DocumentBuilder.createDocFromConfig(testStepConfig));
    }

}
