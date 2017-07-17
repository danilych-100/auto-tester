package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;
import ru.kontur.fts.eps.schemas.common.KeyInfoType;
import ru.kontur.fts.eps.schemas.common.SignedInfoType;

/**
 * Created by Danil on 17.07.2017.
 */
@Data
public class Signature {
    private SignedInfoType signedInfoType;
    private String signatureValue;
    private KeyInfoType keyInfo;
}
