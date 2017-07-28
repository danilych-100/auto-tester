package renue.fts.gateway.admin.autotest.dto.checkSignature;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by azhebko on 15.11.2015.
 */
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="status")
@JsonSubTypes({
        @JsonSubTypes.Type(value=OkResponse.class, name="ok"),
        @JsonSubTypes.Type(value=FailResponse.class, name="fail")
})
public abstract class Response {
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
