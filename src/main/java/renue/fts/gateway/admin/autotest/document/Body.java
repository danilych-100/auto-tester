package renue.fts.gateway.admin.autotest.document;

import lombok.Data;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.OrganizationInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantrequest.WarehouseInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.simpletype.ParticipantMode;


/**
 * Created by Danil on 10.07.2017.
 */
@Data
public class Body {
    private String documentID;
    private ParticipantMode participantMode;
    private String refDocumentID;

    private OrganizationInfoType organizationInfo;
    private MyParticipantInfo participantInfo;
    private WarehouseInfoType warehouseInfo;


}
