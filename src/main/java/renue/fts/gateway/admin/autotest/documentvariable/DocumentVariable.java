package renue.fts.gateway.admin.autotest.documentvariable;

import lombok.Data;

/**
 * Created by Danil on 13.07.2017.
 */
@Data
public class DocumentVariable {
    private String name;
    private String value;
    private VariableType variableType;

    public DocumentVariable(final String name, final VariableType variableType,final String value) {
        this.value = value;
        this.name = name;
        this.variableType = variableType;
    }

    /**
     * @param input
     * @return
     */
    public static boolean isGenerateDocumentVariable(final String input) {
        return input.matches("^\\(generated\\.\\w+\\)$");
    }

    /**
     * @param input
     * @return
     */
    public static boolean isReceivedDocumentVariable(final String input) {
        return input.matches("^\\(received\\.\\w+\\)$");
    }

}
