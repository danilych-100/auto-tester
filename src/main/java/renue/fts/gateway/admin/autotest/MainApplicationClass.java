package renue.fts.gateway.admin.autotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import renue.fts.gateway.admin.autotest.jms.MyMessageSender;
//CHECKSTYLE:OFF

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class MainApplicationClass {

    /**
     * Start point of application
     */
    public static void main(final String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(MainApplicationClass.class, args);
        MyMessageSender sender = context.getBean(MyMessageSender.class);

/*
        String loadedFilePath = YMLApplicationFileLoader.loadYMLAppFileFromDir(new File("C:\\Users\\Danil\\IdeaProjects\\auto-tester\\src\\main\\resources"));
        while (loadedFilePath.equals("File not found")){
            loadedFilePath = YMLApplicationFileLoader.loadYMLAppFileFromDir(new File("C:\\Users\\Danil\\IdeaProjects\\auto-tester\\src\\main\\resources"));
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ScenariosDescription scenariosDescription = mapper.readValue(new File(loadedFilePath), ScenariosDescription.class);

        sender.sendMessage(documentBuilder.createDocFromConfig(scenariosDescription));*/
    }

}
