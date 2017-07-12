package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Start step of configuration loading from YML file.
 */
@Component
@Data
public class ScenariosDescription {
    private Map<String,Step> steps;
    //private TransactionDefinition steps;
}
