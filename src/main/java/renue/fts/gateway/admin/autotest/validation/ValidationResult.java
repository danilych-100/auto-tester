package renue.fts.gateway.admin.autotest.validation;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Result of response validation.
 */
@Data
@Component
public class ValidationResult {

    private boolean isValid = true;
    private Map<String, String> fieldResult = new HashMap<>();

}
