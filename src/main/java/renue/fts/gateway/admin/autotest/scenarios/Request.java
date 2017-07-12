package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;

/**
 * Transaction Request contains: Header, Body.
 */
@Data
public class Request {
    private Header header;
    private Body body;

}
