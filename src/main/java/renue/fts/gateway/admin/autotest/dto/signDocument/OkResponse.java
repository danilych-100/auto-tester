package renue.fts.gateway.admin.autotest.dto.signDocument;

/**
 * Created by azhebko on 15.11.2015.
 */
public class OkResponse extends Response {
    private SignedDocument didSign;

    public SignedDocument getDidSign() { return didSign; }
    public void setDidSign(SignedDocument didSign) { this.didSign = didSign; }
}
