package renue.fts.gateway.admin.autotest.dto.checkSignature;

import java.util.List;

/**
 * Created by azhebko on 28.10.2015.
 */
public class CheckSignatureResult {
    private String refDocumentId;
    private List<CheckSignatureResultInfo> information;

    public String getRefDocumentId() { return refDocumentId; }
    public void setRefDocumentId(String refDocumentId) { this.refDocumentId = refDocumentId; }

    public List<CheckSignatureResultInfo> getInformation() { return information; }
    public void setInformation(List<CheckSignatureResultInfo> information) { this.information = information; }
}
