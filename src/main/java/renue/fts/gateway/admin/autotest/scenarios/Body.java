package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.OrganizationInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantrequest.WarehouseInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.simpletype.ParticipantMode;


/**
 * Transaction Body.
 */
@Data
public class Body {
    private ParticipantMode participantMode;
    private String refDocumentID;

    private OrganizationInfoType organizationInfo;
    private MyParticipantInfo participantInfo;
    private WarehouseInfoType warehouseInfo;


}
