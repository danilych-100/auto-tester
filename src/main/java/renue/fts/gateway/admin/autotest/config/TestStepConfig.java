package renue.fts.gateway.admin.autotest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import renue.fts.gateway.admin.autotest.document.Step;

/**
 * Created by Danil on 07.07.2017.
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "steps")
@Data
public class TestStepConfig {
   // private Map<String,Step> steps;
    private Step step1;

}
