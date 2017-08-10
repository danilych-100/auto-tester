package renue.fts.gateway.admin.autotest.transaction;

import lombok.Data;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;

import java.util.Map;

/**
 * Created by Danil on 27.07.2017.
 */
@Data
public class TransactionInfo {
    private String transactionName;
    private Map<String,ValidationResult> responseInfo;

    public TransactionInfo(final String transactionName,
                           final Map<String, ValidationResult> responseInfo) {
        this.transactionName = transactionName;
        this.responseInfo = responseInfo;
    }

}
