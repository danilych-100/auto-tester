package renue.fts.gateway.admin.autotest.dto.checkSignature;

import java.util.List;

/**
 * Created by azhebko on 15.11.2015.
 */
public class FailResponse extends Response {
    private String message;
    private String exception;
    private List<String> stack;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getException() { return exception; }
    public void setException(String exception) { this.exception = exception; }

    public List<String> getStack() { return stack; }
    public void setStack(List<String> stack) { this.stack = stack; }
}
