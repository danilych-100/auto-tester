package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.MmpoInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.OrganizationInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.OtherInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.SvhInfoWithBodyType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantrequest.WarehouseInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.result.ResultInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.simpletype.ParticipantMode;

import java.util.ArrayList;


/**
 * Transaction Body.
 */
@Data
public class Body {
    private ParticipantMode participantMode;
    private String documentID;
    private String refDocumentID;
    private String participantId;

    private String comment;

    private Signature signature;
    private OrganizationInfoType organizationInfo;
    private MyParticipantInfo participantInfo;
    private WarehouseInfoType warehouseInfo;
    private ArrayList<ResultInfoType> resultInfoList;

    private MmpoInfoType mmpoInfoType;
    private OtherInfoType otherInfoType;


}
