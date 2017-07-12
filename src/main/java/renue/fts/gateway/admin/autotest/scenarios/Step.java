package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;

/**
 * Transaction contains: Name, Request, Response.
 */
@Data
public class Step {
    private String name;
    private Request request;
    private Response response;

}
