package renue.fts.gateway.admin.autotest.validation;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Result of responseInfo validation.
 */
@Data
@Component
public class ValidationResult {

    private boolean valid = true;
    private Map<String, String> fieldResult = new HashMap<>();
    private DateTime validationTime;
}
