package renue.fts.gateway.admin.autotest.documentvariable;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for document variables.
 */
@Data
@Component
public class VariableContainer {
    private Map<String,DocumentVariable> documentVariables = new HashMap<>();

    /**
     * Add variable in container.
     * @param documentVariable
     */
    public void addVariable(final DocumentVariable documentVariable){
        documentVariables.put(documentVariable.getName(),documentVariable);
    }

    /**
     * Get variable from container.
     * @param input
     * @return
     */
    public DocumentVariable getDocumentVariableFromContainer(final String input) throws Exception{
        String variableName = input.substring(input.indexOf('(')+1,input.indexOf(')'));
        return documentVariables.get(variableName);
    }


}
