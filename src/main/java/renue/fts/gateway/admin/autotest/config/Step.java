package renue.fts.gateway.admin.autotest.config;

import lombok.Data;

/**
 * Created by Danil on 07.07.2017.
 */
@Data
public class Step {
    private String name;
    private Request request;
    private Response response;

}
