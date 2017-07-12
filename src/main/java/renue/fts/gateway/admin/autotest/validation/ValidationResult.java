package renue.fts.gateway.admin.autotest.validation;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Danil on 12.07.2017.
 */
@Data
public class ValidationResult {

    private boolean isValid = true;
    private Map<String,String> fieldResult = new HashMap<>();

}
