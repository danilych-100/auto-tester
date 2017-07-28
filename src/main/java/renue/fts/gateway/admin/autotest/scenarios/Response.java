package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;

/**
 * Transaction Response.
 */
@Data
public class Response {
    private String responseName;
    private Header header;
    private Body body;
}
