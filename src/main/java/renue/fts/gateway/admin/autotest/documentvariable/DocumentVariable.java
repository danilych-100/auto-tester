package renue.fts.gateway.admin.autotest.documentvariable;

import lombok.Data;

/**
 * Document variable class.
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

    /*public static boolean isGenerateDocumentVariable(final String input) {
        return input.matches("^\\(generated\\.\\w+\\)$");
    }*/

    /**
     * Check is it document receive variable.
     * @param input
     * @return
     */
    public static boolean isReceivedDocumentVariable(final String input) {
        return input.matches("^\\(received\\.\\w+\\)$");
    }

}
