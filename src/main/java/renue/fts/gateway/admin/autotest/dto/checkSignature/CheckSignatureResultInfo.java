package renue.fts.gateway.admin.autotest.dto.checkSignature;

import java.util.List;

/**
 * Created by azhebko on 28.10.2015.
 */
public class CheckSignatureResultInfo {
    private int source;
    private String code;
    private List<String> description;
    private int category;

    public int getSource() { return source; }
    public void setSource(int source) { this.source = source; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public List<String> getDescription() { return description; }
    public void setDescription(List<String> description) { this.description = description; }

    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }
}
