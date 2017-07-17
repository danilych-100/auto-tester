package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;
import ru.kontur.fts.eps.schemas.common.KeyInfoType;
import ru.kontur.fts.eps.schemas.common.SignedInfoType;

/**
 * Signature type.
 */
@Data
public class Signature {
    private SignedInfoType signedInfoType;
    private String signatureValue;
    private KeyInfoType keyInfo;
}
