package renue.fts.gateway.admin.autotest.dto.checkSignature;

import java.util.List;

/**
 * Created by azhebko on 15.11.2015.
 */
public class OkResponse extends Response {
    private List<CheckSignatureResult> results;

    public List<CheckSignatureResult> getResults() { return results; }
    public void setResults(List<CheckSignatureResult> results) { this.results = results; }
}
