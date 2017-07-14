package renue.fts.gateway.admin.autotest.documentvariable;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danil on 13.07.2017.
 */
@Data
@Component
public class VariableContainer {
    private Map<String,DocumentVariable> documentVariables = new HashMap<>();

    /**
     *
     * @param documentVariable
     */
    public void addVariable(final DocumentVariable documentVariable){
        documentVariables.put(documentVariable.getName(),documentVariable);
    }

    /**
     *
     * @param input
     * @return
     */
    public DocumentVariable getDocumentVariableFromContainer(final String input){
        String variableName = input.substring(input.indexOf('(')+1,input.indexOf(')'));
        return documentVariables.get(variableName);
    }


}
